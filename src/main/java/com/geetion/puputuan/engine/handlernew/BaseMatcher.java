package com.geetion.puputuan.engine.handlernew;


import com.easemob.server.example.constant.HuanXinConstant;
//import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import com.geetion.puputuan.utils.RedisUtil;
import com.geetion.puputuan.utils.RunMatchUtil;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;

/**
 * Created by guodikai on 2017/1/9.
 */
public class BaseMatcher {

    private final Logger logger = Logger.getLogger(BaseMatcher.class);

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

    protected RedisUtil redisUtil = null;

    protected RunMatchUtil runMatchUtil = null;
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
        redisUtil = SpringLoader.getInstance().getBean(RedisUtil.class);
        runMatchUtil = SpringLoader.getInstance().getBean(RunMatchUtil.class);
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
        Map<String, Object> params = new HashMap<>();

        for(Group g : matchGroup){
            params.clear();
            params.put("mainGroupId", mainGroup.getId());
            params.put("matchGroupId", g.getId());

//            if(null == recommendService.getRecommend(params)){
                Recommend recommend = new Recommend();
                recommend.setMainGroupId(groupId);
                recommend.setMatchGroupId(g.getId());
                recommend.setCreateTime(new Date());
                recommends.add(recommend);
                try{
                    recommendService.addRecommend(recommend);
                }catch (Exception e){
                    e.printStackTrace();
                }

//            }
        }
//        if(recommends.size() > 0){
//            recommendService.addRecommendBatch(recommends);
//        }

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
            // 当存在重复操作时，更新recommend的创建时间，此时id值为null，不进行操作
            if(null != r.getId()){
                //添加主队的用户约会
                this.insertRecommendBatch(r,groupMemberList, userRecommends);
            }
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
        if (userRecommendList.size() > 0) {
            userRecommendService.addUserRecommendBatch(userRecommendList);
        }
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
     * 合并队伍
     * @param matchGroupList
     * @param voteGroupList
     * @return
     */
    public List<Group> mergeGroup(List<Group> matchGroupList, List<Group> voteGroupList){

        List<Group> mergerGroupList = new ArrayList<>();
        List<Group> waitToGroupList = new ArrayList<>();

        mergerGroupList.addAll(voteGroupList);

        boolean ifToAdd = true;
        for (Group g : matchGroupList){
            for (Group v : voteGroupList){
                if (g.getId().intValue() == v.getId().intValue()){
                    ifToAdd = false;
                    break;
                }
            }
            if (ifToAdd){
                waitToGroupList.add(g);
            }
            ifToAdd = true;
        }

        mergerGroupList.addAll(waitToGroupList);


//        Map<Long, Group> mergeMap = new HashMap<>();
//        for(Group group : matchGroupList){
//            logger.info(" ==============================match group " + group.getId() + " ===================================");
//            mergeMap.put(group.getId(), group);
//        }
//        for(Group voteGroup: voteGroupList){
//            logger.info(" ==============================match group " + voteGroup.getId() + " ===================================");
//            mergeMap.put(voteGroup.getId(), voteGroup);
//        }
//
//        Iterator<Long> iterator = mergeMap.keySet().iterator();
//        while (iterator.hasNext()){
//            Long key = iterator.next();
//            mergerGroupList.add(mergeMap.get(key));
//        }

        return mergerGroupList;
    }

    /**
     *  从合并的队伍移除superLike(或已组成约会)的队伍
     * @param mainGroup
     * @param mergeGroup
     */
    public void excludeSuperLikeGroup(Group mainGroup, List<Group> mergeGroup){
        Map<String, Object> params = new HashMap<>();

        logger.info("excludeSuperLikeGroup mainGroup id is " + mainGroup.getId());
//        if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
//            params.put("femaleGroupId", mainGroup.getId());
//        }else {
//            params.put("maleGroupId", mainGroup.getId());
//        }
        params.put("selectBoth", true);
        params.put("groupAId", mainGroup.getId());
        params.put("groupBId", mainGroup.getId());
        params.put("type", ActivityTypeAndStatus.BEGIN);
        // 这里不再根据superlike与否来查询.对于正常匹配成功的队伍也应该排除.
        // 避免当前队伍修改配置后vote success表被清掉,导致已经匹配成功的约会队伍又重新推送给当前队伍.
//        params.put("superLike", 1);

        List<Activity> activityList = activityService.getActivityList(params);

        List<Long> superLikeGroupList = new ArrayList<>();
        for (Activity a : activityList){
            logger.info("excludeSuperLikeGroup activity id is " + a.getId());
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
                    logger.info("excludeSuperLikeGroup find group [" + l + "] to be deleted.");
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

    protected boolean checkRuleIfOpen(int i){
        String open = redisUtil.lIndex(RedisKeyAndLock.BASE_MATCH_OPEN, Long.valueOf(i));
        return (null != open && "1".equals(open)) ? true : false;
    }

}
