package com.geetion.puputuan.web.api.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/sms")
public interface SmsController {



    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    Object sendSms(String account,Integer type, String prex);


}
