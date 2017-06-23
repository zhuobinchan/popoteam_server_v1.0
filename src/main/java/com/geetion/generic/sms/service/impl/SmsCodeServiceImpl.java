package com.geetion.generic.sms.service.impl;


import com.geetion.generic.sms.dao.SmsCodeDAO;
import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.service.SmsCodeService;
import com.geetion.generic.sms.utils.mybatis.PageEntity;
import com.geetion.generic.sms.utils.mybatis.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class SmsCodeServiceImpl implements SmsCodeService {

    @Resource
    private SmsCodeDAO smsCodeDAO;

    @Override
    public SmsCode getSmsCodeById(Long id) {
        return smsCodeDAO.selectPk(id);
    }

    @Override
    public List<SmsCode> getSmsCodeList(Map param) {
        return smsCodeDAO.selectParam(param);
    }

    @Override
    public PagingResult<SmsCode> getSmsCodePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<SmsCode> list = getSmsCodeList(pageEntity.getParams());
        PageInfo<SmsCode> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addSmsCode(SmsCode object) {
        return smsCodeDAO.insert(object) == 1;
    }

    @Override
    public boolean updateSmsCode(SmsCode object) {
        return smsCodeDAO.update(object) == 1;
    }

    @Override
    public boolean removeSmsCode(Long id) {
        return smsCodeDAO.delete(id) == 1;
    }

    @Override
    public SmsCode getSmsCode(Map param) {
        return smsCodeDAO.selectOne(param);
    }
}
