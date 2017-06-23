package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.dao.RolePermissionRelativeDAO;
import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.RolePermissionRelative;
import com.geetion.generic.permission.service.RolePermissionRelativeService;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
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
@Service("geetionRolePermissionRelativeService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class RolePermissionRelativeServiceImpl implements RolePermissionRelativeService {


    @Resource(name = "geetionRolePermissionRelativeDAO")
    private RolePermissionRelativeDAO rolePermissionRelativeDAO;

    @Override
    public boolean add(RolePermissionRelative object) {
        return rolePermissionRelativeDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return rolePermissionRelativeDAO.delete(id) > 0;
    }

    @Override
    public boolean update(RolePermissionRelative object) {
        return rolePermissionRelativeDAO.update(object) > 0;
    }

    @Override
    public RolePermissionRelative getById(Long id) {
        return rolePermissionRelativeDAO.selectPk(id);
    }

    @Override
    public List<RolePermissionRelative> getAll() {
        return rolePermissionRelativeDAO.select();
    }

    @Override
    public PagingResult<RolePermissionRelative> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<RolePermissionRelative> list = rolePermissionRelativeDAO.selectParam(pageEntity.getParam());
        PageInfo<RolePermissionRelative> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<RolePermissionRelative> getByParam(Map<String, Object> params) {
        return rolePermissionRelativeDAO.selectParam(params);
    }


    @Override
    public List<Role> getAllRoleContainPermission(Permission permission) {
        return rolePermissionRelativeDAO.selectAllRoleContainPermission(permission);
    }

    @Override
    public List<Permission> getAllPermissionBelongRole(Role role) {
        return rolePermissionRelativeDAO.selectAllPermissionBelongRole(role);
    }

    @Override
    public List<Permission> getAllPermissionByRoleId(Long roleId) {
        Role tempRole = new Role();
        tempRole.setId(roleId);
        return getAllPermissionBelongRole(tempRole);
    }

    @Override
    public PagingResult<Permission> getAllPermissionByRoleIdPagination(Long roleId, PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Permission> list = getAllPermissionByRoleId(roleId);
        PageInfo<Permission> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());
    }

    @Override
    public boolean addPermissionToRole(Long[] permissionIds, Long roleId) {
        Map<String,Object> params = new HashMap<>();
        params.put("list",permissionIds);
        params.put("roleId",roleId);
        return rolePermissionRelativeDAO.insertPermissionToRole(params) > 0;
    }

    @Override
    public boolean removePermissionFromRole(Long[] permissionIds, Long roleId) {
        Map<String,Object> params = new HashMap<>();
        params.put("list",permissionIds);
        params.put("roleId",roleId);
        return rolePermissionRelativeDAO.deletePermissionFromRole(params) > 0;
    }
}

