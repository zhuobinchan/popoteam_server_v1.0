package com.geetion.puputuan.web.api.app.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.sms.constants.SmsTemplate;
import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.service.MsgService;
import com.geetion.generic.sms.service.SmsCodeService;
import com.geetion.puputuan.common.constant.SmsTemplateType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.web.api.app.SmsController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class SmsControllerImpl extends BaseController implements SmsController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private SmsCodeService smsCodeService;
    @Resource
    private UserService userService;

    @Override
    @Transactional(timeout = 5000)
    public Object sendSms(String account, Integer type, String prex) {
        logger.info("SmsControllerImpl searchBar begin...");
        if (checkParaNULL(account, type)) {
            // 号码前缀，如果传null，默认为+86
            if(null == prex){
                prex = "+86";
            }

            Map<String, Object> param = new HashMap<>();
            String template = "";

            switch (type){
                case SmsTemplateType.LOGIN:
                    template = SmsTemplate.Common_Verification;
                    break;
                case SmsTemplateType.REGISTER:
                    template = SmsTemplate.Common_Verification;
                    break;
                case SmsTemplateType.RESETPSW:
                    template = SmsTemplate.Common_Verification;

                    param.put("phone", prex + account);
                    User user = userService.getUser(param);
                    if(null == user){
                        // 手机号码未注册，提示
                        return sendResult(ResultCode.CODE_711.code, ResultCode.CODE_711.msg, null);
                    }
                    break;
                default:
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }

            param.clear();
            param.put("account", prex + account);
            SmsCode smsCode = smsCodeService.getSmsCode(param);

            boolean result = false;
            if (smsCode != null) {

                smsCode.setCode("" + getRandom());
                smsCode.setUpdateTime(new Date());
                smsCode.setValidMillisecond(0l);
                smsCode.setType(SmsTemplateType.NO_USE);

                //更新验证码
                result = smsCodeService.updateSmsCode(smsCode);

            } else {
                smsCode = new SmsCode();
                smsCode.setAccount(prex + account);
                smsCode.setCode("" + getRandom());
                smsCode.setUpdateTime(new Date());
                smsCode.setValidMillisecond(0l);
                smsCode.setType(SmsTemplateType.NO_USE);

                //添加验证码
                result = smsCodeService.addSmsCode(smsCode);
            }


            if (result) {
                // 发短信不能加+86前缀
                boolean sendSmsResult = MsgService.sendMsg(account, smsCode.getCode(), template);
                if (sendSmsResult) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_805.code, ResultCode.CODE_805.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    /**
     * 生成指定范围的随机整数
     *
     * @return
     */
    public int getRandom() {
        //最大数
        int max = 9999;
        //最小数
        int min = 1000;
        Random random = new Random();
        //random.nextInt(max)表示生成[0,max]之间的随机数，然后对(max-min+1)取模。再加上最小数，则得到指定范围内的随机数
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
