package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.UserFavouritePicDAO;
import com.geetion.puputuan.model.jsonModel.UserFavouritePic;
import com.geetion.puputuan.service.UserFavouritePicService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2016/8/4.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserFavouritePicServiceImpl implements UserFavouritePicService {

    @Resource
    private UserFavouritePicDAO userFavouritePicDAO;

    @Override
    public UserFavouritePic getFavouritePicById(Long id) {
        return userFavouritePicDAO.selectByPrimaryKey(id);
    }

    @Override
    public UserFavouritePic getFavouritePic(Map param) {
        return userFavouritePicDAO.selectOne(param);
    }

    @Override
    public List<UserFavouritePic> getFavouritePicList(Map param) {
        return userFavouritePicDAO.selectParam(param);
    }

    @Override
    public boolean addFavouritePic(UserFavouritePic object) {
        return userFavouritePicDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateFavouritePic(UserFavouritePic object) {
        return userFavouritePicDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFavouritePicById(Long id) {
        return userFavouritePicDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean removeFavouritePicByUserId(Long userId) {
        return userFavouritePicDAO.deleteByUserId(userId) > 0;
    }

    @Override
    public boolean addFavouritePicBatch(Map param) {
        return userFavouritePicDAO.insertBatch(param) > 0;
    }

    @Override
    public boolean updateFavouritePicBatch(Map param) {
        return userFavouritePicDAO.updateBatch(param) > 0;
    }
}
