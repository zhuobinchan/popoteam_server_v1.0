package com.geetion.puputuan.utils;

import cn.jpush.api.service.JPushService;
import com.alibaba.fastjson.JSON;
import com.geetion.puputuan.common.constant.JPushType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jian on 2016/5/6.
 */
public class JPushUtils {



    /**
     * 发送好友申请的JPush推送消息
     *
     * @param alias         要发送的用户
     * @param title         发送的标题
     * @param alert         发送的内容
     * @param type          发送的类型，{@link JPushType}
     * @param identify      好友id
     */
    public static void sendAgreeApplyOrNotJPush(String title, String alert, int type, Long identify, String... alias) {

        JPushUtils.sendFriendDeleteJPush(title, alert, type, identify.toString(), alias);
    }

    /**
     * 发送好友申请的JPush推送消息
     *
     * @param alias         要发送的用户
     * @param title         发送的标题
     * @param alert         发送的内容
     * @param type          发送的类型，{@link JPushType}
     * @param friendApplyId 好友申请的id
     */
    public static void sendFriendApplyJPush(String title, String alert, int type, Long friendApplyId, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (friendApplyId != null)
            extras.put("friendApplyId", friendApplyId + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

    /**
     * 发送解除好友关系的JPush推送消息
     *
     * @param alias             要发送的用户
     * @param title             发送的标题
     * @param alert             发送的内容
     * @param type              发送的类型，{@link JPushType}
     * @param friendIdentity    好友的identify
     */
    public static void sendFriendDeleteJPush(String title, String alert, int type, String friendIdentity, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (friendIdentity != null)
            extras.put("friendIdentity", friendIdentity + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

    /**
     * 发送群组相关的JPush推送消息
     *
     * @param alias   要发送的用户
     * @param title   发送的标题
     * @param alert   发送的内容
     * @param type    发送的类型，{@link JPushType}
     * @param groupId 需要带参时的 群组id
     * @param userId  需要带参时的 用户id
     */
    public static void sendGroupJPush(String title, String alert, int type, Long groupId, Long userId, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (groupId != null)
            extras.put("groupId", groupId + "");

        if (userId != null)
            extras.put("userId", userId + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

    /**
     * 发送群组相关的JPush推送消息
     *
     * @param alias       要发送的用户
     * @param title       发送的标题
     * @param alert       发送的内容
     * @param type        发送的类型，{@link JPushType}
     * @param recommendId 推荐的id
     */
    public static void sendMatchJPush(String title, String alert, int type, Long recommendId, String location,Long[] userIds,
                                      Long[] headIds, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (recommendId != null)
            extras.put("recommendId", recommendId + "");
        if (userIds != null)
            extras.put("userIds", JSON.toJSONString(userIds));
        if (headIds != null)
            extras.put("headIds", JSON.toJSONString(headIds));
        if (location != null)
            extras.put("location", location);

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }


    /**
     * 发送约会相关的JPush推送消息
     *
     * @param alias      要发送的用户
     * @param title      发送的标题
     * @param alert      发送的内容
     * @param type       发送的类型，{@link JPushType}
     * @param activityId 需要带参时的 约会id
     * @param userId     需要带参时的 用户id
     */
    public static void sendActivityJPush(String title, String alert, int type, Long activityId, Long userId, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (activityId != null)
            extras.put("activityId", activityId + "");

        if (userId != null)
            extras.put("userId", userId + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }


    /**
     * 发送评价的JPush推送消息
     *
     * @param alias 要发送的用户
     * @param title 发送的标题
     * @param alert 发送的内容
     * @param type  发送的类型，{@link JPushType}
     */
    public static void sendEvaluateJPush(String title, String alert, int type, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

    /**
     * 发送投票的JPush推送消息
     *
     * @param alias 要发送的用户
     * @param title 发送的标题
     * @param alert 发送的内容
     * @param type  发送的类型，{@link JPushType}
     */
    public static void sendVoteJPush(String title, String alert, int type, Long recommendId, Boolean isLike, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (recommendId != null)
            extras.put("recommendId", recommendId + "");
        if (isLike != null)
            extras.put("isLike", isLike + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

    /**
     * 发送透传的JPush推送消息
     *
     * @param alias       要发送的用户
     * @param recommendId 推荐的id
     * @param userIds     用户数组
     * @param heads       头像数组
     * @param alert       发送的内容
     * @param isLike      是否赞成
     * @param type        发送的类型，{@link JPushType}
     */
    public static void sendMessageJPush(String alert, int type, Long recommendId, Long[] userIds,
                                        String[] heads, Boolean isLike, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (recommendId != null)
            extras.put("recommendId", recommendId + "");
        if (userIds != null)
            extras.put("userIds", JSON.toJSONString(userIds));
        if (heads != null)
            extras.put("heads", JSON.toJSONString(heads));
        if (isLike != null)
            extras.put("isLike", isLike + "");
        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOSOnlyMessage(alias, message);
    }

    /**
     * 发送投票结果的透传的JPush推送消息
     *
     * @param alias       要发送的用户
     * @param recommendId 推荐的id
     * @param userId      用户id
     * @param alert       发送的内容
     * @param isLike      是否赞成
     * @param type        发送的类型，{@link JPushType}
     */
    public static void sendVoteMessageJPush(String alert, int type, Long recommendId, Long userId, Boolean isLike, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        if (recommendId != null)
            extras.put("recommendId", recommendId + "");
        if (userId != null)
            extras.put("userId", userId + "");
        if (isLike != null)
            extras.put("isLike", isLike + "");
        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOSOnlyMessage(alias, message);
    }

    /**
     * 发送JPush推送消息 -- 目标用户是 根据标签 tags 获得的
     *
     * @param alert 发送的内容
     * @param title 是否赞成
     * @param type  发送的类型，{@link JPushType}
     * @param tags  目标用户的标签
     */
    public static void sendJPushByTags(String title, String alert, int type, String... tags) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");
        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOSByTags(tags, title, alert, message, extras);
    }

    public static void sendJPushByAccount(String title, String alert, int type, String... alias) {

        Map<String, String> extras = new HashMap<>();
        extras.put("type", type + "");

        //构建JPush的Message对象，添加额外内容和参数
        cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().
                setMsgContent(alert).addExtras(extras).build();

        //发送JPush消息
        JPushService.sendPushAndroidAndIOS(alias, title, alert, message, extras);
    }

}
