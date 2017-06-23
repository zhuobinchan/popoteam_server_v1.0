package com.geetion.generic.permission.service;


import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.userbase.pojo.UserBase;

import java.util.List;
import java.util.Map;

/**
 * 权限服务，包含了所有跟权限相关的API
 * Created by jian on 2015/6/17.
 */
public interface AuthorityService {


    /**
     * 获得用户所有角色
     *
     * @param userId 用户id
     * @return
     */
    public List<Role> getAllRoleByUser(Long userId);

    /**
     * 为用户设置角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return true表示设置成功，false表示设置失败
     */
    public boolean setRoleForUser(Long userId, Long roleId);

    /**
     * 为用户设置角色
     *
     * @param userIds 用户id
     * @param roleId  角色id
     * @return true表示设置成功，false表示设置失败
     */
    public boolean addRoleByUserIdBatch(Long[] userIds, Long roleId);

    /**
     * 删除用户的某个角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return true表示删除成功，false表示删除失败
     */
    public boolean removeRoleFromUser(Long userId, Long roleId);

    /**
     * 获得用户所有权限
     *
     * @param userId 用户id
     * @return
     */
    public List<Permission> getAllPermissionByUser(Long userId);


    /**
     * 查询所有包含该权限的角色
     *
     * @param permissionId
     * @return
     */
    public List<Role> getAllRoleContainPermission(Long permissionId);


    /**
     * 查询该角色包含的所有权限
     *
     * @param roleId
     * @return
     */
    public List<Permission> getAllPermissionBelongRole(Long roleId);

    /**
     * 查询所有需要权限的url
     *
     * @return
     */
    public Map<String, String> getUrlPermissionMap();


    /**
     * 新增角色
     *
     * @param role
     * @param name
     * @return
     */
    public boolean addRole(String role, String name);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    public boolean addRole(Role role);


    /**
     * 根据id获取角色
     *
     * @param roleId
     * @return
     */
    public Role getRoleById(Long roleId);


    /**
     * 新增权限
     *
     * @param permission
     * @param name
     * @return
     */
    public boolean addPermission(String permission, String name);


    /**
     * 批量为角色添加权限
     *
     * @param permissionIds
     * @param roleId
     * @return
     */
    public boolean addPermissionToRole(Long[] permissionIds, Long roleId);


    /**
     * 批量删除角色权限
     *
     * @param permissionIds
     * @param roleId
     * @return
     */
    public boolean removePermissionFromRole(Long[] permissionIds, Long roleId);


    /**
     * 设置该路径需要什么角色才能访问
     *
     * @return
     */
    public boolean setRoleUrl(Long roleId, String url);


    /**
     * 设置该路径需要什么权限才能访问
     *
     * @return
     */
    public boolean setPermissionUrl(Long permissionId, String url);


    /**
     * 根据role名字查询角色
     *
     * @param role
     * @return 该用户信息
     */
    public Role getByRole(String role);


    /**
     * 批量删除用户与角色关系（删掉用户所有角色）
     *
     * @param userIds
     * @return
     */
    public boolean deleteBatchByUserId(Long[] userIds);

    /**
     * 批量删除用户与角色关系（删掉用户对应角色）
     *
     * @param userIds
     * @return
     */
    public boolean deleteRoleBatchByUserId(Long[] userIds, Long roleId);


    /**
     * 获取没有角色的用户
     *
     * @return
     */
    public List<UserBase> getAllUserNotRole();


    /**
     * 根据roleId获取该角色底下的所有用户
     *
     * @param roleId
     * @return
     */
    public List<UserBase> getAllUserByRoleId(Long roleId);


    /**
     * 根据role获取该角色底下的所有用户
     *
     * @param role
     * @return
     */
    public List<UserBase> getAllUserByRole(String role);



}

