package com.geetion.puputuan.service.impl;

import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.JobDAO;
import com.geetion.puputuan.dao.UserBlackListDAO;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.model.UserBlackList;
import com.geetion.puputuan.service.JobService;
import com.geetion.puputuan.service.UserBlackListService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class UserBlackListServiceImpl implements UserBlackListService {


    @Resource
    private UserBlackListDAO userBlackListDAO;

    @Override
    public boolean checkIfInBL(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", 1);

        UserBlackList userBlackList = userBlackListDAO.selectOne(params);
        return userBlackList != null ;
    }

    @Override
    public UserBlackList getUserBlackListById(Long id) {
        return userBlackListDAO.selectByPrimaryKey(id);
    }

    @Override
    public UserBlackList getUserBlackList(Map param) {
        return userBlackListDAO.selectOne(param);
    }

    @Override
    public List<UserBlackList> getUserBlackListList(Map param) {
        return userBlackListDAO.selectParam(param);
    }

    @Override
    public PagingResult<UserBlackList> getUserBlackListPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserBlackList> list = getUserBlackListList(pageEntity.getParam());
        PageInfo<UserBlackList> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean updateUserBlackList(UserBlackList userBlackList) {
        return userBlackListDAO.updateByPrimaryKeySelective(userBlackList) == 1;
    }

    @Override
    public boolean removeUserBlackList(Long id) {
        return userBlackListDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addUserBlackLists(Map map) {
        return userBlackListDAO.insertUserBlackLists(map)>=0;
    }

    @Override
    public boolean removeBlackLists(Map map) {
        return userBlackListDAO.deleteUserBlackLists(map)>=0;
    }
}

