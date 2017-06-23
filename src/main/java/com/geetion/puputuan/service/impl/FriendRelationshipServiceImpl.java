package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.FriendRelationshipDAO;
import com.geetion.puputuan.model.FriendRelationship;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.UserWithCount;
import com.geetion.puputuan.service.FriendRelationshipService;
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
public class FriendRelationshipServiceImpl implements FriendRelationshipService {

    @Resource
    private FriendRelationshipDAO friendRelationshipDAO;

    @Override
    public FriendRelationship getFriendRelationshipById(Long id) {
        return friendRelationshipDAO.selectByPrimaryKey(id);
    }

    @Override
    public FriendRelationship getFriendRelationship(Map param) {
        return friendRelationshipDAO.selectOne(param);
    }

    @Override
    public List<FriendRelationship> getFriendRelationshipList(Map param) {
        return friendRelationshipDAO.selectParam(param);
    }

    @Override
    public PagingResult<FriendRelationship> getFriendRelationshipPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<FriendRelationship> list = getFriendRelationshipList(pageEntity.getParam());
        PageInfo<FriendRelationship> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addFriendRelationship(FriendRelationship object) {
        return friendRelationshipDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateFriendRelationship(FriendRelationship object) {
        return friendRelationshipDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFriendRelationship(Long id) {
        return friendRelationshipDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addFriendRelationshipBatch(List<FriendRelationship> list) {
        return friendRelationshipDAO.insertBatch(list) == list.size();
    }

    @Override
    public List<User> getFriendRelationshipWithUser(Map param) {
        return friendRelationshipDAO.selectParamWithUser(param);
    }


    @Override
    public List<User> getUserOrderByFirstPinyin(Map param) {
        return friendRelationshipDAO.selectParamWithUserOrderByFirstPinyin(param);
    }

    @Override
    public PagingResult<User> getWithUserOrderByFirstPinyinPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<User> list = getUserOrderByFirstPinyin(pageEntity.getParam());
        PageInfo<User> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public PagingResult<User> getFriendRelationshipWithUserPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<User> list = getFriendRelationshipWithUser(pageEntity.getParam());
        PageInfo<User> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean removeFriendRelationshipBetween(Map param) {
        return friendRelationshipDAO.deleteBetween(param) > 0;
    }

    @Override
    public List<Long> getFriendIds(List<Long> ids) {
        return friendRelationshipDAO.selectFriendId(ids);
    }

    @Override
    public List<UserWithCount> getFriendDetailList(Map param) {
        return friendRelationshipDAO.selectFriendDetail(param);
    }

    @Override
    public PagingResult<UserWithCount> getFriendDetailPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserWithCount> list = getFriendDetailList(pageEntity.getParam());
        PageInfo<UserWithCount> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }
}
