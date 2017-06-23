package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.constant.UserLikeGroupType;
import com.geetion.puputuan.dao.UserLikeGroupDAO;
import com.geetion.puputuan.dao.UserSuperLikeDAO;
import com.geetion.puputuan.model.UserLikeGroup;
import com.geetion.puputuan.model.UserSuperLike;
import com.geetion.puputuan.service.UserLikeGroupService;
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
public class UserLikeGroupServiceImpl implements UserLikeGroupService {

    @Resource
    private UserLikeGroupDAO userLikeGroupDAO;


    @Override
    public List<UserLikeGroup> getUserLikeGroupList(Map params) {
        return userLikeGroupDAO.selectParam(params);
    }

    @Override
    public boolean addUserLikeGroupLike(List<UserLikeGroup> list) {
        for(UserLikeGroup t : list){
            userLikeGroupDAO.insertSelective(t);
        }
        return true;
    }

    @Override
    public void setUserLikeGroupToInvalid(Long groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("isLike", UserLikeGroupType.Invalid);
        userLikeGroupDAO.updateToInvalid(params);
    }

}
