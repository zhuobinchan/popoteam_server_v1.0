package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.ComplainUserDAO;
import com.geetion.puputuan.model.ComplainUser;
import com.geetion.puputuan.service.ComplainUserService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Simon on 2016/8/10.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ComplainUserServiceImpl implements ComplainUserService {

    @Resource
    private ComplainUserDAO complainUserDAO;

    @Override
    public boolean addComplainUser(ComplainUser object) {
        return complainUserDAO.insertSelective(object) == 1;
    }
}
