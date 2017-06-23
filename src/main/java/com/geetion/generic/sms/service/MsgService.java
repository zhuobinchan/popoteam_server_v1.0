package com.geetion.generic.sms.service;


//引入阿里大鱼SDK

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geetion.generic.sms.config.SmsConfig;
import com.geetion.generic.sms.constants.SmsTemplate;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import com.geetion.puputuan.model.jsonModel.JSONUser;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by jian on 2016/4/27.
 */
public class MsgService {


    /**
     * AlibabaAliqinFcSmsNumSendRequest 参数详解
     * (1) extend:可选，公共回传参数，在“消息返回”中会透传回该参数；
     * 举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
     * (2) recNum:必填，短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。
     * 群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
     * (3) smsFreeSignName:必填，短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。
     * 如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。
     * 短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
     * (4) smsParam:可选，短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
     * 示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
     * (5) smsTemplateCode:必填，短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
     * (6) smsType:必填，短信类型，传入值请填写normal
     * 具体的查看：{ @link http://open.taobao.com/doc2/apiDetail?apiId=25450#s1}
     */

    public static boolean sendMsg(String number, String code, String template) {

        try {

            //请填写自己的app key,app secret
            TaobaoClient client = new DefaultTaobaoClient(SmsConfig.URL, SmsConfig.APP_KEY, SmsConfig.APP_SECRET);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

            //公共回传参数
            //req.setExtend("123456");
            req.setSmsType(SmsConfig.SMS_TYPE);
            //短信签名
            req.setSmsFreeSignName(SmsConfig.SMS_FREE_SIGN_NAME);
            //短信接收号码
            req.setRecNum(number);
            //短信模板变量
            req.setSmsParamString(SmsTemplate.setSmsParamString(template, code, SmsConfig.PRODUCT, null));
            //短信模板ID
            req.setSmsTemplateCode(template);
            //执行发送短信功能
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

            //获得返回的数据
            String responseString = rsp.getBody();

            //将返回的信息解析成JSON
            JSONObject responseJSONObject = JSON.parseObject(responseString);

            //如果发送成功、则存在 alibaba_aliqin_fc_sms_num_send_response 字段 ，send_response不为空
            JSONObject sendResponseJSONObject = responseJSONObject.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");

            if (sendResponseJSONObject != null) {
                /**
                 * 发送成功返回
                 * {"alibaba_aliqin_fc_sms_num_send_response":
                 * {"result":{"err_code":"0","model":"101408862482^1101929157807","success":true},"request_id":"qm4e0kfsyj83"}}
                 */
                JSONObject result = sendResponseJSONObject.getJSONObject("result");
//                Boolean success = (boolean) result.get("success");
                return true;
            } else {
                /**
                 * 发送失败返回
                 * {"error_response":{"code":15,"msg":"Remote service error",
                 * "sub_code":"isv.BUSINESS_LIMIT_CONTROL","sub_msg":"触发业务流控","request_id":"z2a4k8vfs4s6"}}
                 */
                JSONObject errorResponse = responseJSONObject.getJSONObject("error_response");
                String subMsg = (String) errorResponse.get("sub_msg");
                System.out.println("\n 短信发送失败，错误码 " + subMsg);
                String subCode = (String) errorResponse.get("sub_code");
                System.out.println("\n 短信发送失败，错误描述 " + subCode);
                MsgService.reSend(number, template);
                return false;
            }

        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void subscribeMsg() {

        TmcClient client = new TmcClient(SmsConfig.APP_KEY, SmsConfig.APP_SECRET, "default"); // 关于default参考消息分组说明
        client.setMessageHandler(new MessageHandler() {
            public void onMessage(Message message, MessageStatus status) {


                Map<String, String> content = (Map<String, String>) JSON.parse(message.getContent());
                String errCode = content.get("err_code");
                String receiver = content.get("receiver");
                String template = content.get("sms_code");
                //发送失败时，进行重发
                if(errCode.equals("DELIVRD")){
                    MsgService.cleanResendTimes(receiver);
                }else {
                    MsgService.reSend(receiver, template);
                }

                try {
                    System.out.println(message.getContent());
                    System.out.println(message.getTopic());
                } catch (Exception e) {
                    e.printStackTrace();
                    status.fail(); // 消息处理失败回滚，服务端需要重发
                    // 重试注意：不是所有的异常都需要系统重试。
                    // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
                    // 对于，由于网络问题，权限问题导致的失败，可重试。
                    // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
                }
            }
        });

        try {
            client.connect("ws://mc.api.taobao.com"); // 消息环境地址：ws://mc.api.tbsandbox.com/
        } catch (LinkException e) {
            System.out.println(e.getStackTrace());
        }
    }

    /**
     * 将发送失败的短信放入redis队列，等待后台进程进行重发
     * @param phone
     * @param template
     */
    private static void reSend(String phone, String template){

        RedisTemplate<Serializable, Serializable> redisTemplate = SpringLoader.getInstance().getBean(RedisTemplate.class);

        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        byte[] listKey = serializer.serialize(RedisKeyAndLock.SMS_REPEAT_SEND);
        byte[] sms = serializer.serialize(phone + ":" + template);

        byte[] phoneByte = serializer.serialize(phone);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            @Transactional
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.rPush(listKey, sms);
                connection.setNX(phoneByte, serializer.serialize("0"));
                connection.incr(phoneByte);
                return true;
            }}
        );
    }

    private static void cleanResendTimes(String phone){
        RedisTemplate<Serializable, Serializable> redisTemplate = SpringLoader.getInstance().getBean(RedisTemplate.class);
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        byte[] phoneByte = serializer.serialize(phone);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            @Transactional
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(phoneByte);
                return true;
            }}
        );
    }
    public static void main(String[] args) {

        sendMsg("13430224502", "1243", SmsTemplate.User_registration);

    }
}
