package com.geetion.generic.permission.service;


import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.RolePermissionRelative;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
public interface RolePermissionRelativeService {

    /**
     * 添加用户
     *
     * @param object
     * @return 是否添加成功
     */
    public boolean add(RolePermissionRelative object);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return 是否删除成功
     */
    public boolean remove(Long id);

    /**
     * 修改用户信息
     *
     * @param object
     * @return 是否修改成功
     */
    public boolean update(RolePermissionRelative object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return 该用户信息
     */
    public RolePermissionRelative getById(Long id);

    /**
     * 不分页查询全部用户
     *
     * @return 全部用户信息
     */
    public List<RolePermissionRelative> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     * @return 分页后的信息
     */
    public PagingResult<RolePermissionRelative> getByPagination(PageEntity pageEntity);

    /**
     * 根据条件查询用户
     *
     * @param params
     * @return 查询后的用户信息
     */
    public List<RolePermissionRelative> getByParam(Map<String, Object> params);


    /**
     * 查询所有包含该权限的角色
     * @param permission
     * @return
     */
    public List<Role> getAllRoleContainPermission(Permission permission);


    /**
     * 查询该角色包含的所有权限
     * @param role
     * @return
     */
    public List<Permission> getAllPermissionBelongRole(Role role);

    /**
     * 根据角色id获取权限
     * @param roleId
     * @return
     */
    List<Permission> getAllPermissionByRoleId(Long roleId);

    /**
     * 分页根据角色id获取权限
     * @param roleId
     * @return
     */
    PagingResult<Permission> getAllPermissionByRoleIdPagination(Long roleId, PageEntity pageEntity);

    /**
     * 批量为角色添加权限
     * @param
     * @return
     */
    public boolean addPermissionToRole(Long[] permissionIds, Long roleId);

    /**
     * 批量删除角色权限
     * @param
     * @return
     */
    public boolean removePermissionFromRole(Long[] permissionIds, Long roleId);

}

