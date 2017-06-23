package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.UserSettingDAO;
import com.geetion.puputuan.model.jsonModel.UserSetting;
import com.geetion.puputuan.service.UserSettingService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Simon on 2016/8/3.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserSettingServiceImpl implements UserSettingService {

    @Resource
    private UserSettingDAO userSettingDAO;

    @Override
    public UserSetting getSettingById(Long id) {
        return userSettingDAO.selectByPrimaryKey(id);
    }

    @Override
    public UserSetting getSetting(Map param) {
        return userSettingDAO.selectOne(param);
    }

    @Override
    public boolean addSetting(UserSetting object) {
        return userSettingDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateSetting(UserSetting object) {
        return userSettingDAO.updateByPrimaryKeySelective(object) == 1;
    }
}
