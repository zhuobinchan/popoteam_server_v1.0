package com.geetion.puputuan.web.api.app.impl;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.LongitudeAndLatitudeUtils;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.model.jsonModel.JSONUserBase;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.app.ActivityController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class ActivityControllerImpl extends BaseController implements ActivityController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityMemberService activityMemberService;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMemberHistoryService groupMemberHistoryService;
    @Resource
    private LocationService locationService;
    @Resource
    private SignService signService;
    @Resource
    private BarService barService;
    @Resource
    private EvaluateService evaluateService;
    @Resource
    private CommonService commonService;
    @Resource
    private FriendRelationshipService friendRelationshipService;

    @Resource
    private TransactionHelper transactionHelper;

    @Resource
    private OssFileUtils ossFileUtils;

    /**
     * 签到地点离定点的距离
     */
    private static double SIGN_DISTANCE = 50000d;

    /**
     * 男女双方的距离
     */
    private static double MALE_FEMALE_DISTANCE = 50000d;


    @Override
    public Object runningActivity(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        logger.info("ActivityControllerImpl runningActivity begin...");
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("haveRunningActivity", false);
        resultMap.put("activityList", null);
        List<Activity> activityList = new ArrayList<>();
        switch (methodType){
            case 1:
                // 不分页查询所有数据；
                //查询该用户的约会，群聊是组队中状态，约会状态是正在进行中
                activityList = activityService.getRunningActivityList();
                break;
            case 2:
                // 分页查询
                PagingResult<Activity> activityListPage = activityService.getRunningActivityPage(pageEntity);
                activityList = activityListPage.getResultList();

                resultMap.put("totalPage", activityListPage.getTotalPage());
                resultMap.put("totalSize", activityListPage.getTotalSize());
                resultMap.put("currentPage", activityListPage.getCurrentPage());
                break;
        }

        if (activityList != null && activityList.size() > 0) {
            Map<String, Object> params = new HashMap<>();
            for (Activity activity : activityList){
                activity.setName(EmojiCharacterUtil.emojiRecovery2(activity.getName()));
                params.clear();
                params.put("activityId", activity.getId());
                params.put("status", ActivityTypeAndStatus.IN_ACTIVITY);
                List<ActivityMember> activityMembers = activityMemberService.getActivityMemberByParam(params);

                List<JSONUserBase> jsonUserList = new ArrayList<>();
                for(ActivityMember am: activityMembers){
                    ossFileUtils.getUserHead(am.getUser(), null);
                    JSONUserBase jub = ConvertBeanUtils.convertDBModelToJSONModel(am.getUser());
                    jub.setGroupId(am.getGroupId());
                    jsonUserList.add(jub);
                }

                activity.setActivityMemberList(jsonUserList);

            }

            resultMap.put("haveRunningActivity", true);
            resultMap.put("activityList", activityList);
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }


    @Override
    public Object searchActivity(Long activityId) {
        logger.info("ActivityControllerImpl searchActivity begin...");
        Map<String, Object> resultMap = new HashMap<>();

        if(checkParaNULL(activityId)) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", activityId);
            params.put("types",new int[]{ActivityTypeAndStatus.BEGIN, ActivityTypeAndStatus.SUCCESS});
            Activity activity = activityService.getActivityByUser(params);

            if (null == activity){
                return sendResult(ResultCode.CODE_904.code, ResultCode.CODE_904.msg, null);
            }

            activity.setName(EmojiCharacterUtil.emojiRecovery2(activity.getName()));

            params.clear();
            params.put("activityId", activityId);
            params.put("status", ActivityTypeAndStatus.IN_ACTIVITY);
            List<ActivityMember> activityMembers = activityMemberService.getActivityMemberByParam(params);

            List<JSONUserBase> jsonUserList = new ArrayList<>();
            for(ActivityMember am: activityMembers){
                ossFileUtils.getUserHead(am.getUser(), null);
                JSONUserBase jub = ConvertBeanUtils.convertDBModelToJSONModel(am.getUser());
                jub.setGroupId(am.getGroupId());
                jsonUserList.add(jub);
            }

            activity.setActivityMemberList(jsonUserList);
            resultMap.put("activity", activity);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object activityMember() {
        logger.info("ActivityControllerImpl activityMember begin...");
        Map<String, Object> resultMap = new HashMap<>();
        //查询该用户的约会，群聊是组队中状态，约会状态是正在进行中
        Activity activity = commonService.getRunningActivity();
        if (activity != null) {
            //查询约会的用户
            List<GroupMember> list = commonService.getActivityMember(activity.getId());
            //获取头像
            ossFileUtils.getUserHeadGroupMemberList(list, null);
            resultMap.put("list", list);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_908.code, ResultCode.CODE_908.msg, resultMap);
    }

    @Override
    public Object updateActivity(String groupName, String address, String area,
                                 String locationName, Double longitude, Double latitude, Long barId) {
        //查询用户当前进行中的群聊
        Group group = commonService.getRunningGroup();
        if (group != null) {
            Map<String, Object> param = new HashMap<>();
            try {
                group.setName(groupName);
                Bar bar = barService.getBarById(barId);

                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                //barId是在字典中，则可以更新
                if (user.getSex().equals("M") && bar != null) {
                    group.setBarId(barId);
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
                }

                //如果是男性群，则更新location
                if (location != null && location.getId() != null) {
                    group.setLocationId(location.getId());
                }
                //如果是女性群，则更新area
                if (user.getSex().equals("F") && checkParaNULL(area)) {
                    param.put("name", area);
                    List<District> districtList = districtService.getAreaByParam(param);

                    //如果找得到该区域，则更新
                    if (districtList != null && districtList.size() != 0) {
                        group.setAreaId(districtList.get(0).getCode());
                    }
                }

                if (groupService.updateGroup(group)) {
                    if (checkParaNULL(groupName)) {

                        //获取该队的队长
                        User leader = commonService.getGroupLeader(group.getId());
                        if (leader != null) {
                            //更新环信群名
                            HuanXinChatGroupService.modifyChatGroup(group.getRoomId(), groupName, "蒲团群聊", 10L);
                        }
                    }
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

            } catch (HuanXinChatGroupException e) {
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
    @Transactional(timeout = 5000)
    public Object quitActivity(Long activityId) {
        logger.info("ActivityControllerImpl quitActivity begin...");
        Activity activity = activityService.getActivityById(activityId);
        User loginUser = userService.getByUserId(shiroService.getLoginUserBase().getId());

        // 取得当前用户的约会信息
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginUser.getUserId());
        param.put("activityId", activityId);

        // 用户约会状态更新为离开
        ActivityMember activityMember = activityMemberService.getActivityMemberByParam(param).get(0);
        activityMember.setStatus(ActivityTypeAndStatus.OUT_ACTIVITY);
        activityMemberService.update(activityMember);

        if (activity != null){
            // 对应约会存在
            param.clear();
            param.put("activityId", activityId);
            param.put("status", ActivityTypeAndStatus.IN_ACTIVITY);
            List<ActivityMember> activityMemberList = activityMemberService.getActivityMemberByParam(param);

            String[] activityMemberAccountList = new String[activityMemberList.size()];
            for(int i = 0; i < activityMemberList.size(); i++){
                activityMemberAccountList[i] = activityMemberList.get(i).getUser().getUserBase().getAccount();
            }

            try {
                if (activityMemberList.size() >= 2){
                    HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                    huanXinMessageExtras.setActivityId(activityId);
                    HuanXinChatGroupService.removeSingleUserFromChatGroup(activity.getRoomId(), loginUser.getUserBase().getAccount());
                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                            loginUser.getNickName() + " 离开约会\n", HuanXinSendMessageType.ADD_OR_QUIT,
                            huanXinMessageExtras, activity.getRoomId());
                }else{
                    //约会群少于2人时，约会群会直接做过期失效处理，后台推送消息给另一个成员

                    activity.setType(ActivityTypeAndStatus.DISSOLUTION);
                    activity.setExpireTime(new Date());
                    activity.setExpireType(1);
                    activityService.updateActivity(activity);
                    //解散约会
                    HuanXinChatGroupService.deleteChatGroup(activity.getRoomId());

                    HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                    huanXinMessageExtras.setRoomId(activity.getRoomId());
                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                            "由于 " + loginUser.getNickName() +
                                    " 退出约会，该方人数低于两人，约会自动解散", HuanXinSendMessageType.ACTIVITY_DISSOLUTION,
                            huanXinMessageExtras, activityMemberAccountList);
                    //step 6.8:发送JPush消息给约会成员知道
//                    JPushUtils.sendActivityJPush("约会群解散", "由于 " + loginUser.getNickName() +
//                            " 退出约会，该方人数低于两人，约会自动解散", JPushType.ACTIVITY_RELATED, null, null, activityMemberAccountList);
                }

            } catch (HuanXinChatGroupException e) {
                logger.error("ActivityControllerImpl quitActivity error" + e);
            } catch (HuanXinMessageException e) {
                logger.error("ActivityControllerImpl quitActivity error" + e);
            }
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    @Override
    public Object searchEvaluate(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity) {

        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            //查询自己要评价的用户列表
            param.put("userId", shiroService.getLoginUserBase().getId());
            //查询未删除的评价
            param.put("isDelete", EvaluateType.NO_DELETE);
            switch (methodType) {
                case 1:
                    List<Evaluate> evaluateList = evaluateService.getEvaluateList(param);
                    for (Evaluate evaluate : evaluateList) {
                        ossFileUtils.getUserHead(evaluate.getEvaluateUser(), null);
                    }
                    resultMap.put("list", evaluateList);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Evaluate obj = evaluateService.getEvaluateById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:

                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Evaluate> pagingForKeyword = evaluateService.getEvaluatePage(pageEntity);
                    for (Evaluate evaluate : pagingForKeyword.getResultList()) {
                        ossFileUtils.getUserHead(evaluate.getEvaluateUser(), null);
                    }
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object evaluate(Long evaluateId, Boolean isLike) {
        if (checkParaNULL(evaluateId, isLike)) {
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                //设置评价状态
                Evaluate evaluate = evaluateService.getEvaluateById(evaluateId);
                if (evaluate == null) {
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_915.code, ResultCode.CODE_915.msg, null);
                }
                //设置是否喜欢
                evaluate.setIsLike(isLike ? EvaluateType.HAVE_EVALUATE_LIKE : EvaluateType.HAVE_EVALUATE_UNLIKE);
                //设置为已删除
                evaluate.setIsDelete(EvaluateType.HAVE_DELETE);
                evaluateService.updateEvaluate(evaluate);

                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                User evaluateUser = userService.getByUserId(evaluate.getEvaluateUserId());

                //如果是喜欢，则查询对方是否也是喜欢
                if (isLike) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("userId", evaluateUser.getUserId());
                    param.put("evaluateUserId", user.getUserId());
                    Evaluate anotherEvaluate = evaluateService.getEvaluate(param);
                    if (anotherEvaluate.getIsLike() == EvaluateType.HAVE_EVALUATE_LIKE) {
                        //添加好友关系
                        List<FriendRelationship> list = new ArrayList<>();
                        FriendRelationship friendRelationship = new FriendRelationship();
                        friendRelationship.setUserId(evaluateUser.getUserId());
                        friendRelationship.setFriendId(user.getUserId());
                        //表示好友的来源是通过约会评价完后自动添加的
                        friendRelationship.setType(FriendType.FROM_ACTIVITY);
                        list.add(friendRelationship);
                        friendRelationship = new FriendRelationship();
                        friendRelationship.setUserId(user.getUserId());
                        friendRelationship.setFriendId(evaluateUser.getUserId());
                        //表示好友的来源是通过约会评价完后自动添加的
                        friendRelationship.setType(FriendType.FROM_ACTIVITY);
                        list.add(friendRelationship);
                        boolean result = friendRelationshipService.addFriendRelationshipBatch(list);

                        //添加jpush推送
                        JPushUtils.sendEvaluateJPush("双方评价成功，成为好友", "由于你与 " + user.getNickName() +
                                        "互相评价成功，所以你们现在已经是好友了",
                                JPushType.ACTIVITY_EVALUATE_FRIEND, evaluateUser.getUserBase().getAccount());

                        JPushUtils.sendEvaluateJPush("双方评价成功，成为好友", "由于你与 " + evaluateUser.getNickName() +
                                        "互相评价成功，所以你们现在已经是好友了",
                                JPushType.ACTIVITY_EVALUATE_FRIEND, user.getUserBase().getAccount());

                    }
                    /** 提价事务 */
                    transactionHelper.commit(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }

            } catch (DuplicateKeyException e) {
                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
                if (e.getMessage().contains("uq_friend_friend_relationship_ref_user")) {
                    System.out.println("\nDuplicateKeyException 已经是好友");
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }


    @Override
    public Object unEvaluateNum() {

        Evaluate evaluate = new Evaluate();
        evaluate.setUserId(shiroService.getLoginUserBase().getId());
        evaluate.setIsDelete(EvaluateType.NO_DELETE);
        //计算用户有几条未评价的消息
        int unEvaluateNum = evaluateService.countEvaluateNum(evaluate);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("unEvaluateNum", unEvaluateNum);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

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

    public boolean userSign(Location location, double distance, Long activityId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("activityId", activityId);
        Sign sign = signService.getSign(param);
        if (sign != null) {
            //之前有签到过，则计算之前签到记录的地点与定点的距离
            double oldDistance = LongitudeAndLatitudeUtils.getDistance(location.getLongitude(), location.getLatitude(),
                    sign.getLocation().getLongitude(), sign.getLocation().getLatitude());
            //如果新距离小于旧距离，则更新location
            if (distance < oldDistance) {
                sign.setLocationId(location.getId());
                return signService.updateSign(sign);
            }
            return false;
        } else {
            //之前没有签到过，则插入签到记录
            sign = new Sign();
            sign.setUserId(shiroService.getLoginUserBase().getId());
            sign.setActivityId(activityId);
            sign.setLocationId(location.getId());
            return signService.addSign(sign);
        }
    }

}
