package com.geetion.puputuan.web.api.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.common.utils.UuidUtils;
import com.geetion.puputuan.engine.thread.MatchThread;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.model.jsonModel.ImUser;
import com.geetion.puputuan.model.jsonModel.JSONUser;
import com.geetion.puputuan.model.jsonModel.JSONUserBase;
import com.geetion.puputuan.pojo.GroupMemberVote;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.supervene.recommend.service.SuperveneRecommendService;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.app.GroupController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class GroupControllerImpl extends BaseController implements GroupController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;
    @Resource
    private UserService userService;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMemberHistoryService groupMemberHistoryService;
    @Resource
    private MessageService messageService;
    @Resource
    private LocationService locationService;
    @Resource
    private BarService barService;
    @Resource
    private VoteService voteService;
    @Resource
    private RecommendService recommendService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityMemberService activityMemberService;
    @Resource
    private RecommendSuccessService recommendSuccessService;
    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private CommonService commonService;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private ConsultationService consultationService;
    @Resource
    private SignService signService;
    @Resource
    private UserRecommendService userRecommendService;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private UserSuperLikeService userSuperLikeService;
    @Resource
    private UserLikeGroupService userLikeGroupService;
    @Resource
    private UserSuperLikeConfigService userSuperLikeConfigService;
    @Resource
    private RunMatchUtil runMatchUtil;
    @Resource
    private UserBlackListService userBlackListService;

    private final Logger logger = Logger.getLogger(GroupControllerImpl.class);

    // 返回用户匹配队伍数
    private final static String NUM_LOCAL_GROUP = "NUM_LOCAL_GROUP";
    // 返回super like的次数
    private final static String NUM_SUPER_LIKE = "NUM_SUPER_LIKE";

    /**
     * 实例化推荐服务
     */
    private SuperveneRecommendService superveneRecommendService = new SuperveneRecommendService();


    /**
     * 群聊人数上限
     */
    private static int MAX_MEMBERS = 4;
    /**
     * 群聊人数下限
     */
    private final static int MIN_MEMBERS = 2;

    @Override
    public Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object, String friendSex,
                         Boolean orderByFirstLetter) {

        if (checkParaNULL(methodType, orderByFirstLetter)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            //获得当前登录用户的详细信息
            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
            //获取同性的队友
            param.putAll(pojoToMap(object));
            param.put("userId", shiroService.getLoginUserBase().getId());
            if (friendSex == null) {
                param.put("sex", user.getSex());
            } else if (!"A".equals(friendSex) && ("F".equals(friendSex) || "M".equals(friendSex))) {
                param.put("sex", friendSex);
            }

            List<Integer> notInStatus = new ArrayList<>();
            //查询不在创建中状态的群的用户
            notInStatus.add(GroupTypeAndStatus.GROUP_CREATE);
            //查询不在匹配中状态的群的用户
            notInStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
            //查询不在组队中状态的群的用户
            notInStatus.add(GroupTypeAndStatus.GROUP_TEAM);
            List<Integer> notInMemberStatus = new ArrayList<>();
            //查询不是加入状态群的用户
            notInMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);
            notInMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
            notInMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);
            param.put("notInStatus", notInStatus);
            param.put("notInMemberStatus", notInMemberStatus);

            switch (methodType) {
                case 1:
                    List<User> list;
                    if (orderByFirstLetter) {
                        //根据首字母分页
                        list = friendRelationshipService.getUserOrderByFirstPinyin(param);
                    } else {
                        //根据添加时间分页
                        list = friendRelationshipService.getFriendRelationshipWithUser(param);
                    }
                    //获取头像
                    ossFileUtils.getUserHeadList(list, null);
                    resultMap.put("list", ConvertBeanUtils.convertDBListToJSONList(list));
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        User obj = userService.getUserById(id);
                        //获取头像
                        ossFileUtils.getUserHead(obj, null);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<User> pagingForKeyword = null;
                    if (orderByFirstLetter) {
                        //根据首字母分页
                        pagingForKeyword = friendRelationshipService.getWithUserOrderByFirstPinyinPage(pageEntity);
                    } else {
                        //根据添加时间分页
                        pagingForKeyword = friendRelationshipService.getFriendRelationshipWithUserPage(pageEntity);
                    }
                    if (null != pagingForKeyword) {
                        //获取头像
                        ossFileUtils.getUserHeadPage(pagingForKeyword, null);
                        resultMap.put("list", ConvertBeanUtils.convertDBListToJSONList(pagingForKeyword.getResultList()));
                        resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                        resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                        resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    }
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object searchGroupByRecommend(Long recommendId) {
        logger.info("GroupControllerImpl searchGroupByRecommend begin...");
        if (checkParaNULL(recommendId)) {
            Map<String, Object> resultMap = new HashMap<>();
            //查询推荐的队伍
            Recommend recommend = recommendService.getRecommendById(recommendId);
            if (recommend != null) {
                Group group = groupService.getGroupById(recommend.getMatchGroupId());
                if (group != null) {
                    //查询团队成员
                    List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
                    //获取头像
                    ossFileUtils.getUserHeadGroupMemberList(groupMemberList, null);
                    resultMap.put("group", group);
                    resultMap.put("groupMemberList", groupMemberList);
                    logger.info("GroupControllerImpl searchGroupByRecommend return groupMemberList size " + groupMemberList.size());
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
                }
            }

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchRunningGroup() {
        logger.info("GroupControllerImpl searchRunningGroup ");
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("haveRunningGroup", false);
        resultMap.put("group", null);
        resultMap.put("groupMemberList", null);
        resultMap.put("isLeader", false);

        //如果用户已经在正在进行的队中，则list不为空
        Group group = commonService.getRunningGroup();

        if (group != null) {
            //查询团队成员
            List<GroupMember> groupMemberList = commonService.getInviteAndInGroupMember(group.getId());

            //合法性判断,由于创建队伍的时候不再添加同步锁,通过MySQL的唯一索引实现类似锁的效果.
            //而且MySQL不支持事务嵌套,因此如果在创建过程中后台服务异常重启,可能导致异常的队伍数据存在.
            //这里需要判断队伍数据是否异常,异常则需要清理这部分数据.可以通过是否有roomID来判断.
            if (group.getRoomId() == null || group.getRoomId().equalsIgnoreCase("") || groupMemberList.size() == 0) {
                rollbackGroup(group.getId(), groupMemberList.toArray(new GroupMember[groupMemberList.size()]), false);
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
            }

            //获取头像
            ossFileUtils.getUserHeadGroupMemberList(groupMemberList, null);
            resultMap.put("haveRunningGroup", true);
            resultMap.put("group", ConvertBeanUtils.convertToJSONGroup(group));
            resultMap.put("groupMemberList", ConvertBeanUtils.converToJSONGroupMember(groupMemberList));
            boolean isLeader = false;
            for (GroupMember gm : groupMemberList) {
                if (gm.getUserId().equals(shiroService.getLoginUserBase().getId()) && gm.getType().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER)) {
                    isLeader = true;
                }
            }
            resultMap.put("isLeader", isLeader);
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object createGroup(String[] identifys, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Long barId, Integer matchGroupType, Long[] disLikeGroups, Long likeGroupId) {
        logger.info("GroupControllerImpl createGroup ");
        //要邀请的用户identify可能为null
        if (checkParaNULL(provinceId, province, cityId, city, areaId, area)) {
            /** 开启事务 */
//            TransactionStatus transactionStatus = transactionHelper.start();
            Map<String, Object> resultMap = new HashMap<>();
            List<GroupMember> groupMemberList = new ArrayList<>();
            List<User> userList = new ArrayList<>();
            List<User> toAdd = new ArrayList<>();
            Group group = new Group();
            String sysTime = runMatchUtil.getSysTime();

            boolean needRollbackUserLikeGroup = false;
            try {
                //获得当前用户信息
                User leader = userService.getByUserId(shiroService.getLoginUserBase().getId());

                if (userBlackListService.checkIfInBL(leader.getUserId())) {
                    return sendResult(ResultCode.CODE_922.code, ResultCode.CODE_922.msg, null);
                }

                toAdd.add(leader);

                //如果有队员,判断队员数量是否有效,有效则添加
                if (identifys != null && identifys.length >= 1 && identifys.length < MAX_MEMBERS) {
                    for (int i = 0; i < identifys.length; i++) {
                        //查询用户
                        User player = userService.getUserByIdentifyId(identifys[i]);
                        if (player == null) {
                            return sendResult(ResultCode.CODE_720.code, ResultCode.CODE_720.msg, null);
                        }

                        userList.add(player);
                        toAdd.add(player);
                    }
                }

                //step 2:创建队友群
                group.setToken(UuidUtils.generateUuid());
                group.setName(leader.getNickName() + "的队伍");
                group.setType(this.getGroupSexType(null, toAdd, null));
                group.setStatus(GroupTypeAndStatus.GROUP_MATCHING);
                group.setProvinceId(Integer.valueOf(provinceId.toString()));
                group.setProvince(province);

                group.setCityId(Integer.valueOf(cityId.toString()));
                group.setCity(city);

                group.setAreaId(Integer.valueOf(areaId.toString()));
                group.setArea(area);

                group.setBarId(barId);
                group.setRunTime(sysTime);
                group.setRecommendSex(matchGroupType);

                if (groupService.addGroup(group)) {
                    //添加队长进群
                    GroupMember leadMember = addGroup(group.getId(), leader.getUserId(),
                            GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);

                    //队长在其他队伍中,组队失败
                    if (leadMember == null) {
                        rollbackGroup(group.getId(), null, false);
                        logger.info("GroupControllerImpl createGroup leader is in other team.");
                        return sendResult(ResultCode.CODE_900.code, ResultCode.CODE_900.msg, null);
                    }

                    leadMember.setUser(leader);
                    groupMemberList.add(leadMember);

                    //要加入的群聊的成员
                    List<String> memberList = new ArrayList<>();
                    StringBuffer sb = new StringBuffer();
                    List<JSONUserBase> inOtherGroupMemberList = new ArrayList<>();
                    List<User> inGroupList = new ArrayList<>();
                    List<User> inBlackList = new ArrayList<>();
                    String roomId = "";

                    for (User user : userList) {

                        if (userBlackListService.checkIfInBL(user.getUserId())) {
                            inBlackList.add(user);
                            continue;
                        }

                        GroupMember member = addGroup(group.getId(), user.getUserId(),
                                GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);

                        if (member == null) {
                            ossFileUtils.getUserHead(user, null);
                            inOtherGroupMemberList.add(ConvertBeanUtils.convertUserToJSONUser(user));
                            continue;
                        }

                        member.setUser(user);

                        //获得每一个用户的账户作为环信的用户名
//                        members[index++] = user.getUserBase().getAccount() + "";
                        memberList.add(user.getUserBase().getAccount() + "");
                        sb.append(EmojiCharacterUtil.emojiRecovery2(user.getNickName()) + "、");
                        groupMemberList.add(member);
                        inGroupList.add(user);
                    }

                    //所有队员都在其他队伍中,组队失败,需要删除队长及队伍数据
//                    if (memberList.isEmpty() && identifys.length > 0) {
//                        rollbackGroup(group.getId(), groupMemberList.toArray(new GroupMember[groupMemberList.size()]), false);
//                        logger.info("GroupControllerImpl createGroup memberList is empty.");
//                        if(inOtherGroupMemberList.size() > 0){
//                            return sendResult(ResultCode.CODE_901.code, ResultCode.CODE_901.msg, null);
//                        }
//
//                        if(inBlackList.size() > 0){
//                            return sendResult(ResultCode.CODE_923.code, ResultCode.CODE_923.msg, null);
//                        }
//
//                    } else {
                    //创建环信聊天群，如果成功则获得创建的环信群聊id
                    roomId = HuanXinChatGroupService.createChatGroup(EmojiCharacterUtil.emojiRecovery2(leader.getNickName()) + "的队伍", "队伍群聊", false,
                            new Long(MAX_MEMBERS), false, leader.getUserBase().getAccount() + "", memberList.toArray(new String[memberList.size()]));  // TODO modified by simon at 2016/09/26  -- 添加members
                    group.setRoomId(roomId);
                    //保存环信的聊天id进数据库
                    groupService.updateGroup(group);

                    if (addUserLikeGroup(disLikeGroups, likeGroupId, leader, group)) {
                        needRollbackUserLikeGroup = true;
                    } else {
                        rollbackGroup(group.getId(), groupMemberList.toArray(new GroupMember[groupMemberList.size()]), false);
                        logger.error("GroupControllerImpl createGroup addUserLikeGroup error.");
                        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                    }
//                    }

                    //如果只有队长一个人则不推送消息.等队长添加其他成员时再推送群聊消息.
                    if (inGroupList.size() != 0) {
                        Bar bar = barService.getBarById(barId);
                        new SendGroupCreateMsgThread(group, leader, bar, inGroupList, inBlackList).run();
                    }

                    resultMap.put("isLeader", true);
                    resultMap.put("haveRunningGroup", true);
                    resultMap.put("group", ConvertBeanUtils.convertToJSONGroup(group));
                    resultMap.put("groupMemberList", ConvertBeanUtils.converToJSONGroupMember(groupMemberList));
                    resultMap.put("inOtherGroupMemberList", inOtherGroupMemberList);
                } else {
                    // 队伍添加失败
                    logger.error("GroupControllerImpl createGroup addGroup error.");
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }

            } catch (HuanXinChatGroupException e) {
                logger.error("GroupControllerImpl createGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
//                transactionHelper.rollback(transactionStatus);
                rollbackGroup(group.getId(), groupMemberList.toArray(new GroupMember[groupMemberList.size()]), needRollbackUserLikeGroup);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("GroupControllerImpl createGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
//                transactionHelper.rollback(transactionStatus);
                rollbackGroup(group.getId(), groupMemberList.toArray(new GroupMember[groupMemberList.size()]), needRollbackUserLikeGroup);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }


            // 调用工具类，将队伍相关信息加入redis缓存中
            runMatchUtil.addToRedis(group, false, null, null);

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    private boolean rollbackGroup(Long groupId, GroupMember[] members, boolean isRollbackUserLikeGroup) {
        int count = members.length;

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();

        try {
            if (count != 0) {
                //先删除成员表数据
                for (int i = 0; i < count; i++) {
                    GroupMember member = members[i];
                    if (member != null) {
                        groupMemberService.removeGroupMember(member.getId());
                    }
                }
            }

            //再删除群聊数据
            groupService.removeGroup(groupId);

            if (isRollbackUserLikeGroup) {
                userLikeGroupService.setUserLikeGroupToInvalid(groupId);
            }

            transactionHelper.commit(transactionStatus);
        } catch (Exception ex) {
            logger.error("GroupControllerImpl rollbackGroup error " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
            return false;
        }

        return true;
    }

    /**
     * 添加用户喜欢/不喜欢队伍记录
     *
     * @param disLikeGroups
     * @param likeGroups
     * @param group
     * @param group
     */
    private boolean addUserLikeGroup(Long[] disLikeGroups, Long likeGroups, User user, Group group) {
        logger.info("GroupControllerImpl addUserLikeGroup ");
        List<UserLikeGroup> list = new ArrayList<>();

        if (checkParaNULL(disLikeGroups) && null != disLikeGroups) {
            for (int i = 0; i < disLikeGroups.length; i++) {
                UserLikeGroup t = new UserLikeGroup();
                t.setUserId(user.getUserId());
                t.setGroupId(group.getId());
                t.setLikeGroupId(disLikeGroups[i]);
                t.setIsLike(UserLikeGroupType.Dislike);
                list.add(t);
            }
        }

        if (checkParaNULL(likeGroups) && null != group) {
            UserLikeGroup t = new UserLikeGroup();
            t.setUserId(user.getUserId());
            t.setGroupId(group.getId());
            t.setLikeGroupId(likeGroups);
            t.setIsLike(UserLikeGroupType.Like);
            list.add(t);
        }

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();

        try {
            if (checkParaNULL(list) && list.size() > 0) {
                userLikeGroupService.addUserLikeGroupLike(list);
            }
            transactionHelper.commit(transactionStatus);
        } catch (Exception ex) {
            logger.error("GroupControllerImpl addUserLikeGroup error " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
            return false;
        }

        return true;
    }

    @Override
    public Object updateGroup(String groupName, Long barId, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Integer matchGroupType) {
        logger.info("GroupControllerImpl updateGroup " + runMatchUtil.getSysTime());

        String currentTime = runMatchUtil.getSysTime();
        boolean ifRest = true;
        //查询用户当前进行中的群聊
        Group group = commonService.getRunningGroup();
        if (group != null) {

            if (null == barId && null == provinceId && null == province && null == cityId && null == city && null == areaId && null == area) {
                ifRest = false;
            } else if (group.getBarId().equals(barId) && group.getProvinceId().equals(provinceId)
                    && group.getCityId().equals(cityId) && group.getAreaId().equals(areaId)) {
                // 队伍基本信息未变，不需要重新跑匹配
                ifRest = false;
            }

            // 兼容旧版本，未传provinceId、province参数，判断省跟市是否一致
            if (null == provinceId && null != cityId) {
                String prexProvince = String.valueOf(group.getProvinceId()).substring(0, 3);
                String prexCity = String.valueOf(cityId).substring(0, 3);
                if (!prexProvince.equals(prexCity)) {
                    return sendResult(ResultCode.CODE_921.code, ResultCode.CODE_921.msg, null);
                }
            }


            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                if (null != groupName && !groupName.equals("")) {
                    group.setName(groupName);
                }

                boolean barIsChanged = false;
                if (null != barId && group.getBarId() != barId) {
                    group.setBarId(barId);
                    barIsChanged = true;
                }

                if (null != provinceId) {
                    group.setProvinceId(provinceId.intValue());
                }

                if (null != province && !province.equals("")) {
                    group.setProvince(province);
                }

                if (null != cityId) {
                    group.setCityId(cityId.intValue());
                }

                if (null != city && !city.equals("")) {
                    group.setCity(city);
                }

                if (null != areaId) {
                    group.setAreaId(areaId.intValue());
                }

                if (null != area && !area.equals("")) {
                    group.setArea(area);
                }
                // 当修改查询队伍类型时，不需要更新这两个时间
                if (ifRest) {
                    // 将修改时间改为当前
                    group.setModifyTime(new Date());
                    group.setRunTime(currentTime);
                }
                group.setRecommendSex(matchGroupType);
                if (groupService.updateGroup(group)) {

                    List<GroupMember> members = commonService.getGroupMember(group.getId());

                    if (ifRest) {
                        //清除队伍所有推荐信息,准备重新开始匹配流程
                        clearGroupRecommendData(group.getId());

                        if (members != null & members.size() > 1) {
                            Bar bar = barService.getBarById(barId);
                            String newArea = group.getArea();
                            if (group.getAreaId().equals(0L)) {
                                newArea = group.getCity();
                            }
                            //发队员推送消息
                            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                                    "约会更改：" + bar.getName() + "(" + newArea + ")\n", HuanXinSendMessageType.UPDATE, null, group.getRoomId());
                        }

//                        //丛林Mad House活动需要添加新消息指导用户找到购票入口
//                        if (true == barIsChanged && bar.getActionType() == BarType.ACTION_TYPE_YOUZAN_IN_GROUP) {
//                            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
//                                    "您可以在下方工具栏点击+号进入商城购买 MAD HOUSE 活动门票\n", HuanXinSendMessageType.ADD_OR_QUIT,
//                                    null, group.getRoomId());
//                        }

                        // 将队伍修改时间放入缓存中
                        runMatchUtil.addToRedis(group, true, null, null);
                    } else {
                        if (members != null & members.size() > 1) {
                            String sex = "";

                            switch (matchGroupType) {
                                case 0:
                                    sex = "男";
                                    break;
                                case 1:
                                    sex = "女";
                                    break;
                                case 2:
                                    sex = "全部";
                                    break;
                                case 3:
                                    sex = "混合";
                                    break;
                            }

                            //发队员推送消息
                            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                                    "推荐约会队伍性别改为：" + sex + "\n", HuanXinSendMessageType.UPDATE, null, group.getRoomId());
                        }
                    }

                    /** 提交事务 */
                    transactionHelper.commit(transactionStatus);

                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_919.msg, null);
                } else {
                    logger.info("GroupControllerImpl updateGroup update error");
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }
            } catch (HuanXinMessageException e) {
                logger.error("GroupControllerImpl updateGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
            } catch (Exception e) {
                logger.error("GroupControllerImpl updateGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
            }
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }


    @Deprecated
    public Object updateGroup(String groupName, String address, String province, String city, String area,
                              String locationName, Double longitude, Double latitude, Long barId) {

        //查询用户当前进行中的群聊
        Group group = commonService.getRunningGroup();
        if (group != null) {
            try {
                Map<String, Object> param = new HashMap<>();

                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                String updateString = user.getNickName() + " 修改 ";
                String finalPlace = "";

                //如果正在匹配中，则无法修改信息
                if (group.getStatus().longValue() == GroupTypeAndStatus.GROUP_MATCHING) {
//                    return sendResult(ResultCode.CODE_909.code, ResultCode.CODE_909.msg, null);
                }
                group.setName(groupName);
                Bar bar = barService.getBarById(barId);
                //barId是在字典中，则可以更新
                if (user.getSex().equals("M") && bar != null) {
                    group.setBarId(barId);
                }
                //上传省的名字，保存在群组中，为后台统计用
                if (checkParaNULL(province)) {
                    param.put("name", province);
                    List<District> districtList = districtService.getProvinceByParam(param);
                    //如果找得到该区域，则更新
                    if (districtList != null && districtList.size() != 0) {
                        group.setProvinceId(districtList.get(0).getCode());
                    }
                }
                //女性群，则显性更新 city 或 area 并提示前端，男性则隐姓更新，因为一定会有Address上传，最后提示的是更新Address
                if (checkParaNULL(city)) {
                    //如果地区不为空，则说明有选择地区，否则选择整个城市
                    param.put("name", city);
                    List<District> districtList = districtService.getCityByParam(param);
                    //如果找得到该区域，则更新
                    if (districtList != null && districtList.size() != 0) {
                        group.setCityId(districtList.get(0).getCode());
                        finalPlace = "地点为 " + city;
                    }
                }
                //更新area
                if (checkParaNULL(area)) {
                    param.clear();
                    param.put("name", area);
                    List<District> districtList = districtService.getAreaByParam(param);
                    //如果找得到该区域，则更新
                    if (districtList != null && districtList.size() != 0) {
                        group.setAreaId(districtList.get(0).getCode());
                        finalPlace = "地点为 " + area;
                    }
                }
                Location location = null;
                if (user.getSex().equals("M") && checkParaNULL(locationName, longitude, latitude, address)) {
                    location = new Location();
                    location.setName(locationName);
                    location.setLongitude(longitude);
                    location.setLatitude(latitude);
                    location.setAddress(address);
                    //添加地点信息
                    locationService.addLocation(location);
                    //如果是男性群，则更新location
                    if (location.getId() != null) {
                        group.setLocationId(location.getId());
                    }
                    finalPlace = "地点为 " + address;
                }
                updateString += finalPlace;
                if (groupService.updateGroup(group)) {
                    //如果更新了地点，并且用户有在进行的约会则删掉签到的地点
                    if (location != null && location.getId() != null) {
                        Activity activity = commonService.getRunningActivity();
                        if (activity != null) {
                            param.clear();
                            param.put("activityId", activity.getId());
                            //删除所有签到地点
                            signService.removeSignByParam(param);
                        }
                    }
                    //发送 环信 消息 -- 修改群资料
//                    sendHuanXinMessage(HuanXinConstant.MESSAGE_CHATGROUPS, group.getRoomId(), updateString,
//                            HuanXinSendMessageType.UPDATE, null, null, null, null, null, group.getRoomId());

                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                            updateString + "\n", HuanXinSendMessageType.UPDATE, null, group.getRoomId());

                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

            } catch (HuanXinMessageException e) {
                e.printStackTrace();
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                e.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }

    @Override
    public Object invite(String[] identifys) {
        logger.info("GroupControllerImpl invite begin ...");
        if (checkParaNULL(identifys) && identifys.length > 0) {
            String currentTime = runMatchUtil.getSysTime();
            //查询用户当前进行中的群聊
            Group group = commonService.getRunningGroup();
            if (group == null) {
                return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
            }

            TransactionStatus transactionStatus = null;
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            List<JSONUser> inOtherGroupMemberList = new ArrayList<>();
            try {

                //获得创建者信息（队长）
                User leader = userService.getByUserId(shiroService.getLoginUserBase().getId());
                //获得群成员
                List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
                StringBuffer sb = new StringBuffer();
                //获取点击邀请的人的身份（理论上应该是队长）
                param.put("userId", shiroService.getLoginUserBase().getId());
                param.put("groupId", group.getId());
                GroupMember groupMember = groupMemberService.getGroupMember(param);

                //step 1.5:如果当前用户不是队长，则无邀请权限
                if (groupMember == null || !(groupMember.getType() == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER)) {
                    return sendResult(ResultCode.CODE_903.code, ResultCode.CODE_903.msg, resultMap);
                }

                //当前已加入群人数跟待邀请的人数大于4个，则人数超出限制
                if (groupMemberList != null && groupMemberList.size() != 0) {
                    if (identifys.length + groupMemberList.size() > MAX_MEMBERS) {
                        return sendResult(ResultCode.CODE_902.code, ResultCode.CODE_902.msg, resultMap);
                    }
                }

                Long[] userIds = new Long[identifys.length];
                List<User> toAdd = new ArrayList<>();
                for (int i = 0; i < identifys.length; i++) {
                    User user = userService.getUserByIdentifyId(identifys[i]);
                    if (null == user) {
                        //邀请用户不存在
                        return sendResult(ResultCode.CODE_720.code, ResultCode.CODE_720.msg, null);
                    }

                    if (userBlackListService.checkIfInBL(user.getUserId())) {
                        //邀请用户在黑名单
                        return sendResult(ResultCode.CODE_923.code, ResultCode.CODE_923.msg, null);
                    }

                    // 判断当前用户是否在其他队伍中
                    boolean isInOtherGroup = groupMemberService.ifInOtherGroup(user.getUserId());
                    if (isInOtherGroup) {
                        ossFileUtils.getUserHead(user, null);
                        inOtherGroupMemberList.add(ConvertBeanUtils.convertUserToJSONUser(user));
                    } else {
                        sb.append(user.getNickName() + "、");
                        userIds[i] = user.getUserId();
                        toAdd.add(user);
                    }

                }

                int oldMemberCount = groupMemberList.size();
                String[] memberAccount = null;

                if (userIds.length > 0 && userIds[0] != null) {
                    //要加入群聊的成员的账户
                    memberAccount = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, userIds);

                    for (Long userId : userIds) {
                        //添加队友进群
                        this.addGroup(group.getId(), userId,
                                GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
                    }
                }

                /** 开启事务 */
                //添加成员addGroup有单独事务处理,其余前边的内容只是查询,不需要另外加事务.
                transactionStatus = transactionHelper.start();

                // 队伍成员发现变化时，更新队伍修改时间为当前时间
                group.setRunTime(currentTime);
                group.setModifyTime(new Date());
                group.setType(this.getGroupSexType(groupMemberList, toAdd, null));
                groupService.updateGroup(group);

                //清除队伍所有推荐信息,准备重新开始匹配流程
                clearGroupRecommendData(group.getId());

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);

                HuanXinChatGroupService.addBatchUsersToChatGroup(group.getRoomId() + "", memberAccount);

                if (oldMemberCount > 1) {
                    if (null != memberAccount) {
                        //发送 环信 消息 -- 邀请入群
                        HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                        huanXinMessageExtras.setAccount(leader.getUserBase().getAccount());
                        huanXinMessageExtras.setHeadId(leader.getHeadId());
                        huanXinMessageExtras.setUserId(leader.getUserId());
                        huanXinMessageExtras.setNickName(leader.getNickName());
                        huanXinMessageExtras.setGroupId(group.getId());
                        huanXinMessageExtras.setRoomId(group.getRoomId());
                        HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                                leader.getNickName() + " 邀请您加入队伍", HuanXinSendMessageType.INVITE,
                                huanXinMessageExtras, memberAccount);
                    }

                    new SendGroupMemberAddMsgThread(group, leader, sb).run();
                } else {
                    Bar bar = barService.getBarById(group.getBarId());
                    new SendGroupCreateMsgThread(group, leader, bar, toAdd, new ArrayList<>()).run();
                }
//                // 给群成员发信息
//                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
//                        leader.getNickName() + " 邀请 " + sb.substring(0, sb.length() - 1)  + " 加入队伍\n", HuanXinSendMessageType.ADD_OR_QUIT,
//                        null, group.getRoomId());

                //TODO 判断队伍是否在匹配状态下，如果是，需要重新跑匹配引擎
                if (group.getStatus().equals(GroupTypeAndStatus.GROUP_MATCHING)) {
//                    this.runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), true);

                    runMatchUtil.addToRedis(group, true, Arrays.asList(userIds), null);
                }

            } catch (HuanXinMessageException e) {
                logger.error("GroupControllerImpl invite error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                logger.error("GroupControllerImpl invite error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            resultMap.put("inOtherGroupMemberList", inOtherGroupMemberList);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    public Object invite(Long[] userIds) {
        if (checkParaNULL(userIds) && userIds.length > 0) {

            //查询用户当前进行中的群聊
            Group group = commonService.getRunningGroup();
            if (group == null) {
                return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
            }

            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            try {

                //step 1.1:获得创建者信息（队长）
                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                //step 1.2:获得群信息

                //step 1.3:获得群成员
                List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());

                //step 1.4:获取点击邀请的人的身份（理论上应该是队长）
                param.put("userId", user.getUserId());
                GroupMember groupMember = groupMemberService.getGroupMember(param);
                //step 1.5:如果当前用户不是队长，则无邀请权限
                if (groupMember == null || !(groupMember.getType() == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER)) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_903.code, ResultCode.CODE_903.msg, resultMap);
                }

                //step 1.6:当前已加入群人数跟待邀请的人数大于5个，则人数超出限制
                if (groupMemberList != null && groupMemberList.size() != 0) {
                    if (userIds.length + groupMemberList.size() > MAX_MEMBERS) {
                        /** 回滚事务 */
                        transactionHelper.rollback(transactionStatus);
                        return sendResult(ResultCode.CODE_902.code, ResultCode.CODE_902.msg, resultMap);
                    }
                }

//                for (Long userId : userIds) {
//                    //step 2.1:查询是否有用户已经在其他群中了
//                    List<User> userList = commonService.userHaveInRunningGroup(userId);
//                    //如果用户已经有在其他队中，则返回提示
//                    if (userList != null && userList.size() != 0) {
//                        resultMap.put("list", userList);
//                        /** 回滚事务 */
//                        transactionHelper.rollback(transactionStatus);
//                        return sendResult(ResultCode.CODE_901.code, ResultCode.CODE_901.msg, resultMap);
//                    }
//                }

                //要加入群聊的成员的token
//                String[] memberTokens = commonService.getUserAccountOrToken(CommonService.USER_TOKEN, userIds);

                //要加入群聊的成员的账户
                String[] memberAccount = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, userIds);

                for (Long userId : userIds) {
                    //step 3.1:添加队友进群
                    addGroupMember(group.getId(), userId,
                            GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);
                    //step 3.2:添加队友进群历史记录表
                    addGroupMemberHistory(group.getId(), userId,
                            GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);
                }
                //step 5:发送JPush消息
//                JPushUtils.sendGroupJPush("邀请", user.getNickName() + " 邀请您加入群", JPushType.INVITE_GROUP, group.getId(), null, memberTokens);
                //发送 环信 消息 -- 邀请入群
//                boolean result = sendHuanXinMessage(HuanXinConstant.MESSAGE_USERS, user.getUserBase().getAccount(),
//                        user.getNickName() + " 邀请您加入群，地址：" + group.getLocation().getAddress(), HuanXinSendMessageType.INVITE,
//                        user.getUserBase().getAccount(),
//                        user.getHeadId(), user.getUserId(), user.getNickName(), group.getId(), memberAccount);

                HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                huanXinMessageExtras.setAccount(user.getUserBase().getAccount());
                huanXinMessageExtras.setHeadId(user.getHeadId());
                huanXinMessageExtras.setUserId(user.getUserId());
                huanXinMessageExtras.setNickName(user.getNickName());
                huanXinMessageExtras.setGroupId(group.getId());
//                huanXinMessageExtras.setLocation(group.getLocation() == null ? null : group.getLocation().getAddress());

                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        user.getNickName() + " 邀请您加入群", HuanXinSendMessageType.INVITE,
                        huanXinMessageExtras, memberAccount);

//                System.out.println("\n\n\n发送 环信 消息 result " + result);

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
            } catch (HuanXinMessageException e) {
                e.printStackTrace();
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                e.printStackTrace();
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    /**
     * 清除队伍所有推荐信息,包括user recommend/recommend/recommend success
     * 这里不添加事务管理,通过上层方法的事务进行管理
     *
     * @param groupId
     * @return
     */
    private void clearGroupRecommendData(Long groupId) {

        //清除成员的userRecommend数据
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        userRecommendService.removeUserRecommendByGroupId(param);

        //清除队伍的recommend success数据
        recommendSuccessService.removeRecommendSuccessByMainGroup(groupId);

        //清除队伍的recommend数据
        recommendService.removeMainIdRecommend(groupId);
    }

    @Override
    public Object groupMember() {
        //查询用户当前进行中的群聊
        Group group = commonService.getRunningGroup();
        if (group != null) {
            Map<String, Object> resultMap = new HashMap<>();
            //获得群成员
            List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
            //获取头像
            ossFileUtils.getUserHeadGroupMemberList(groupMemberList, null);
            resultMap.put("list", groupMemberList);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }

        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }

    @Override
    public Object searchInvite(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> resultMap = new HashMap<>();
            switch (methodType) {
                case 1:
                    List<Group> list = getInvitingGroupList();
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Group obj = groupService.getGroupById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
//                    if (pageEntity != null)
//                        pageEntity = new PageEntity();
                    PagingResult<Group> pagingForKeyword = getInvitingGroupPage(pageEntity);
                    if (null != pagingForKeyword) {
                        resultMap.put("list", pagingForKeyword.getResultList());
                        resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                        resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                        resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    }
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object agreeGroup(Long groupId) {
        logger.info("GroupControllerImpl agreeGroup begin ...");
        if (checkParaNULL(groupId)) {

            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            Group myGroup = commonService.getRunningGroup();
            if (myGroup != null) {
                return sendResult(ResultCode.CODE_900.code, ResultCode.CODE_900.msg, null);
            }

            //查询群聊
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                //只有创建中的队伍用户才可以加入
                if (group.getStatus() != GroupTypeAndStatus.GROUP_CREATE) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_906.code, ResultCode.CODE_906.msg, resultMap);
                }
                //获得群成员
                List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
                //人数超过5个，则无法再加入
                if (groupMemberList.size() > MAX_MEMBERS) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_902.code, ResultCode.CODE_902.msg, resultMap);
                }
                //查询是否用户已经在其他群中了
                List<User> userList = commonService.userHaveInRunningGroup(user.getUserId());
                if (userList != null && userList.size() != 0) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_900.code, ResultCode.CODE_900.msg, resultMap);
                }

                param.put("groupId", groupId);
                param.put("userId", user.getUserId());
                GroupMember groupMember = groupMemberService.getGroupMember(param);

                if (groupMember == null) {
                    //如果数据库没有该项记录，说明用户没有被邀请过
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_905.code, ResultCode.CODE_905.msg, null);
                } else if (groupMember.getStatus().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_REMOVE)) {
                    // 用户已经被队长踢出
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_918.code, ResultCode.CODE_918.msg, null);
                } else {
                    //更新队友的信息为加入
                    groupMember.setStatus(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
                    //更新时间，表示进群时间
                    //groupMember.setCreateTime(new Date());
                    groupMemberService.updateGroupMember(groupMember);
                }
                //添加队友进群历史记录表
                addGroupMemberHistory(group.getId(), user.getUserId(),
                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);


                /** 提交事务 */
                transactionHelper.commit(transactionStatus);

                HuanXinChatGroupService.addSingleUserToChatGroup(group.getRoomId() + "", user.getUserBase().getAccount());
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        user.getNickName() + " 加入群\n", HuanXinSendMessageType.ADD_OR_QUIT,
                        null, group.getRoomId());

                resultMap.put("isLeader", groupMember.getType() == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER ? true : false);
                resultMap.put("haveRunningGroup", true);
                resultMap.put("group", ConvertBeanUtils.convertToJSONGroup(group));
                //将新添加的队员添加到list表，并将目前队伍的成员返回
                groupMemberList.add(groupMember);
                resultMap.put("groupMemberList", ConvertBeanUtils.converToJSONGroupMember(groupMemberList));

            } catch (HuanXinChatGroupException e) {
                logger.error("GroupControllerImpl agreeGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (HuanXinMessageException e) {
                logger.error("GroupControllerImpl agreeGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                logger.error("GroupControllerImpl agreeGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object removeGroupMember(String[] identifys) {
        logger.info("GroupControllerImpl removeGroupMember begin ...");
        String currentTime = runMatchUtil.getSysTime();
        // 判断参数是否为空
        if (identifys == null || identifys.length == 0) {
            return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
        }

        Group group = commonService.getRunningGroup();
        // 群聊不存在或结束，直接返回
        if (group == null) {
            return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
        }
        //获取队伍成员
        List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
        // 查询群聊队长
        User leader = commonService.getGroupLeader(group.getId());
        // 当前用户不是队长，不能移除队员
        if (!leader.getUserId().equals(shiroService.getLoginUserBase().getId())) {
            return sendResult(ResultCode.CODE_903.code, ResultCode.CODE_903.msg, null);
        }

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();
        Map<String, Object> param = new HashMap<>();

        try {
            StringBuffer nickNameSb = new StringBuffer();
            List<User> waitToRemove = new ArrayList<>();
            List<Long> waitToRemoveId = new ArrayList<>();

            // 将队员移除队伍
            for (String s : identifys) {
                User user = userService.getUserByIdentifyId(s);
                waitToRemove.add(user);
                waitToRemoveId.add(user.getUserId());
                param.put("userId", user.getUserId());
                param.put("groupId", group.getId());

                //查询要移除的群成员，若该成员不存在，则提示成员不存在
                GroupMember groupMember = groupMemberService.getGroupMember(param);
                //队员不存在，直接返回
                if (groupMember == null) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_910.code, ResultCode.CODE_910.msg, null);
                }

                //直接从表中删除成员
//                groupMember.setStatus(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_REMOVE);
//                groupMemberService.updateGroupMember(groupMember);
                groupMemberService.removeGroupMember(groupMember.getId());
                //添加成员被移除记录进群历史记录表
                addGroupMemberHistory(group.getId(), user.getUserId(),
                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_REMOVE);
                // 将离队的队员从群聊中移除
                removeHuanXinUserFromChatGroup(group.getRoomId(), user.getUserBase().getAccount());

                nickNameSb.append(user.getNickName()).append("、");
            }

//            //获得团队群的成员token（不包括自己）
//            String[] groupMembers = commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT);
//            // 队员离开队伍之后，队伍人员少于2人时，队伍自动解散
//            if(groupMembers.length < MIN_MEMBERS - 1){
//                // 解散队伍
//                group.setStatus(GroupTypeAndStatus.GROUP_DISSOLUTION);
////                group.setModifyTime(new java.sql.Date());
//                groupService.updateGroup(group);
//
//                // 解散环信群
//                HuanXinChatGroupService.deleteChatGroup(group.getRoomId());
//
//                HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
//                huanXinMessageExtras.setRoomId(group.getRoomId());
//                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "团队成员少于2人，自动解散",
//                        HuanXinSendMessageType.GROUP_DISSOLUTION, huanXinMessageExtras,
//                        commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));
//
//                //清除成员表数据
//                param.clear();
//                param.put("groupId", group.getId());
//                groupMemberService.removeGroupMemberByParam(param);
//
//                userRecommendService.removeUserRecommendByGroupId(param);
//
//                runMatchUtil.cleanRedis(group.getId());
//            }else{


            // 设置队伍修改时间为当前，用于后台匹配算法执行时，判断队伍信息是否为最新
            group.setModifyTime(new Date());
            group.setRunTime(currentTime);
            group.setType(this.getGroupSexType(groupMemberList, null, waitToRemove));
            groupService.updateGroup(group);

            //清除队伍所有推荐信息,准备重新开始匹配流程
            clearGroupRecommendData(group.getId());

//                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
//                        nickNameSb.substring(0, nickNameSb.length() - 1) + " 离开队伍\n", HuanXinSendMessageType.ADD_OR_QUIT,
//                        null, group.getRoomId());

            new SendGroupMemberQuitMsgThread(group, nickNameSb.substring(0, nickNameSb.length() - 1)).run();

            runMatchUtil.addToRedis(group, true, null, waitToRemoveId);
//            }

            /** 提交事务 */
            transactionHelper.commit(transactionStatus);

            //给被移除队伍的队员发环信消息
            for (User user : waitToRemove) {
                HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                huanXinMessageExtras.setRoomId(group.getRoomId());
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        " 您被踢出群", HuanXinSendMessageType.GROPU_BE_REMOVE, huanXinMessageExtras, user.getUserBase().getAccount() + "");
            }

        } catch (HuanXinMessageException e) {
            logger.error("GroupControllerImpl removeGroupMember error " + e.getStackTrace().toString());
            /** 回滚事务 */
            transactionHelper.rollback(transactionStatus);
            return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
        } catch (HuanXinChatGroupException e) {
            logger.error("GroupControllerImpl removeGroupMember error " + e.getStackTrace().toString());
            /** 回滚事务 */
            transactionHelper.rollback(transactionStatus);
            return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("GroupControllerImpl removeGroupMember error " + e.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
            return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
        }

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);

    }

    @Override
    public Object quitGroup() {
        logger.info("GroupControllerImpl quitGroup begin ...");
        String currentTime = runMatchUtil.getSysTime();
        //step 1.1:检测群聊是否存在，是否进行中
        Group group = commonService.getRunningGroup();
        if (group != null) {

            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
            List<User> toRemove = new ArrayList<>();
            toRemove.add(user);
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("userId", shiroService.getLoginUserBase().getId());
                param.put("groupId", group.getId());
                GroupMember groupMember = groupMemberService.getGroupMember(param);

                // 成员为队长时，不允许离开队伍，目前队长离开队伍即解散队伍。
                if (groupMember.getType() == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER) {
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_917.code, ResultCode.CODE_917.msg, null);
                }

                // 直接从成员表中删除
//                groupMember.setStatus(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_QUIT);
//                groupMemberService.updateGroupMember(groupMember);
                groupMemberService.removeGroupMember(groupMember.getId());
                // 添加用户的群历史记录状态（退出）
                addGroupMemberHistory(group.getId(), user.getUserId(),
                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_QUIT);

//                //获得团队群的成员token（不包括自己）
//                String[] groupMembers = commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT);
//
//                // 队员离开队伍之后，队伍人员少于2人时，队伍自动解散
//                if(groupMembers.length <= MIN_MEMBERS - 1){
//                    // 解散队伍
//                    group.setStatus(GroupTypeAndStatus.GROUP_DISSOLUTION);
////                    group.setModifyTime(new Date());
//                    groupService.updateGroup(group);
//
//                    // 解散环信群
//                    HuanXinChatGroupService.deleteChatGroup(group.getRoomId());
//
//                    HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
//                    huanXinMessageExtras.setRoomId(group.getRoomId());
//                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "团队成员少于2人，自动解散",
//                            HuanXinSendMessageType.GROUP_DISSOLUTION, huanXinMessageExtras,
//                            commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));
//
//                    //清除成员表数据
//                    param.clear();
//                    param.put("groupId", group.getId());
//                    groupMemberService.removeGroupMemberByParam(param);
//                    userRecommendService.removeUserRecommendByGroupId(param);
//                    runMatchUtil.cleanRedis(group.getId());
//                }else{
                // 将离队的队员从群聊中移除
                removeHuanXinUserFromChatGroup(group.getRoomId(), user.getUserBase().getAccount());

                // 设置队伍修改时间为当前，用于后台匹配算法执行时，判断队伍信息是否为最新
                group.setModifyTime(new Date());
                group.setRunTime(currentTime);
                group.setType(this.getGroupSexType(commonService.getGroupMember(group.getId()), null, toRemove));
                groupService.updateGroup(group);

                //清除队伍所有推荐信息,准备重新开始匹配流程
                clearGroupRecommendData(group.getId());


//                    // 通知其他群聊中其他成员
//                    HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
//                    huanXinMessageExtras.setRoomId(group.getRoomId());
//                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
//                            user.getNickName() + " 离开队伍\n",
//                            HuanXinSendMessageType.ADD_OR_QUIT, huanXinMessageExtras, group.getRoomId());
                new SendGroupMemberQuitMsgThread(group, user.getNickName()).run();
                List<Long> waitToRemove = new ArrayList<>();
                waitToRemove.add(user.getUserId());
                runMatchUtil.addToRedis(groupService.getGroupById(group.getId()), true, null, waitToRemove);

//                }

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);

            } catch (HuanXinChatGroupException e) {
                logger.error("GroupControllerImpl quitGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
//            } catch (HuanXinMessageException e) {
//                logger.error("GroupControllerImpl quitGroup error " + e.getStackTrace().toString());
//                /** 回滚事务 */
//                transactionHelper.rollback(transactionStatus);
//                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                logger.error("GroupControllerImpl quitGroup error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }

    private int getGroupSexType(List<GroupMember> groupMemberList, List<User> toAdd, List<User> toRemove) {

        //统计队员的性别情况，第一位男、第二位女
        int[] sexCount = new int[]{0, 0};

        if (checkParaNULL(groupMemberList)) {
            for (GroupMember g : groupMemberList) {
                if (SexType.MALE.equals(g.getUser().getSex())) {
                    sexCount[0] = sexCount[0] + 1;
                } else {
                    sexCount[1] = sexCount[1] + 1;
                }
            }
        }

        if (checkParaNULL(toAdd)) {
            for (User u : toAdd) {
                if (SexType.MALE.equals(u.getSex())) {
                    sexCount[0] = sexCount[0] + 1;
                } else {
                    sexCount[1] = sexCount[1] + 1;
                }
            }
        }

        if (checkParaNULL(toRemove)) {
            for (User u : toRemove) {
                if (SexType.MALE.equals(u.getSex())) {
                    sexCount[0] = sexCount[0] - 1;
                } else {
                    sexCount[1] = sexCount[1] - 1;
                }
            }
        }


        if (sexCount[0] >= 1 && sexCount[1] >= 1) {
            return GroupTypeAndStatus.GROUP_MIX;
        } else if (sexCount[0] >= 1) {
            return GroupTypeAndStatus.GROUP_MALE;
        } else {
            return GroupTypeAndStatus.GROUP_FEMALE;
        }

    }

    @Deprecated
    @Override
    public Object startMatch() {
        final Group group = commonService.getRunningGroup();
        if (group != null) {
            try {
                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                //如果群的地点或群的区域为空，则提示
//                if ((user.getSex().equals(SexType.MALE) && group.getLocationId() == null) ||
//                        (user.getSex().equals(SexType.FEMALE) && group.getCityId() == null)) {
//                    return sendResult(ResultCode.CODE_912.code, ResultCode.CODE_912.msg, null);
//                }
                //查询群成员，如果少于两人，则无法匹配
                List<GroupMember> list = commonService.getGroupMember(group.getId());
                if (list == null || list.size() < 2) {
                    return sendResult(ResultCode.CODE_914.code, ResultCode.CODE_914.msg, null);
                }

                //将状态更改为匹配中
                group.setStatus(GroupTypeAndStatus.GROUP_MATCHING);
                boolean result = groupService.updateGroup(group);

                if (result) {
                    //调用匹配算法，后台开始线程计算，计算成功之后推送到客户端
                    //superveneRecommendService.doRecommend(group.getId());

                    // 调用匹配线程池，执行匹配引擎
                    runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), false);

                    //推送开始匹配状态给队员 -- 透传消息
//                    JPushUtils.sendMessageJPush("队伍开始匹配", JPushType.START_MATCHING, null, null, null, null,
//                            commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));

                    //发送环信消息 推送开始匹配状态给队员
                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "队伍开始匹配",
                            HuanXinSendMessageType.START_MATCHING, null,
                            commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));


                    //清除之前的队伍
//                    recommendService.removeMainIdRecommend(group.getId());
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);

                }
                //TODO 临时加入，以后删除
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        //推送同区域的
//                        recommendPushToClientByArea(group);
//                    }
//                }, 1000 * 5);

                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            } catch (Exception e) {
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }

    /**
     * 用于队伍信息修改时，直接在队列中添加
     *
     * @param group
     */
    private void runModifyMatchEngine(Group group) {
        // 将队伍修改时间放入缓存中
//        if(ShareCacheVar.groupModifyMap.contains(group.getId())){
//            ShareCacheVar.groupModifyMap.remove(group.getId());
//        }
        ShareCacheVar.groupModifyMap.put(group.getId(), group.getModifyTime());
        synchronized (ShareCacheVar.groupWaitingQueue) {
            Iterator iterator = ShareCacheVar.groupWaitingQueue.iterator();
            Map<String, Object> it;
            // 将前面队列等待中的队伍置为无效
            while (iterator.hasNext()) {
                it = (Map<String, Object>) iterator.next();
                Long groupIdWaiting = (Long) it.get("groupId");
                Long userIdWaiting = (Long) it.get("userId");
                Long isValid = (Long) it.get("isValid");
                if (groupIdWaiting.equals(group.getId()) && isValid.equals(1L)) {
                    it.put("isValid", 0L);
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", group.getId());
            map.put("userId", shiroService.getLoginUserBase().getId());
            map.put("isReMatch", 1L);
            map.put("isValid", 1L);
            map.put("time", group.getModifyTime());
            ShareCacheVar.groupWaitingQueue.add(map);
        }
    }

    /**
     * 跑匹配算法
     *
     * @param groupId
     * @param userId
     * @param isReMatch
     */
    private void runMatchEngine(Long groupId, final Long userId, boolean isReMatch) {
        // 只有当前队伍没有在执行匹配线程时，才执行新的匹配线程
        logger.info("==============runMatchEngine:" + groupId + "===================");
        logger.info("==============MatchThread.matchThreadMap:" + MatchThread.matchThreadMap.toString() + "===================");

        if (ShareCacheVar.ifUseNew) {
            // 新的匹配机制
            synchronized (ShareCacheVar.groupWaitingQueue) {

                if (ifAddToQueue(ShareCacheVar.groupWaitingQueue, groupId, userId)) {
                    logger.info("============== ShareCacheVar.groupWaitingQueue not contain groupId " + groupId);
                    synchronized (ShareCacheVar.groupNoMatchWaitingQueue) {
                        if (ifAddToQueue(ShareCacheVar.groupNoMatchWaitingQueue, groupId, userId)) {
                            logger.info("============== ShareCacheVar.groupNoMatchWaitingQueue not contain groupId " + groupId);
                            Map<String, Object> map = new HashMap<>();
                            map.put("groupId", groupId);
                            map.put("userId", userId);
                            map.put("isReMatch", isReMatch ? 1L : 0L);
                            map.put("isValid", 1L);
                            map.put("time", groupService.getGroupById(groupId).getModifyTime());
                            ShareCacheVar.groupWaitingQueue.add(map);
                            logger.info("============groupWaitingQueue add groupId " + groupId);
                        }
                    }
                }
               /* Iterator iterator = ShareCacheVar.groupWaitingQueue.iterator();
                Map<String, Long> it;
                while (iterator.hasNext()){
                    it = (Map<String, Long>) iterator.next();
                    Long groupIdWaiting = it.get("groupId");
                    Long userIdWaiting = it.get("userId");
                    Long isValid = it.get("isValid");
                    logger.info("groupWaitingQueue groupId " + groupIdWaiting);
                    if (groupIdWaiting.equals(groupId) && isValid.equals(1L)){
                        if(!userIdWaiting.equals(userId)){
                            logger.info("add group member to memberWaitingMap");
                            List<Long> memberWaiting = (List<Long>) ShareCacheVar.memberWaitingMap.get(groupId);

                            if (null == memberWaiting){
                                List<Long> memberList = new ArrayList<>();
                                memberList.add(userId);
                                ShareCacheVar.memberWaitingMap.put(groupId, memberList);
                            }else{
                                //如果当前在队列中的进程，是当前用户触发的，不进行另外的等待队列
                                if (!userIdWaiting.equals(userId)){
                                    for(Long l : memberWaiting){
                                        if(l.equals(userId)){
                                            return;
                                        }
                                    }
                                    logger.info("============memberWaitingMap add groupId ");
                                    // 等待队伍中没有当前用户，添加至等待队伍中
                                    memberWaiting.add(userId);
                                }
                            }
                        }
                        logger.info("============groupWaitingQueue has groupId " + groupIdWaiting);
                        return;
                    }
                }*/
            }

        } else {
            ThreadPoolTaskExecutor poolTaskExecutor = SpringContextUtil.getBean("matchThreadPool");
            poolTaskExecutor.execute(new MatchThread(groupId, userId, isReMatch));
        }

    }

    private boolean ifAddToQueue(ConcurrentLinkedQueue queue, Long groupId, Long userId) {
        Iterator iterator = queue.iterator();
        Map<String, Object> it;
        while (iterator.hasNext()) {
            it = (Map<String, Object>) iterator.next();
            Long groupIdWaiting = (Long) it.get("groupId");
            Long userIdWaiting = (Long) it.get("userId");
            Long isValid = (Long) it.get("isValid");
            logger.info("groupWaitingQueue groupId " + groupIdWaiting);
            if (groupIdWaiting.equals(groupId) && isValid.equals(1L)) {
                logger.info("===============ifAddToQueue queue contain current group");
                if (!userIdWaiting.equals(userId)) {
                    logger.info("add group member to memberWaitingMap");
                    List<Long> memberWaiting = (List<Long>) ShareCacheVar.memberWaitingMap.get(groupId);

                    if (null == memberWaiting) {
                        List<Long> memberList = new ArrayList<>();
                        memberList.add(userId);
                        ShareCacheVar.memberWaitingMap.put(groupId, memberList);
                    } else {
                        //如果当前在队列中的进程，是当前用户触发的，不进行另外的等待队列
                        if (!userIdWaiting.equals(userId)) {
                            for (Long l : memberWaiting) {
                                if (l.equals(userId)) {
                                    return false;
                                }
                            }
                            logger.info("============memberWaitingMap add groupId ");
                            // 等待队伍中没有当前用户，添加至等待队伍中
                            memberWaiting.add(userId);
                        }
                    }
                }
                logger.info("============groupWaitingQueue has groupId " + groupIdWaiting);
                return false;
            }
        }
        logger.info("===============ifAddToQueue queue not contain current group");
        return true;
    }

    @Deprecated
    @Override
    public Object endMatch() {
        //TODO 终止后台线程
        Group group = commonService.getRunningGroup();
        if (group != null) {
            //将状态更改为创建中
            group.setStatus(GroupTypeAndStatus.GROUP_CREATE);
            if (groupService.updateGroup(group)) {

                //清除内存块中该条用户的记录
                superveneRecommendService.cleanMemorystatus((long) (group.getAreaId() == null ?
                        group.getCityId() : group.getAreaId()), group.getId(), group.getType());

                //推送结束匹配状态给队员
//                JPushUtils.sendMatchJPush("取消匹配", "队伍取消匹配", JPushType.END_MATCHING, null, null, null,
//                        commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_TOKEN));

                try {
                    //推送结束匹配状态给队员 -- 透传消息
//                JPushUtils.sendMessageJPush("队伍取消匹配", JPushType.END_MATCHING, null, null, null, null,
//                        commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));

                    //发送环信消息 推送结束匹配状态给队员
                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "队伍取消匹配",
                            HuanXinSendMessageType.END_MATCHING, null,
                            commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT));
                } catch (HuanXinMessageException e) {
                    e.printStackTrace();
                }

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }

    @Override
    public Object vote(Long userRecommendId, Boolean isLike) {
        logger.info("GroupControllerImpl vote begin...");
        if (checkParaNULL(userRecommendId, isLike)) {
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                Map<String, Object> param = new HashMap<>();
                Map<String, Object> resultMap = new HashMap<>();

                //检测群聊是否存在，是否进行中
                Group group = commonService.getRunningGroup();
                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                //群聊不存在，则提示错误
                if (group == null) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                //查询用户推荐记录
                param.put("id", userRecommendId);
                List<UserRecommend> userRecommends = userRecommendService.getUserRecommendByParams(param);

                if (userRecommends.size() == 0) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                // 将用户推荐信息设置为已发送
                UserRecommend userRecommend = userRecommends.get(0);
                userRecommend.setStatus(RecommendType.HAVE_SEND);
                userRecommendService.updateUserRecommend(userRecommend);

                //查询推荐记录
//                Recommend recommend = recommendService.getRecommendById(userRecommend.getRecommendId());
                Recommend recommend = userRecommend.getRecommend();
                if (recommend == null) {
                    transactionHelper.commit(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }

//                Group matchGroup = groupService.getGroupById(recommend.getMatchGroupId());
//                if (matchGroup == null){
//                    transactionHelper.commit(transactionStatus);
//                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
//                }
//
//                //如果推荐信息创建时间早于队伍修改时间,则认为本次投票,只修改userRecommend为已发送,不做其他操作.
//                if (userRecommend.getCreateTime().before(matchGroup.getModifyTime())) {
//                    transactionHelper.commit(transactionStatus);
//                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
//                }

                // 用户如果已经投过票，对已投票结果进行更新，如未投票，则添加投票结果
                param.clear();
                param.put("userId", shiroService.getLoginUserBase().getId());
                param.put("recommendId", recommend.getId());
                Vote vote = voteService.getVote(param);
                boolean result;
                if (null == vote) {
                    // 添加投票记录
                    result = addVote(recommend.getId(), shiroService.getLoginUserBase().getId(), isLike);
                } else {
                    vote.setIsLike(isLike);
                    result = voteService.updateVote(vote);
                }

                boolean isAllTeamLikeResult = false;
                if (result) {
                    //获得群成员
                    List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());

                    //step2.3:是否全队已赞成，即投赞成票人数大于或等于一半
                    isAllTeamLikeResult = isAllTeamLike(recommend, groupMemberList);
                    logger.info("GroupControllerImpl vote isAllTeamLikeResult " + isAllTeamLikeResult);
                }
                /** 提交事务 */
                transactionHelper.commit(transactionStatus);

                // 投票超过一半,voteSuccess中另起事务处理创建约会群的信息插入
                if (isAllTeamLikeResult) {
                    voteSuccess(recommend, user, group);
                }

                // 当用户推荐列表没有数据时，需要重新跑匹配算法
//                param.clear();
//                param.put("userId", user.getUserId());
//                param.put("status", RecommendType.NOT_SEND);
//                List<UserRecommend> notSendUserRecommends = userRecommendService.getUserRecommendByParams(param);
//                if (notSendUserRecommends.size() == 0){
//                    // 调用匹配线程池，执行匹配引擎
//                    this.runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), false);
//                }

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            } catch (Exception e) {
                logger.error("GroupControllerImpl vote error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
            }
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    /**
     * 添加投票记录
     *
     * @param recommendId
     * @param userId
     * @param isLike
     * @return
     */
    private boolean addVote(Long recommendId, Long userId, boolean isLike) {

        Vote vote = new Vote();
        vote.setRecommendId(recommendId);
        vote.setUserId(userId);
        vote.setIsLike(isLike);
        return voteService.addVote(vote);
    }

    @Override
    public Object pushRecommend(Long groupId) {

        if (checkParaNULL(groupId)) {
            //推送一个队伍到客户端
            Group group = groupService.getGroupById(groupId);
            recommendPushToClient(group);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object searchVoteStatus() {
        //查询用户当前群聊
        Group group = commonService.getRunningGroup();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (group != null) {
            //如果是匹配中的队伍，则可以查询用户的投票情况
            if (group.getStatus() == GroupTypeAndStatus.GROUP_MATCHING) {
                resultMap.put("recommendId", null);
                resultMap.put("groupMemberVoteList", null);
                resultMap.put("matchGroupMemberList", null);
//                resultMap.put("vote", null);

                //查询是否有正在投票的队伍，有则查询对方队伍的队友
                param.clear();
                param.put("mainGroupId", group.getId());
                param.put("type", RecommendType.NOT_SEND);

                //查询自己的队友列表
                List<GroupMember> groupMemberList = commonService.getGroupMember(group.getId());
                List<GroupMemberVote> groupMemberVoteList = new ArrayList<>();

                List<Recommend> list = recommendService.getRecommendListByScore(param);
                if (list != null && list.size() != 0) {
                    resultMap.put("recommendId", list.get(0).getId());
                    //查询匹配的队伍
                    Group matchGroup = groupService.getGroupById(list.get(0).getMatchGroupId());
                    if (matchGroup != null) {
                        //查询团队成员
                        List<GroupMember> voteGroupMemberList = commonService.getGroupMember(matchGroup.getId());
                        //获取头像
                        ossFileUtils.getUserHeadGroupMemberList(voteGroupMemberList, null);
//                        resultMap.put("group", matchGroup);
                        resultMap.put("matchGroupMemberList", voteGroupMemberList);
                    }
                }
                //查询是否有投过票
                for (int i = 0; i < groupMemberList.size(); i++) {
                    GroupMemberVote groupMemberVote = new GroupMemberVote();
                    groupMemberVote.setUserId(groupMemberList.get(i).getUserId());
                    groupMemberVote.setGroupId(groupMemberList.get(i).getId());
                    groupMemberVote.setType(groupMemberList.get(i).getType());
                    groupMemberVote.setHead(ossFileUtils.getPicturesUrl(groupMemberList.get(i).getUser().getHeadId(), null));

                    if (list != null && list.size() != 0) {
                        param.clear();
                        param.put("recommendId", list.get(0).getId());
                        param.put("userId", groupMemberList.get(i).getUserId());
                        //查询是否有投过票
                        Vote voteTemp = voteService.getVote(param);
                        if (voteTemp == null) {
                            groupMemberVote.setHasVote(0);
                        } else if (voteTemp.getIsLike()) {
                            groupMemberVote.setHasVote(1);
                        } else {
                            groupMemberVote.setHasVote(2);
                        }
                    } else {
                        groupMemberVote.setHasVote(0);
                    }
                    groupMemberVoteList.add(groupMemberVote);
                    resultMap.put("groupMemberVoteList", groupMemberVoteList);
                }

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
            }
            return sendResult(ResultCode.CODE_913.code, ResultCode.CODE_913.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
    }


    @Override
    public Object searchConsultation(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                                     @ModelAttribute Consultation object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Consultation> list = consultationService.getConsultationList(param);
                    //设置图片的链接
                    for (Consultation consultation : list) {
                        if (consultation.getImageId() != null) {
                            consultation.setImage(ossFileUtils.getPictures(consultation.getImageId(), null));
                        }
                    }
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Consultation obj = consultationService.getConsultationById(id);
                        //设置图片的链接
                        if (obj != null && obj.getImageId() != null) {
                            obj.setImage(ossFileUtils.getPictures(obj.getImageId(), null));
                        }
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Consultation> pagingForKeyword = consultationService.getConsultationPage(pageEntity);
                    //设置图片的链接
                    if (checkParaNULL(pagingForKeyword, pagingForKeyword.getResultList())) {
                        for (Consultation consultation : pagingForKeyword.getResultList()) {
                            if (consultation.getImageId() != null) {
                                consultation.setImage(ossFileUtils.getPictures(consultation.getImageId(), null));
                            }
                        }
                    }
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 10000)
    public Object searchRecommendTeam(String excludeUrId) {
        logger.info("GroupControllerImpl searchRecommendTeam begin excludeUrId:" + excludeUrId + " and userId:" + shiroService.getLoginUserBase().getId());

        Map<String, Object> resultMap = new HashMap<>();
        //取得用户当前的队伍
        Group group = commonService.getRunningGroup();
        if (null == group) {
            return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
        }

        groupService.updateGroupDailyLiving(group.getId());
        if (null == group.getRunTime()) {
            // 旧数据，队伍中是没有该字段值，需要补回
            group.setRunTime(runMatchUtil.getSysTime());
            groupService.updateGroup(group);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", shiroService.getLoginUserBase().getId());
        params.put("status", RecommendType.NOT_SEND);
        params.put("currentGroupId", group.getId());

        List<UserRecommend> userRecommendNotSendList = userRecommendService.getUserRecommendByParams(params);
        //取得未推送的匹配队伍
        List<UserRecommend> userRecommendList = new ArrayList<>();
        if (checkParaNULL(excludeUrId)) {
            params.put("excludeUrId", excludeUrId);
            userRecommendList = userRecommendService.getUserRecommendByParams(params);
        } else {
            userRecommendList = userRecommendNotSendList;
        }


        // 当匹配队伍之后，只剩一队时，由于app传过来的id，把当前app的过滤掉之后，查回来的userRecommendList为空，所以添加多个判断
        // 只有数据未推送队伍为空时，才重跑匹配
        if ((userRecommendList == null || userRecommendList.size() == 0) && userRecommendNotSendList.size() == 0) {
            // 当用户推荐信息为空时，重跑匹配
//            this.runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), false);

            runMatchUtil.addToRedis(group, false, null, null);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }

        List<UserRecommend> disUserRecommandList = new ArrayList<>();

        for (UserRecommend userRecommend : userRecommendList) {

            // 保证取到的推荐信息是当前队伍的，排除脏数据
            if (userRecommend.getRecommend().getMainGroupId().equals(group.getId())
                    && !userRecommend.getRecommend().getMatchGroupId().equals(group.getId())) {
                Long matchGroupId = userRecommend.getRecommend().getMatchGroupId();
                Group matchGroup = groupService.getGroupById(matchGroupId);
                Map<String, Object> param = new HashMap<>();
                param.put("groupId", matchGroupId);
                param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
                // 取得推荐队伍的成员信息
                List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(param);

                //匹配中的队伍已经解散、或不存在，不推送当前队伍，并将推送消息状态改为已推送
                //或者推荐信息创建时间在队伍修改时间之前，为无效推荐
                if (null == matchGroup
                        || matchGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)
                        || groupMemberList.size() == 0) {
                    disUserRecommandList.add(userRecommend);
                    continue;
                }

                if (null == group.getRecommendSex()) {
                    if (GroupTypeAndStatus.GROUP_MIX == matchGroup.getType()) {
                        disUserRecommandList.add(userRecommend);
                        continue;
                    }
                    if (group.getType().intValue() == matchGroup.getType().intValue()) {
                        disUserRecommandList.add(userRecommend);
                        continue;
                    }
                } else {
//                    if(GroupTypeAndStatus.GROUP_MIX != group.getRecommendSex()){
//                        if(GroupTypeAndStatus.GROUP_MIX == matchGroup.getType()){
//                            disUserRecommandList.add(userRecommend);
//                            continue;
//                        }
//                        if(group.getRecommendSex().intValue() != matchGroup.getType().intValue()){
//                            disUserRecommandList.add(userRecommend);
//                            continue;
//                        }
//                    }
                    // 向前兼容查看全部
                    if (group.getRecommendSex() != GroupTypeAndStatus.GROUP_ALL &&
                            group.getRecommendSex().intValue() != matchGroup.getType().intValue()) {
                        disUserRecommandList.add(userRecommend);
                        continue;
                    }
                }

                resultMap.put("userRecommendId", userRecommend.getId());
                resultMap.put("matchGroupMemberList", convertToJSONUser(groupMemberList));
                resultMap.put("province", matchGroup.getProvince());
                resultMap.put("city", matchGroup.getCity());
                resultMap.put("area", matchGroup.getArea());
                resultMap.put("barId", matchGroup.getBarId());
                userRecommendService.setUserRecommendSend(disUserRecommandList);
                //当查询到用户一条未推荐信息，就直接返回给app
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
            }
        }

        userRecommendService.setUserRecommendSend(disUserRecommandList);
        // 过滤掉一些无效队伍之后，未推送队伍为0时，重新跑匹配规则
        if (userRecommendNotSendList.size() - disUserRecommandList.size() == 0) {
//            this.runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), false);
            runMatchUtil.addToRedis(group, false, null, null);
        }
        // 只有过滤队伍为空时，且查询推送队伍也为空时，需要重新跑匹配规则
        // excludeUrId == null && userRecommendList == 0
//        if( ("").equals( excludeUrId ) || null == excludeUrId){
        // 当用户推荐消息进行过滤之后，推荐消息为空时，重新跑匹配规则
//            this.runMatchEngine(group.getId(), shiroService.getLoginUserBase().getId(), false);
//            userRecommendService.setUserRecommendSend(disUserRecommandList);
//        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object dissolution() {
        logger.info("GroupControllerImpl dissolution begin ...");
        Group group = commonService.getRunningGroup();
        User leader = commonService.getGroupLeader(group.getId());
        if (leader.getUserId().equals(shiroService.getLoginUserBase().getId())) {
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                group.setStatus(GroupTypeAndStatus.GROUP_DISSOLUTION);
                group.setModifyTime(new Date());
                groupService.updateGroup(group);
                recommendService.sumGroupRecommend(group.getId());

                this.clearGroupRecommendData(group.getId());

//                userRecommendService.removeUserRecommendByGroupId(params);

                String[] groupMembers = commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT);

                Map<String, Object> params = new HashMap<>();
                params.put("groupId", group.getId());
                // 直接从成员表中删除
                groupMemberService.removeGroupMemberByParam(params);
                // 添加用户的群历史记录状态（退出）
//                addGroupMemberHistory(group.getId(), user.getUserId(),
//                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_QUIT);

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
                // 解散环信群
                HuanXinChatGroupService.deleteChatGroup(group.getRoomId());
                HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                huanXinMessageExtras.setRoomId(group.getRoomId());
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "您好，您所在的队伍已经被队长解散。",
                        HuanXinSendMessageType.GROUP_DISSOLUTION, huanXinMessageExtras,
                        groupMembers);

            } catch (HuanXinMessageException e) {
                logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            } catch (HuanXinChatGroupException e) {
                logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getStackTrace());
                logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

            runMatchUtil.cleanRedis(group.getId());

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_903.code, ResultCode.CODE_903.msg, null);
    }

    @Override
    public Object searchLocalGroup(Long cityId, Long areaId) {
        logger.info("GroupControllerImpl searchLocalGroup begin ...");
        if (checkParaNULL(cityId, areaId)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            // 取得字典表中配置的返回队伍数量
            int numOfGroup = Integer.valueOf(sysDicService.getSysDicByKey(NUM_LOCAL_GROUP).getValue());

            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
            params.put("cityId", cityId);
            params.put("areaId", areaId);
            params.put("status", GroupTypeAndStatus.GROUP_MATCHING);
            if (SexType.FEMALE.equals(user.getSex())) {
                params.put("type", GroupTypeAndStatus.GROUP_MALE);
            } else {
                params.put("type", GroupTypeAndStatus.GROUP_FEMALE);
            }

            // 取得同市同地区的异性队伍
            List<Group> groupList = groupService.getGroupList(params);

            // 当同市同地区小于字典配置的队伍数时，查询同个地市的队伍，补充
            // 排除已经查询出来的队伍
            if (groupList.size() < numOfGroup) {
                List<Long> ids = new ArrayList<>();
                for (Group g : groupList) {
                    ids.add(g.getId());
                }
                params.put("notContainsGroups", ids);
                params.remove("areaId");
                List<Group> sameCityGroupList = groupService.getGroupList(params);

                if (sameCityGroupList.size() <= numOfGroup - groupList.size()) {
                    groupList.addAll(sameCityGroupList);
                } else {
                    groupList.addAll(sameCityGroupList.subList(0, numOfGroup - groupList.size()));
                }

            }

            List<Map<String, Object>> localGroupList = new ArrayList<>();

            if (groupList.size() > 0) {
                for (int i = 0; i < numOfGroup && i < groupList.size(); i++) {
                    Map<String, Object> groupMap = new HashMap<>();
                    params.clear();
                    params.put("groupId", groupList.get(i).getId());
                    params.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS,
                            GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
                    List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(params);

                    groupMap.put("group", groupList.get(i));
                    groupMap.put("groupMemberList", ConvertBeanUtils.converToJSONGroupMember(groupMemberList));
                    localGroupList.add(groupMap);
                }
            }

            resultMap.put("localGroupList", localGroupList);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    /**
     * 手动提交事务，保证当环信推送消息给app时，app查询约会时，事务已经完成提交
     *
     * @param userRecommendId
     * @return
     */
    @Override
    public Object superLike(Long userRecommendId) {
        logger.info("GroupControllerImpl superLike begin...");
        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();
        Map<String, Object> resultMap = new HashMap<>();

        UserSuperLike userSuperLike = new UserSuperLike();
        try {
            int numOfSuperLike;
            Long userId = shiroService.getLoginUserBase().getId();
            // 取得字典表中配置的返回队伍数量
            List<UserSuperLike> userSuperLikes = userSuperLikeService.getTodaySuperLike(userId);
            UserSuperLikeConfig userSuperLikeConfig = userSuperLikeConfigService.getUserSuperLikeConfig(userId);
            User user = userService.getByUserId(userId);

            // 判断当前用户superlike配置次数
            if (null != userSuperLikeConfig) {
                numOfSuperLike = userSuperLikeConfig.getTimes();
            } else {
                numOfSuperLike = Integer.valueOf(sysDicService.getSysDicByKey(NUM_SUPER_LIKE).getValue());
            }

            if (userSuperLikes.size() >= numOfSuperLike) {
                //用户未配置对应的superlike次数，使用系统默认次数
                //superlike次数超过当天限制次数
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_920.code, ResultCode.CODE_920.msg + numOfSuperLike + "次", null);
            }

            //用户superlike剩余次数

            resultMap.put("userSuperLike", userSuperLikes.size());
            resultMap.put("superLikeConfig", numOfSuperLike);

            Map<String, Object> param = new HashMap<>();
            //查询用户推荐记录
            param.put("id", userRecommendId);
            List<UserRecommend> userRecommends = userRecommendService.getUserRecommendByParams(param);

            if (userRecommends.size() == 0) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_907.code, ResultCode.CODE_907.msg, resultMap);
            }

            //查询推荐记录
            Recommend recommend = recommendService.getRecommendById(userRecommends.get(0).getRecommendId());

            if (recommend == null) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_907.code, ResultCode.CODE_907.msg, resultMap);
            }

            // 添加superlike的记录
            userSuperLike.setUserId(user.getUserId());
            userSuperLike.setRecommendId(recommend.getId());
            userSuperLike.setGroupId(recommend.getMainGroupId());
            userSuperLike.setMatchGroupId(recommend.getMatchGroupId());
            userSuperLikeService.addUserSuperLike(userSuperLike);

            // 将用户推荐信息设置为已发送
            UserRecommend userRecommend = userRecommends.get(0);
            userRecommend.setStatus(RecommendType.HAVE_SEND);
            userRecommendService.updateUserRecommend(userRecommend);

            //检测群聊是否存在，是否进行中
            Group mainGroup = groupService.getGroupById(recommend.getMainGroupId());
            Group matchGroup = groupService.getGroupById(recommend.getMatchGroupId());
            //群聊不存在，则提示错误
            if (mainGroup == null || matchGroup == null) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, resultMap);
            }

            if (mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION) || matchGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, resultMap);
            }

            // 判断是否有约会存在
//            if (GroupTypeAndStatus.GROUP_MALE == mainGroup.getType()){
//                param.clear();
//                param.put("maleGroupId", recommend.getMainGroupId());
//                param.put("femaleGroupId", recommend.getMatchGroupId());
//                param.put("type", ActivityTypeAndStatus.BEGIN);
//            }else{
//                param.clear();
//                param.put("femaleGroupId", recommend.getMainGroupId());
//                param.put("maleGroupId", recommend.getMatchGroupId());
//                param.put("type", ActivityTypeAndStatus.BEGIN);
//            }

            String uniqueId = "";
            // 用两队的id生成对应约会的唯一键值
            if (mainGroup.getId() > matchGroup.getId()) {
                uniqueId = String.valueOf(mainGroup.getId()) + String.valueOf(matchGroup.getId());
            } else {
                uniqueId = String.valueOf(matchGroup.getId()) + String.valueOf(mainGroup.getId());
            }

            param.clear();
//            param.put("groupAId", recommend.getMainGroupId());
//            param.put("groupBId", recommend.getMatchGroupId());
            param.put("uniqueId", uniqueId);
            param.put("type", ActivityTypeAndStatus.BEGIN);

            Activity activity = activityService.getActivity(param);

            if (null != activity) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_1001.code, ResultCode.CODE_1001.msg, resultMap);
            }

            //直接创建约会队伍
            Activity newActivity = createActivity(recommend, user, mainGroup, matchGroup, 1);

            if (null == newActivity) {
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

            transactionHelper.commit(transactionStatus);
            //事务提交成功，用户superlike次数加1
            resultMap.put("userSuperLike", userSuperLikes.size() + 1);
            // 通过启线程推送消息
            String[] alias = commonService.getBothGroupMemberAccountOrToken(recommend, CommonService.USER_ACCOUNT);
            new SendActivityHuanXinThread(newActivity, alias, newActivity.getRoomId(), 1, mainGroup, matchGroup).run();
        } catch (DuplicateKeyException ex) {
            logger.info("GroupControllerImpl superLike DuplicateKeyException: " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
            return sendResult(ResultCode.CODE_1001.code, ResultCode.CODE_1001.msg, resultMap);
        } catch (Exception ex) {
            logger.error("GroupControllerImpl superLike error: " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, resultMap);
        }


        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    private List<JSONUser> convertToJSONUser(List<GroupMember> groupMemberList) {
        List<JSONUser> jsonUserList = new ArrayList<>();

        for (GroupMember groupMember : groupMemberList) {
            JSONUser jsonUser = new JSONUser();
            User user = userService.getByUserId(groupMember.getUserId());
            ImUser imUser = new ImUser();
            imUser.setUserName(user.getUserBase().getAccount());

            jsonUser.setUserId(groupMember.getUserId());
            jsonUser.setIdentify(user.getIdentify());
            ossFileUtils.getPhotoList(user.getAlbum(), null);
            List<Photo> photoList = user.getAlbum();

            for (Photo photo : photoList) {

                if (photo.getIsAvatar()) {
                    jsonUser.setAvatarId(photo.getId());
                    jsonUser.setAvatarUrl(photo.getImage().getUrl());
                }
            }
            jsonUser.setNickName(groupMember.getUser().getNickName());
            jsonUser.setNickNameChar(groupMember.getUser().getNickNameChr());
            jsonUser.setSex(groupMember.getUser().getSex());
            jsonUser.setImUser(imUser);
            jsonUserList.add(jsonUser);
        }
        return jsonUserList;
    }

    /**
     * 查询用户被邀请的群聊 -- 不分页
     * 群状态是非结束（创建，匹配，组队）
     * 群成员状态是邀请（非加入，踢出）
     *
     * @return 用户被邀请的群聊列表
     */
    public List<Group> getInvitingGroupList() {
        List<Integer> inStatus = new ArrayList<>();
        //查询在创建中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_CREATE);
        //查询在匹配中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
        //查询在组队中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_TEAM);

        List<Integer> inMemberStatus = new ArrayList<>();
        //查询加入状态的用户
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);

        Map<String, Object> param = new HashMap<>();
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("inStatus", inStatus);
        param.put("inMemberStatus", inMemberStatus);

        //如果用户已经在正在进行的队中，则list不为空
        return groupService.getGroupByStatusList(param);
    }

    /**
     * 查询用户被邀请的群聊 -- 分页
     * 群状态是非结束（创建，匹配，组队）
     * 群成员状态是邀请（非加入，踢出）
     *
     * @return 用户被邀请的群聊列表
     */
    public PagingResult<Group> getInvitingGroupPage(PageEntity pageEntity) {
        List<Integer> inStatus = new ArrayList<>();
        //查询在创建中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_CREATE);
        //查询在匹配中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
        //查询在组队中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_TEAM);

        List<Integer> inMemberStatus = new ArrayList<>();
        //查询加入状态的用户
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);

        Map<String, Object> param = new HashMap<>();
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("inStatus", inStatus);
        param.put("inMemberStatus", inMemberStatus);

        pageEntity.setParam(param);
        //如果用户已经在正在进行的队中，则list不为空
        return groupService.getGroupByStatusPage(pageEntity);
    }

    private GroupMember addGroup(Long groupId, Long userId, Integer type, Integer status) {
        GroupMember member = null;
        TransactionStatus transactionStatus = transactionHelper.start();
        try {
            member = addGroupMember(groupId, userId, type, status);

            if (member != null) {
                addGroupMemberHistory(groupId, userId, type, status);
            }

            transactionHelper.commit(transactionStatus);
        } catch (TransactionException tex) {
            transactionHelper.rollback(transactionStatus);

            logger.error("GroupControllerImpl addGroup error: " + tex.getStackTrace().toString());
        } catch (Exception ex) {
            transactionHelper.rollback(transactionStatus);

            logger.error("GroupControllerImpl addGroup error: " + ex.getStackTrace().toString());
        }

        return member;
    }

    /**
     * 添加队友进群
     *
     * @param groupId 团队群id
     * @param userId  用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */

    public GroupMember addGroupMember(Long groupId, Long userId, Integer type, Integer status) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMember.setType(type);
        groupMember.setStatus(status);
        return (groupMemberService.addGroupMember(groupMember) == true) ? groupMember : null;
    }

    /**
     * 添加队友进群历史记录表
     *
     * @param groupId 团队群id
     * @param userId  用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */
    public boolean addGroupMemberHistory(Long groupId, Long userId, Integer type, Integer status) {
        GroupMemberHistory groupMemberHistory = new GroupMemberHistory();
        groupMemberHistory.setGroupId(groupId);
        groupMemberHistory.setUserId(userId);
        groupMemberHistory.setType(type);
        groupMemberHistory.setStatus(status);
        return groupMemberHistoryService.addGroupMemberHistory(groupMemberHistory);

    }


    /**
     * 批量添加队友进群
     *
     * @param groupId 团队群id
     * @param userIds 用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */
    public boolean addGroupMemberBatch(Long groupId, Long[] userIds, Integer type, Integer status) {
        List<GroupMember> list = new ArrayList<>();
        for (Long userId : userIds) {
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(groupId);
            groupMember.setUserId(userId);
            groupMember.setType(type);
            groupMember.setStatus(status);
            list.add(groupMember);
        }
        return groupMemberService.addGroupMemberBatch(list);
    }

    /**
     * 批量添加队友进群历史记录表
     *
     * @param groupId 团队群id
     * @param userIds 用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */
    public boolean addGroupMemberHistoryBatch(Long groupId, Long[] userIds, Integer type, Integer status) {
        List<GroupMemberHistory> list = new ArrayList<>();
        for (Long userId : userIds) {
            GroupMemberHistory groupMemberHistory = new GroupMemberHistory();
            groupMemberHistory.setGroupId(groupId);
            groupMemberHistory.setUserId(userId);
            groupMemberHistory.setType(type);
            groupMemberHistory.setStatus(status);
            list.add(groupMemberHistory);
        }
        return groupMemberHistoryService.addGroupMemberHistoryBatch(list);

    }

    /**
     * 添加通知消息进数据库
     *
     * @param userId  用户id
     * @param type    消息类型，查看{@link MessageType}
     * @param content 消息内容
     * @param param   对象，针对每一个消息类型，是不同的对象
     * @return 是否添加成功
     */
    public boolean addMessage(Long userId, Integer type, String content, String param) {
        Message message = new Message();
        message.setUserId(userId);
        message.setContent(content);
        message.setType(type);
        message.setIsRead(false);
        message.setParam(param);
        return messageService.addMessage(message);
    }


    /**
     * 判断当前队伍投票成功率
     *
     * @param recommend
     * @param groupMemberList
     * @return
     */
    public boolean isAllTeamLike(Recommend recommend, List<GroupMember> groupMemberList) {

        //取得当前的投票情况
        Map<String, Object> param = new HashMap<>();
        param.put("recommendId", recommend.getId());
        List<Vote> voteList = voteService.getVoteList(param);

        // 统计当前队伍对推荐队伍的投票情况
        double successNum = 0;
        for (Vote v : voteList) {
            if (v.getIsLike()) {
                successNum++;
            }
        }
        int size = groupMemberList.size();

        //是否赞成票人数大于或等于一半
        if (voteList.size() / (double) size >= 0.5 && successNum / size >= 0.5) {
            recommend.setType(RecommendType.HAVE_SEND);
            recommendService.updateRecommend(recommend);
            return true;
        }
        return false;
    }

    // voteSuccess 并发锁
