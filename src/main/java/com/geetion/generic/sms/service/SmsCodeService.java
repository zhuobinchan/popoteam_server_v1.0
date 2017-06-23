package com.geetion.generic.sms.service;


import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.utils.mybatis.PageEntity;
import com.geetion.generic.sms.utils.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

public interface SmsCodeService {

    SmsCode getSmsCodeById(Long id);

    List<SmsCode> getSmsCodeList(Map param);

    PagingResult<SmsCode> getSmsCodePage(PageEntity pageEntity);

    boolean addSmsCode(SmsCode object);

    boolean updateSmsCode(SmsCode object);

    boolean removeSmsCode(Long id);


    /**
     * 查询一个
     * @param param
     * @return
     */
    public SmsCode getSmsCode(Map param);
}
