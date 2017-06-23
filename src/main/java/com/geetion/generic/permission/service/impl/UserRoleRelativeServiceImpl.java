package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.dao.UserRoleRelativeDAO;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.permission.service.UserRoleRelativeService;
import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;
import com.geetion.generic.userbase.pojo.UserBase;
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

/**
 * Created by jian on 2015/6/17.
 */
@Service("geetionUserRoleRelativeService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserRoleRelativeServiceImpl implements UserRoleRelativeService {


    @Resource(name = "geetionUserRoleRelativeDAO")
    private UserRoleRelativeDAO userRoleRelativeDAO;

    @Override
    public boolean add(UserRoleRelative object) {
        return userRoleRelativeDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return userRoleRelativeDAO.delete(id) > 0;
    }

    @Override
    public boolean update(UserRoleRelative object) {
        return userRoleRelativeDAO.update(object) > 0;
    }

    @Override
    public UserRoleRelative getById(Long id) {
        return userRoleRelativeDAO.selectPk(id);
    }

    @Override
    public List<UserRoleRelative> getAll() {
        return userRoleRelativeDAO.select();
    }

    @Override
    public PagingResult<UserRoleRelative> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserRoleRelative> list = userRoleRelativeDAO.selectParam(pageEntity.getParams());
        PageInfo<UserRoleRelative> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<UserRoleRelative> getByParam(Map<String, Object> params) {
        return userRoleRelativeDAO.selectParam(params);
    }

    @Override
    public List<Role> getRoleByUserId(Long userId) {
        return userRoleRelativeDAO.selectRoleByUserId(userId);
    }

    @Override
    public boolean deleteBatchByUserId(Long[] userIds) {
        return userRoleRelativeDAO.deleteBatchByUserId(userIds) > 0;
    }

    @Override
    public boolean deleteRoleBatchByUserId(Long[] userIds, Long roleId) {
        Map<String,Object> params = new HashMap<>();
        params.put("userIds",userIds);
        params.put("roleId",roleId);
        return userRoleRelativeDAO.deleteRoleBatchByUserId(params) > 0;
    }

    @Override
    public boolean addRoleByUserIdBatch(Map<String, Object> params) {
        return userRoleRelativeDAO.insertRoleByUserIdBatch(params) > 0;
    }

    @Override
    public List<UserBase> getAllUserNotRole() {
        return userRoleRelativeDAO.selectAllUserNotRole();
    }

    @Override
    public List<UserBase> getAllUserByRoleId(Long roleId) {
        return userRoleRelativeDAO.selectAllUserByRoleId(roleId);
    }

    @Override
    public PagingResult<UserBase> getAllUserByRoleIdPagination(Long roleId, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserBase> list = getAllUserByRoleId(roleId);
        PageInfo<UserBase> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());
    }

    @Override
    public List<UserBase> getAllUserByRole(String role) {
        return null;
    }
}

