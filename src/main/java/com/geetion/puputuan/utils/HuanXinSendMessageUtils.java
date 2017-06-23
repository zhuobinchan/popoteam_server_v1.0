package com.geetion.puputuan.utils;

import com.alibaba.fastjson.JSON;
import com.easemob.server.example.api.impl.EasemobSendMessage;
import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.exception.HuanxinResponseException;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.google.gson.GsonBuilder;
import io.swagger.client.model.Msg;
import io.swagger.client.model.MsgContent;
import io.swagger.client.model.UserName;

import java.util.HashMap;
import java.util.Map;

//import com.easemob.server.example.constant.HuanXinConstant;

/**
 * Created by jian on 2016/5/6.
 */
public class HuanXinSendMessageUtils {

    private static EasemobSendMessage easemobSendMessage = new EasemobSendMessage();

    /**
     * 发送环信消息
     *
     * @param targetType           发送的对象，是发给用户还是群
     * @param targets              目标用户，数组表示
     * @param from                 发送者
     * @param msg                  消息内容
     * @param type                 发送的类型 {@link HuanXinSendMessageType}
     * @param huanXinMessageExtras 封装了所有需要发送环信字段
     * @return 是否发送成功
     * @throws HuanXinMessageException
     */
    public static boolean sendMessage(String targetType, String from, String msg, Integer type,
                                      HuanXinMessageExtras huanXinMessageExtras, String... targets) throws HuanXinMessageException {

        msg = EmojiCharacterUtil.emojiRecovery2(msg);
        if (targets.length == 0) {
            return false;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

            }
        }).start();
        Map<String, Object> ext = new HashMap<>();
        if (huanXinMessageExtras == null)
            huanXinMessageExtras = new HuanXinMessageExtras();

        if (from.equals(HuanXinConstant.SYSUSER)) {
            Map<String, Object> emApnsExt = new HashMap<>();

            if (targetType.equals(HuanXinConstant.MESSAGE_USERS)) {
                emApnsExt.put("em_push_title", msg);
            } else {
                emApnsExt.put("em_push_title", "群消息: " + msg);
            }

            ext.put("em_apns_ext", emApnsExt);
        }

        ext.put("type", type.toString() + "");
        ext.put("object", huanXinMessageExtras.toJSONString());


        Msg easemodMsg = new Msg();
        MsgContent msgContent = new MsgContent();
        msgContent.type(MsgContent.TypeEnum.TXT).msg(msg);
        UserName userName = new UserName();
        for (String target: targets ) {
            userName.add(target);
        }
//        Map<String,Object> ext = new HashMap<>();
//        ext.put("test_key","test_value");
        easemodMsg.from(from).target(userName).targetType(targetType).msg(msgContent).ext(ext);
        System.out.println(new GsonBuilder().create().toJson(easemodMsg));
        Object result = null;
        try {
            result = easemobSendMessage.sendMessage(easemodMsg);
        } catch (HuanxinResponseException e) {

        }

        if (result != null) {
            return true;
        } else {
//            System.out.println("\n\nresponseWrapper " + responseWrapper + "\n\n");
//            throw new HuanXinMessageException("发送消息失败");
            return false;
        }

    }


}
