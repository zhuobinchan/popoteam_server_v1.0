package cn.jpush.api.service;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.examples.PushExample;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.geetion.puputuan.utils.EmojiCharacterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jian on 2016/4/15.
 */
public class JPushService {

    protected static final Logger LOG = LoggerFactory.getLogger(JPushService.class);

    private static final String appKey = "aaef6d6ef4714bfb90096c03";
    private static final String masterSecret = "67d7bd3e7d42eef77a6a38ab";

    //title 可选，通知标题；如果指定了，则通知里原来展示 App名称的地方，将展示成这个字段。
    //alert 必填，通知内容；这里指定了，则会覆盖上级统一指定的 alert 信息；内容可以为空字符串，则表示不展示到通知栏

    //message 必填，消息内容本身,应用内消息。或者称作：自定义消息，透传消息。
    //此部分内容不会展示到通知栏上，JPush SDK 收到消息内容后透传给 App。App 需要自行处理。


    /**
     * 推送到所有设备所有用户（广播）
     */
    public static void sendPushAllDevice(String alert) {

        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        PushPayload payload = buildPushObject_all_all_alert(alert);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
    }


    /**
     * 推送到 Android和 ios设备的指定用户
     */
    public static void sendPushAndroidAndIOS(String[] alias, String title, String alert, Message message,
                                             Map<String, String> extras) {

        final JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        // 还原转义过后的emoji表情
        alert = EmojiCharacterUtil.emojiRecovery2(alert);
        final PushPayload payload = buildPushObject_android_and_ios(alias, title, alert, message, extras);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PushResult result = jpushClient.sendPush(payload);
                    LOG.info("Got result - " + result);
                } catch (APIConnectionException e) {
                    LOG.error("Connection error. Should retry later. ", e);

                } catch (APIRequestException e) {
                    LOG.error("Error response from JPush server. Should review and fix it. ", e);
                    LOG.info("HTTP Status: " + e.getStatus());
                    LOG.info("Error Code: " + e.getErrorCode());
                    LOG.info("Error Message: " + e.getErrorMessage());
                    LOG.info("Msg ID: " + e.getMsgId());
                }
            }
        }).start();


    }


    /**
     * 推送到 Android和 ios设备的指定用户 -- 透传消息
     */
    public static void sendPushAndroidAndIOSOnlyMessage(String[] alias, Message message) {

        final JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        final PushPayload payload = buildPushObject_all_message(alias, message);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PushResult result = jpushClient.sendPush(payload);
                    LOG.info("Got result - " + result);

                } catch (APIConnectionException e) {
                    LOG.error("Connection error. Should retry later. ", e);

                } catch (APIRequestException e) {
                    LOG.error("Error response from JPush server. Should review and fix it. ", e);
                    LOG.info("HTTP Status: " + e.getStatus());
                    LOG.info("Error Code: " + e.getErrorCode());
                    LOG.info("Error Message: " + e.getErrorMessage());
                    LOG.info("Msg ID: " + e.getMsgId());
                }
            }

        }).start();


    }

    /**
     * 推送到 Android和 ios设备的指定用户 -- 使用tags标签
     */

    public static void sendPushAndroidAndIOSByTags(String[] tags, String title, String alert, Message message,
                                                   Map<String, String> extras) {

        final JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        final PushPayload payload = buildPushObject_tags_messageWithExtras(tags, title, alert, message, extras);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PushResult result = jpushClient.sendPush(payload);
                    LOG.info("Got result - " + result);

                } catch (APIConnectionException e) {
                    LOG.error("Connection error. Should retry later. ", e);

                } catch (APIRequestException e) {
                    LOG.error("Error response from JPush server. Should review and fix it. ", e);
                    LOG.info("HTTP Status: " + e.getStatus());
                    LOG.info("Error Code: " + e.getErrorCode());
                    LOG.info("Error Message: " + e.getErrorMessage());
                    LOG.info("Msg ID: " + e.getMsgId());
                }
            }

        }).start();
    }


    /**
     * 用自定义的config，构建JPushClient对象，推送到所有设备所有用户（广播）
     */
    public static void sendPushWithCustomConfig(String alert) {
        ClientConfig config = ClientConfig.getInstance();
        // Setup the custom hostname
        config.setPushHostName("https://api.jpush.cn");

        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3, null, config);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert(alert);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
    }

    /**
     * 给指定用户设置IOS通知
     */
    public static void sendIosAlert(String title, String body, String alias) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);

        IosAlert alert = IosAlert.newBuilder()
                .setTitleAndBody(title, body)
                .setActionLocKey("PLAY")
                .build();
        try {
            PushResult result = jpushClient.sendIosNotificationWithAlias(alert, new HashMap<String, String>(), alias);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 给指定Android用户发送消息，如果用户在限时前没有接收到，则会发送短信给用户（要收费）
     */
    public static void sendWithSMS(String content, int delayTime, String title, String message, String alias) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            SMS sms = SMS.content(content, delayTime);
            PushResult result = jpushClient.sendAndroidMessageWithAlias(title, message, sms, alias);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
    }


    /**
     * 构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @return
     */
    private static PushPayload buildPushObject_all_all_alert(String alert) {
        return PushPayload.alertAll(alert);
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。
     *
     * @return
     */
    private static PushPayload buildPushObject_all_alias_alert(String[] alias, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(alert))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @return
     */
    private static PushPayload buildPushObject_android_tag_alertWithTitle(String tag, String title, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }


    /**
     * 构建推送对象：平台是 Android 跟 iOS，目标是 tag 为 "tag1" 的设备，通知 ALERT，并且Android标题为 TITLE。
     * 并且附加字段 from = "JPush"；
     *
     * @return
     */
    private static PushPayload buildPushObject_android_and_ios(String[] alias, String title, String alert, Message message,
                                                               Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build())
                .setMessage(message)
                .build();
    }


    /**
     * 构建推送对象：平台是 Android 跟 iOS，透传消息，不在通知栏显示
     *
     * @return
     */
    private static PushPayload buildPushObject_all_message(String[] alias, Message message) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setMessage(message)
                .build();
    }

    /**
     * 构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 -
     * 通知信息是 ALERT，角标数字为 5，通知声音为 "happy"，并且附加字段 from = "JPush"；
     * 消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。
     * APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @return
     */
    private static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String[] tags, String message, int badge,
                                                                                    String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .setBadge(badge)
                                .setSound("happy")
                                .addExtra(extraKey, extraValue)
                                .build())
                        .build())
                .setMessage(Message.content(message))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）且（"alias1" 与 "alias2" 的并集），
     * 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
     *
     * @return
     */
    private static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String[] tags, String[] alias,
                                                                                  String message, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tags))
                        .addAudienceTarget(AudienceTarget.alias(alias))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(message)
                        .addExtra(extraKey, extraValue)
                        .build())
                .build();
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）
     * 推送内容是 - 内容为 alert 的消息，并且附加字段 message。
     *
     * @return
     */
    private static PushPayload buildPushObject_tags_messageWithExtras(String[] tags, String title, String alert,
                                                                      Message message, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tags))
                        .build())
                .setNotification(Notification.newBuilder()
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build())
                .setMessage(message)
                .build();
    }
}