//    private static Object voteSuccessLock = new Object();

    /**
     * 对这个队伍投赞成票，查询是否有互相赞成的队伍，有则开启约会，无则继续推送
     *
     * @param recommend
     * @param user
     * @param group
     * @throws HuanXinChatGroupException
     */
    public void voteSuccess(Recommend recommend, User user, Group group) throws HuanXinChatGroupException {
        Map<String, Object> param = new HashMap<>();

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();

        try {

            // 添加赞成通过记录
            RecommendSuccess recommendSuccess = new RecommendSuccess();
            recommendSuccess.setMainGroupId(recommend.getMainGroupId());
            recommendSuccess.setMatchGroupId(recommend.getMatchGroupId());
            recommendSuccess.setMainRecommendId(recommend.getId());
            //这个字段是没用的
            recommendSuccess.setMatchRecommendId(recommend.getId());
            recommendSuccessService.addRecommendSuccess(recommendSuccess);

            //先提交事务,避免双方队伍同时投票like,但由于事务未提交无法组成约会.创建Activity需另起事务处理.
            transactionHelper.commit(transactionStatus);

            /** 开启事务 */
            TransactionStatus activityTransactionStatus = transactionHelper.start();

            try {
                //查询推荐队伍是不是有对本队伍投票通过的记录
                param.clear();
                param.put("mainGroupId", recommend.getMatchGroupId());
                param.put("matchGroupId", recommend.getMainGroupId());
                RecommendSuccess matchRecommendSuccess = recommendSuccessService.getRecommendSuccess(param);

                //是否需要推送消息
                boolean needPushMsg = false;
                Activity activity = null;
                Group mainGroup = null, matchGroup = null;

                //只有双方队伍互相投票通过时，才会生成约会记录
                if (matchRecommendSuccess != null) {

                    mainGroup = groupService.getGroupById(recommend.getMainGroupId());
                    matchGroup = groupService.getGroupById(recommend.getMatchGroupId());

                    param.clear();
                    param.put("id", matchRecommendSuccess.getMainRecommendId());
                    Recommend matchRecommend = recommendService.getRecommend(param);
                    // 判断该表中是否有对方对本队的投票成功记录，并且该记录的recommend的创建时间晚于本队的modifyTime，且对方group状态仍在匹配中
                    if (matchRecommend != null
                            && matchRecommend.getCreateTime().after(mainGroup.getModifyTime())
                            && matchGroup != null
                            && matchGroup.getStatus().equals(GroupTypeAndStatus.GROUP_MATCHING)) {
                        activity = createActivity(recommend, user, mainGroup, matchGroup, 0);

                        needPushMsg = true;
                    }
                }

                transactionHelper.commit(activityTransactionStatus);

                if (needPushMsg) {
                    String[] alias = commonService.getBothGroupMemberAccountOrToken(recommend, CommonService.USER_ACCOUNT);
                    new SendActivityHuanXinThread(activity, alias, activity.getRoomId(), 1, mainGroup, matchGroup).run();
                }

            } catch (DuplicateKeyException ex) {
                logger.error("GroupControllerImpl voteSuccess createActivity error " + ex.getStackTrace().toString());
                transactionHelper.rollback(activityTransactionStatus);
            } catch (Exception ex) {
                logger.error("GroupControllerImpl voteSuccess createActivity error " + ex.getStackTrace().toString());
                transactionHelper.rollback(activityTransactionStatus);
            }


        } catch (DuplicateKeyException ex) {
            logger.error("GroupControllerImpl voteSuccess error " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
        } catch (Exception ex) {
            logger.error("GroupControllerImpl voteSuccess error " + ex.getStackTrace().toString());
            transactionHelper.rollback(transactionStatus);
        }

    }

    /**
     * 组建约会群
     *
     * @param recommend
     * @param user
     * @param mainGroup
     * @param matchGroup
     */

    private Activity createActivity(Recommend recommend, User user, Group mainGroup, Group matchGroup, int isSuperLike) {

        try {
            Map<String, Object> param = new HashMap<>();
            //男女群的成员账户名
            String[] alias = commonService.getBothGroupMemberAccountOrToken(recommend, CommonService.USER_ACCOUNT);

            String activityName = buildActivityName(recommend);
            //创建环信群聊
            String roomId = HuanXinChatGroupService.createChatGroup(EmojiCharacterUtil.emojiRecovery2(activityName), "的约会", false, 11l, true, HuanXinConstant.SYSUSER, alias);

            Bar mainBar = barService.getBarById(mainGroup.getBarId());
            Bar matchBar = barService.getBarById(matchGroup.getBarId());
            int isExpire = 0;
            if ((null != mainBar && null != matchBar) && (1 == mainBar.getIsExpire() || 1 == matchBar.getIsExpire())) {
                isExpire = 1;
            }

            String uniqueId = "";
            // 用两队的id生成对应约会的唯一键值
            if (mainGroup.getId() > matchGroup.getId()) {
                uniqueId = String.valueOf(mainGroup.getId()) + String.valueOf(matchGroup.getId());
            } else {
                uniqueId = String.valueOf(matchGroup.getId()) + String.valueOf(mainGroup.getId());
            }
            //创建约会群
            Activity activity = new Activity();
            activity.setName(activityName);
            activity.setType(ActivityTypeAndStatus.BEGIN);
//            activity.setMaleGroupId(user.getSex().equals(SexType.MALE) ? recommend.getMainGroupId() : recommend.getMatchGroupId());
//            activity.setFemaleGroupId(user.getSex().equals(SexType.FEMALE) ? recommend.getMainGroupId() : recommend.getMatchGroupId());
            activity.setGroupAId(recommend.getMainGroupId());
            activity.setGroupBId(recommend.getMatchGroupId());
            activity.setRoomId(roomId);
            activity.setSuperLike(isSuperLike);
            activity.setIsExpire(isExpire);
            activity.setUniqueId(uniqueId);
            activityService.addActivity(activity);

            param.clear();
            param.put("groupId", mainGroup.getId());
            param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
            List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(param);

            param.clear();
            param.put("groupId", matchGroup.getId());
            param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
            groupMemberList.addAll(groupMemberService.getGroupMemberList(param));

            List<ActivityMember> activityMemberList = new ArrayList<>();
            for (GroupMember gm : groupMemberList) {
                ActivityMember am = new ActivityMember();
                am.setUserId(gm.getUserId());
                am.setActivityId(activity.getId());
                am.setStatus(ActivityTypeAndStatus.IN_ACTIVITY);
                am.setCreateTime(new Date());
                am.setGroupId(gm.getGroupId());
                activityMemberList.add(am);
            }
            activityMemberService.addActivityMemberBatch(activityMemberList);
            return activity;

        } catch (HuanXinChatGroupException e) {
            logger.error("GroupControllerImpl createActivity error " + e.getStackTrace().toString());
        }
        return null;
    }


    /**
     * 根据两个队伍的队伍成员昵称生成约会名
     *
     * @param recommend
     * @return
     */
    public String buildActivityName(Recommend recommend) {

        Map<String, Object> param = new HashMap<>();
        param.put("groupId", recommend.getMainGroupId());
        List<GroupMember> mainGroupMemberList = groupMemberService.getGroupMemberList(param);

        param.put("groupId", recommend.getMatchGroupId());
        List<GroupMember> matchGroupMemberList = groupMemberService.getGroupMemberList(param);

//        mainGroupMemberList.addAll(matchGroupMemberList);

        StringBuffer sb = new StringBuffer();
        for (GroupMember gm : mainGroupMemberList) {
            if (gm.getType().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER)) {
                sb.append(gm.getUser().getNickName()).append("、");
            }
        }

        for (GroupMember gm : matchGroupMemberList) {
            if (gm.getType().equals(GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER)) {
                sb.append(gm.getUser().getNickName()).append("、");
            }
        }

        return sb.toString().substring(0, sb.length() - 1) + "的约会";
    }

    /**
     * 推送匹配队伍到客户端
     *
     * @param group
     */
    public void recommendPushToClient(Group group) {
        Map<String, Object> param = new HashMap<>();
        param.put("mainGroupId", group.getId());
        //查询未推送的队伍
        param.put("type", RecommendType.NOT_SEND);
        List<Recommend> notSendlist = recommendService.getRecommendListByScore(param);
        //查询已推送的队伍
        param.put("type", RecommendType.HAVE_SEND);
        List<Recommend> haveSendlist = recommendService.getRecommendListByScore(param);

        /**
         * 推送有两种情况
         * 1.当为推送的数量为1的时候，则表示有新队伍加进去，所以要把这个新队伍推送给他
         * 2.当列表里还没有开始推送队伍的时候，这时候调用这个接口就要把队伍推送出去
         */
        if ((notSendlist != null && notSendlist.size() == 1) ||
                (notSendlist != null && notSendlist.size() != 0 && haveSendlist.size() == 0)) {
            List<GroupMember> matchGroupNumber = commonService.getGroupMember(notSendlist.get(0).getMatchGroupId());
            List<GroupMember> mainGroupNumber = commonService.getGroupMember(group.getId());
            List<Long> userIds = new ArrayList<>();
//            List<String> heads = new ArrayList<>();
            List<Long> headIds = new ArrayList<>();
            List<String> groupMemberTokens = new ArrayList<>();
            for (GroupMember groupMember : matchGroupNumber) {
                userIds.add(groupMember.getUserId());
//                heads.add(ossFileUtils.getUserHead(groupMember.getUser()).getHead().getUrl());
                headIds.add(groupMember.getUser().getHeadId());
            }
            for (GroupMember groupMember : mainGroupNumber) {
                groupMemberTokens.add(groupMember.getUser().getUserBase().getAccount());
            }

//            System.out.println("\n 已经匹配到一个队伍 \n");
//            JPushUtils.sendMatchJPush("匹配队伍", "已经匹配到一个队伍", JPushType.HAVE_MATCHING_RESULT,
//                    notSendlist.get(0).getId(), group.getLocation() != null ? group.getLocation().getAddress() : null,
//                    userIds.toArray(new Long[userIds.size()]), headIds.toArray(new Long[headIds.size()]),
//                    groupMemberTokens.toArray(new String[groupMemberTokens.size()]));

            //发送环信消息 将约会推送给两个队伍
            HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
            huanXinMessageExtras.setRecommendId(notSendlist.get(0).getId());
            huanXinMessageExtras.setLocation(group.getLocation() != null ? group.getLocation().getAddress() : null);
            huanXinMessageExtras.setUserIds(userIds.toArray(new Long[userIds.size()]));
            huanXinMessageExtras.setHeadIds(headIds.toArray(new Long[headIds.size()]));
            try {
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        "已经匹配到一个队伍", HuanXinSendMessageType.HAVE_MATCHING_RESULT, huanXinMessageExtras,
                        groupMemberTokens.toArray(new String[groupMemberTokens.size()]));
            } catch (HuanXinMessageException e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * 删除环信群成员
     *
     * @param roomId  环信群id
     * @param account 要删除的用户
     * @return
     * @throws HuanXinChatGroupException
     */
    public boolean removeHuanXinUserFromChatGroup(String roomId, String account) throws HuanXinChatGroupException {

        //查询 roomId 对应的环信群组
        Object responseWrapper = null;
        try {
            responseWrapper = HuanXinChatGroupService.getChatGroupDetails(roomId);
        } catch (HuanXinChatGroupException e) {
            //等于404表示群组不存在，则群成员也不存在
            if (e.getCode() == 404) {
                return true;
            }
        }

        if (responseWrapper == null) {
            return false;
        }

        JSONObject jsonObject = JSON.parseObject((String) responseWrapper);
        JSONArray data = jsonObject.getJSONArray("data");

        if (data == null || data.size() <= 0) {
            return false;
        }

        JSONObject group = data.getJSONObject(0);
        String groupId = group.getString("id");

        if (groupId == null || !groupId.equals(roomId)) {
            return false;
        }

        //responseStatus 等于200表示群组存在
        //执行删除环信群成员功能
        return HuanXinChatGroupService.removeSingleUserFromChatGroup(roomId, account);
    }

}
