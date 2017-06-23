package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.SysDicDAO;
import com.geetion.puputuan.dao.UserSuperLikeDAO;
import com.geetion.puputuan.model.SysDic;
import com.geetion.puputuan.model.UserSuperLike;
import com.geetion.puputuan.service.SysDicService;
import com.geetion.puputuan.service.UserSuperLikeService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserSuperLikeServiceImpl implements UserSuperLikeService {

    @Resource
    private UserSuperLikeDAO userSuperLikeDAO;

    @Override
    public List<UserSuperLike> getTodaySuperLike(Long userId) {
        return userSuperLikeDAO.selectTodaySuperLike(userId);
    }

    @Override
    public boolean addUserSuperLike(UserSuperLike object) {
        return userSuperLikeDAO.insertSelective(object) == 1;
    }

    @Override
    public List<UserSuperLike> getSuperLikeByParam(Map param) {
        return userSuperLikeDAO.selectParam(param);
    }

    @Override
    public void deleteUserSuperLike(Map param) {
        userSuperLikeDAO.deleteSuperLike(param);
    }


}
