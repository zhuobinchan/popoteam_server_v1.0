package com.geetion.puputuan.engine.handler;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 责任链--匹配队伍
 */
public abstract class Matcher {

    protected Matcher matcher;

    protected List<Group> matchGroup = new ArrayList<>();

    protected GroupService groupService = null;

    protected GroupMemberService groupMemberService = null;

    protected RecommendService recommendService = null;

    protected UserRecommendService userRecommendService = null;

    protected CommonService commonService = null;

    protected ShiroService shiroService = null;

    protected RecommendSuccessService recommendSuccessService = null;

    protected ActivityService activityService = null;

    protected UserLikeGroupService userLikeGroupService = null;
    protected TransactionHelper transactionHelper = null;
    private final Logger logger = Logger.getLogger(Matcher.class);
    /**
     * 匹配队伍
     */
    public abstract boolean matchGroup(Long groupId, Long userId, Date time);

    /**
     * 执行下一个处理者
     * @param groupId
     * @return
     */
    protected boolean nextMatchGroup(Long groupId, Long userId, Date time){
        if (this.getMatcher() != null){
          return this.getMatcher().matchGroup(groupId, userId,time);
        }
        return false;
    }
    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    // 初始化
    {
        groupService = SpringLoader.getInstance().getBean(GroupService.class);
        groupMemberService = SpringLoader.getInstance().getBean(GroupMemberService.class);
        recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
        userRecommendService = SpringLoader.getInstance().getBean(UserRecommendService.class);
        commonService = SpringLoader.getInstance().getBean(CommonService.class);
        shiroService = SpringLoader.getInstance().getBean(ShiroService.class);
        recommendSuccessService = SpringLoader.getInstance().getBean(RecommendSuccessService.class);
        activityService = SpringLoader.getInstance().getBean(ActivityService.class);
        userLikeGroupService = SpringLoader.getInstance().getBean(UserLikeGroupService.class);
        transactionHelper = SpringLoader.getInstance().getBean(TransactionHelper.class);

    }

    /**
     * 选出投票给本队的异性队伍
     * @param groupId
     * @return
     */
    public List<Group> selectVoteGroup(Long groupId){
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        return this.groupService.selectVoteGroup(params);
    }

    /**
     * 添加约会
     * @param mainGroup
     * @param matchGroup
     */
    public void addRecommend(Group mainGroup, List<Group> matchGroup, List<Recommend> recommends){
        Long groupId = mainGroup.getId();
        for(Group g : matchGroup){
            Recommend recommend = new Recommend();
            recommend.setMainGroupId(groupId);
            recommend.setMatchGroupId(g.getId());
            recommend.setCreateTime(new Date());
            recommends.add(recommend);
        }
        if(recommends.size() > 0){
            recommendService.addRecommendBatch(recommends);
        }

    }

    /**
     * 添加用户推荐
     * @param mainGroup
     * @param recommends
     */
    public void addUserRecommend(Group mainGroup, List<Recommend> recommends, List<UserRecommend> userRecommends){

        Map<String, Object> mainGroupParams = new HashMap<>();
        mainGroupParams.put("groupId", mainGroup.getId());
        mainGroupParams.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS,
                GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(mainGroupParams);

        for(Recommend r : recommends){
            //添加主队的用户约会
            this.insertRecommendBatch(r,groupMemberList, userRecommends);
            //添加匹配队伍的用户约会
            //this.insertRecommendBatch(r,groupMembers, userRecommends);
        }

    }

    /**
     * 批量添加用户推荐
     * @param recommend
     * @param groupMembers
     */
    private void insertRecommendBatch(Recommend recommend, List<GroupMember> groupMembers, List<UserRecommend> userRecommends){

        List<UserRecommend> userRecommendList = new ArrayList<>();
        for(GroupMember gm : groupMembers){
            UserRecommend ur = this.convertGroupMemberToUserRecommend(recommend,gm);
            userRecommendList.add(ur);
        }
        userRecommendService.addUserRecommendBatch(userRecommendList);
        userRecommends.addAll(userRecommendList);
    }

