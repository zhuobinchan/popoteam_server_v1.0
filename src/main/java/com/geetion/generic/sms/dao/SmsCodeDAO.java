package com.geetion.generic.sms.dao;

import com.geetion.generic.sms.dao.base.BaseDAO;
import com.geetion.generic.sms.pojo.SmsCode;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SmsCodeDAO extends BaseDAO<SmsCode, Long> {


    /**
     * 查询一个
     * @param param
     * @return
     */
    public SmsCode selectOne(Map param);
}