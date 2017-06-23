package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.SysDicDAO;
import com.geetion.puputuan.model.SysDic;
import com.geetion.puputuan.service.SysDicService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class SysDicServiceImpl implements SysDicService {

    @Resource
    private SysDicDAO sysDicDAO;

    @Override
    public SysDic getSysDicByKey(String key) {
        return sysDicDAO.selectByKey(key);
    }

    public void updateSysDic(SysDic sysDic){
        sysDicDAO.updateByPrimaryKeySelective(sysDic);
    }
}