    /**
     * 转换UserRecommend
     * @param recommend
     * @param groupMember
     * @return
     */
    private UserRecommend convertGroupMemberToUserRecommend(Recommend recommend, GroupMember groupMember){
        UserRecommend ur = new UserRecommend();
        ur.setUserId(groupMember.getUserId());
        ur.setRecommendId(recommend.getId());
        ur.setStatus(RecommendType.NOT_SEND);
        ur.setCreateTime(new Date());
        return  ur;
    }

    /**
     * 通过环信发送消息
     */
    protected synchronized void sendRecommendMsgHuanxin(Group mainGroup, Long userId){
        logger.info("Matcher mainGroup = " + mainGroup + "userId = " + userId);
        logger.info("Matcher thread name = " + Thread.currentThread().getName());
        List<GroupMember> groupMemberList = commonService.getGroupMember(mainGroup.getId());
        //第一次匹配成功时，队伍成员进入投票状态，将队伍成员状态改成投票中
        //如果为重新匹配成功时，需要将新进入的队伍成员状态也改成投票中
        List<GroupMember> voteGroupMemberList = new ArrayList<>();
        List<String> groupMemberTokens = new ArrayList<>();

        for (GroupMember groupMember : groupMemberList) {
            // 当用户第一次进队伍时，需要给用户推送推荐队伍
            if (groupMember.getStatus().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS)){
                groupMemberTokens.add(groupMember.getUser().getUserBase().getAccount());
                groupMember.setStatus(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);
                logger.info("Matcher voteGroupMemberList add " + groupMember.getUser().getUserBase().getAccount());
                voteGroupMemberList.add(groupMember);
            }
            // 当用户状态为投票中时，只需要给自己推送消息
            if (groupMember.getStatus().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE)){
                List<UserRecommend> userRecommendList = userRecommendService.getUserRecommendByUserId(groupMember.getUserId(), RecommendType.NOT_SEND);
                logger.info("Matcher userRecommendList size " + userRecommendList.size());

                if(groupMember.getUserId().equals(userId) && (null == userRecommendList || userRecommendList.size() <= 0)){
                    logger.info("Matcher groupMemberTokens add " + groupMember.getUser().getUserBase().getAccount());
                    groupMemberTokens.add(groupMember.getUser().getUserBase().getAccount());
                }
            }
        }
        // 更新队伍成员状态为投票中
        for (GroupMember gm : voteGroupMemberList){
            groupMemberService.updateGroupMember(gm);
        }

        //发送环信消息 将推荐结果发送给当前队伍
        HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
        logger.info("Matcher groupMemberTokens add " + groupMemberTokens.toArray(new String[groupMemberTokens.size()]));
        try {
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                    "恭喜，匹配到新的队伍", HuanXinSendMessageType.HAVE_MATCHING_RESULT, huanXinMessageExtras,
                    groupMemberTokens.toArray(new String[groupMemberTokens.size()]));
        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送环信消息
     * 如果匹配算法匹配到新队伍，需要检查memberWaitingMap，判断是否需要给其他队员发送推送消息
     * 如果用户重新用户推荐信息表，则只给当前用户发消息
     * @param mainGroup
     * @param userId
     * @param ifRest
     */
    protected synchronized void sendRecommendMsgHuanxinByType(Group mainGroup, Long userId, boolean ifRest){
        logger.info("Matcher mainGroup = " + mainGroup + "userId = " + userId);
        logger.info("Matcher thread name = " + Thread.currentThread().getName());
        List<GroupMember> groupMemberList = commonService.getGroupMember(mainGroup.getId());
        //第一次匹配成功时，队伍成员进入投票状态，将队伍成员状态改成投票中
        //如果为重新匹配成功时，需要将新进入的队伍成员状态也改成投票中
        List<GroupMember> voteGroupMemberList = new ArrayList<>();
        List<String> groupMemberTokens = new ArrayList<>();
        String[] token = new String[groupMemberList.size()];
//        ConcurrentHashMap waitingMap ;
//        if(1 == type){
//            waitingMap = ShareCacheVar.memberWaitingMap;
//        }else {
//            waitingMap = ShareCacheVar.memberNoMatchWaitingMap;
//        }

        if (ifRest){
            // 重置用户推荐信息表，只需要给当前用户推送消息
            token = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, userId);
            if ( ShareCacheVar.memberWaitingMap.containsKey(mainGroup.getId())){
                List<Long> members = (List)  ShareCacheVar.memberWaitingMap.get(mainGroup.getId());
                // 当前用户重置用户推荐表数据，通过等待中的第一个用户重新进行重置。
                // ps：正常情况下，是可以直接将等待中的用户直接放入队列中，全部重新匹配规则。
                // 但是考虑中，可能过程中有新的队伍生成，那么会出现重复数据
                if(members.size() > 0){
                    HashMap<String, Long> map = new HashMap<>();
                    map.put("groupId", mainGroup.getId());
                    map.put("userId", members.get(0));
                    map.put("isReMatch", 0L);
                    map.put("isValid", 1L);
                    ShareCacheVar.groupWaitingQueue.add(map);
                    members.remove(0);
                }
            }
        }else{

            if( ShareCacheVar.memberWaitingMap.containsKey(mainGroup.getId())){
                // 取得统计中等的队伍成员
                List<Long> members = (List)  ShareCacheVar.memberWaitingMap.get(mainGroup.getId());
                boolean ifInWaitingMember = false;
                // 去除可能存在的赃数据
                for(Long m : members){
                    if(m.longValue() == userId.longValue()){
                        ifInWaitingMember = true;
                    }
                }
                if (! ifInWaitingMember){
                    // 添加当前用户
                    members.add(userId);
                }
                // 取得需要推送的用户token
                token = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, members.toArray(new Long[members.size()]));

                // 移除等待执行的用户
                ShareCacheVar.memberWaitingMap.remove(mainGroup.getId());
            }else{
                token = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, userId);
            }

            for (GroupMember groupMember : groupMemberList) {
                // 当用户第一次进队伍时，需要给用户推送推荐队伍
                if (groupMember.getStatus().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS)){
                    groupMemberTokens.add(groupMember.getUser().getUserBase().getAccount());
                    groupMember.setStatus(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);
                    voteGroupMemberList.add(groupMember);
                }
            }
        }

        // 更新队伍成员状态为投票中
        for (GroupMember gm : voteGroupMemberList){
            groupMemberService.updateGroupMember(gm);
        }

        for (int i = 0; i < token.length; i++){
            if (!groupMemberTokens.contains(token[i])){
                groupMemberTokens.add(token[i]);
            }
        }


        logger.info("Matcher groupMemberTokens add " + groupMemberTokens.toArray(new String[groupMemberTokens.size()]));

        try {
            logger.info("Matcher sendRecommendMsgHuanxinByType tokens " + groupMemberTokens.size() + groupMemberTokens.toString());
            logger.info("Matcher sendRecommendMsgHuanxinByType ifReset " + ifRest);
            logger.info("======================================================================");

            //发送环信消息 将推荐结果发送给当前队伍
            HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
            huanXinMessageExtras.setIfReset(ifRest);
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                    "恭喜，匹配到新的队伍", HuanXinSendMessageType.HAVE_MATCHING_RESULT, huanXinMessageExtras,
                    groupMemberTokens.toArray(new String[groupMemberTokens.size()]));
        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断当前执行的线程队伍信息是否是最新的
     * @param mainGroup
     * @return
     */
    public boolean ifNewestGroup(Group mainGroup, Date time){

        // 判断当前队伍信息是否修改过
        if(ShareCacheVar.groupModifyMap.containsKey(mainGroup.getId())){
            Date date = (Date) ShareCacheVar.groupModifyMap.get(mainGroup.getId());
            logger.info("militime between modifyTime and time " + (time.getTime() - date.getTime()));
            // 判断两个时间之间的相差秒数
            long second = (time.getTime() - date.getTime()) / 1000;
            if(second >= 0){
                // 当前线程中的队伍信息为最新信息，将缓存中的队伍记录清除
                ShareCacheVar.groupModifyMap.remove(mainGroup.getId());
                logger.info("=============== current group info is newest");
                return true;
            }else{
                logger.info("=============== current group info is not newest");
                return false;
            }
        }
        return true;
    }

    /**
     * 合并队伍
     * @param matchGroupList
     * @param voteGroupList
     * @return
     */
    public List<Group> mergeGroup(List<Group> matchGroupList, List<Group> voteGroupList){

        List<Group> mergerGroupList = new ArrayList<>();

        Map<Long, Group> mergeMap = new HashMap<>();
        for(Group group : matchGroupList){
            mergeMap.put(group.getId(), group);
        }
        for(Group voteGroup: voteGroupList){
            mergeMap.put(voteGroup.getId(), voteGroup);
        }

        Iterator<Long> iterator = mergeMap.keySet().iterator();
        while (iterator.hasNext()){
            Long key = iterator.next();
            mergerGroupList.add(mergeMap.get(key));
        }

        return mergerGroupList;
    }

    /**
     *  从合并的队伍移除superLike的队伍
     * @param mainGroup
     * @param mergeGroup
     */
    public void excludeSuperLikeGroup(Group mainGroup, List<Group> mergeGroup){
        Map<String, Object> params = new HashMap<>();

//        if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
//            params.put("femaleGroupId", mainGroup.getId());
//        }else {
//            params.put("maleGroupId", mainGroup.getId());
//        }
        params.put("groupAId", mainGroup.getId());
        params.put("type", ActivityTypeAndStatus.BEGIN);
        params.put("superLike", 1);

        List<Activity> activityList = activityService.getActivityList(params);

        List<Long> superLikeGroupList = new ArrayList<>();
        for (Activity a : activityList){
//            if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
//                superLikeGroupList.add(a.getMaleGroupId());
//            }else {
//                superLikeGroupList.add(a.getFemaleGroupId());
//            }

            superLikeGroupList.add(a.getGroupBId());
        }

        // 将合并list中superlike过的队伍找出来
        List<Group> waitToRemoveList = new ArrayList<>();
        for (Group g : mergeGroup){
            for(Long l : superLikeGroupList){
                if(g.getId().equals(l)){
                    waitToRemoveList.add(g);
                }
            }
        }
        mergeGroup.removeAll(waitToRemoveList);
    }

    /**
     * 排除用户不喜欢的队伍
     * @param userId
     * @param mainGroup
     * @param mergeGroup
     */
    public void excludeDislikeGroup(Long userId, Group mainGroup, List<Group> mergeGroup){
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", mainGroup.getId());
        params.put("isLike", 0);
        List<UserLikeGroup> userLikeGroupList = userLikeGroupService.getUserLikeGroupList(params);

        List<Group> waitToRemoveList = new ArrayList<>();
        for (Group g : mergeGroup){
            for(UserLikeGroup u : userLikeGroupList){
                if(g.getId().equals(u.getLikeGroupId())){
                    waitToRemoveList.add(g);
                }
            }
        }
        mergeGroup.removeAll(waitToRemoveList);

    }

    public void pollLikeGroupToFirst(Long userId, Group mainGroup, List<Group> mergeGroup){
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", mainGroup.getId());
        params.put("isLike", UserLikeGroupType.Like);
        List<UserLikeGroup> userLikeGroupList = userLikeGroupService.getUserLikeGroupList(params);

        if (null != userLikeGroupList && userLikeGroupList.size() >= 1){

            List<Group> sortList = new ArrayList<>();
            // 将用户喜欢的队伍放到最前面
            Group likeGroup = groupService.getGroupById(userLikeGroupList.get(0).getLikeGroupId());

            params.clear();
            params.put("mainGroupId", mainGroup.getId());
            params.put("matchGroupId", likeGroup.getId());
            Recommend recommend = recommendService.getRecommend(params);
            // 第一次进行匹配或重新匹配时，才需要将喜欢的队伍放到最前面，后面再进行匹配时，不需要进行前置队伍
            if( null != recommend ){
                return;
            }

            if (null != likeGroup){
                sortList.add(likeGroup);
            }
            for (Group g : mergeGroup){
                if (!g.getId().equals(likeGroup.getId())){
                    sortList.add(g);
                }
            }
            mergeGroup.clear();
            mergeGroup.addAll(sortList);
        }


    }
}
