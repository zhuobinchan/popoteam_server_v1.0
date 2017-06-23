package com.task;

import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.service.MsgService;
import com.geetion.generic.sms.service.SmsCodeService;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guodikai on 2017/4/11.
 */
public class ReSendSMS {
    private final Logger logger = Logger.getLogger(ReSendSMS.class);
    private final int RESEND_TIMES = 1;
    @Resource(name = "redisTemplate")
    private RedisTemplate<Serializable, Serializable> redisTemplate;
    @Resource
    private SmsCodeService smsCodeService;

    public void excute(){
        logger.info("====================== ReSendSMS excute ======================");
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        byte[] repeatSend = serializer.serialize(RedisKeyAndLock.SMS_REPEAT_SEND);

        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            @Transactional
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

                String reSend = serializer.deserialize(connection.rPop(repeatSend));
                if (null != reSend){
                    String[] strings = reSend.split(":");

                    String times = serializer.deserialize(connection.get(serializer.serialize(strings[0])));

                    if (null != times && Integer.valueOf(times) > RESEND_TIMES){
                        logger.info("====================== ReSendSMS exceeded retry count ======================");
                        connection.del(serializer.serialize(strings[0]));
                        // 只进行一次重发
                        return true;
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("account", "+86" + strings[0]);
                    SmsCode smsCode = smsCodeService.getSmsCode(params);

                    logger.info("====================== ReSendSMS sendMsg  ======================");
                    logger.info("====================== phone " + strings[0]);
                    logger.info("====================== template " + strings[1]);
                    MsgService.sendMsg(strings[0], smsCode.getCode(), strings[1]);
                }
                return true;
            }}
        );
        logger.info("====================== ReSendSMS end ======================");
    }
}
