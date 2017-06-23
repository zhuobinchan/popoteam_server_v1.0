package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.FancyUserDAO;
import com.geetion.puputuan.model.FancyUser;
import com.geetion.puputuan.service.FancyUserService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class FancyUserServiceImpl implements FancyUserService {

    @Resource
    private FancyUserDAO fancyUserDAO;

    private Map<String, Object> params;



    @Override
    public boolean addFancyUser(FancyUser fancyUser) {
        return fancyUserDAO.insertSelective(fancyUser) == 1;
    }

    @Override
    public boolean removeFancyUserByUserId(Long userId) {
        return fancyUserDAO.deleteByUserId(userId) > 0;
    }
}
