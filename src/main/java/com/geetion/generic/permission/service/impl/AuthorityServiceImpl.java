package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.PermissionUrl;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.permission.service.*;
import com.geetion.generic.userbase.pojo.UserBase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 2015/6/17.
 */
@Service("geetionAuthorityService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class AuthorityServiceImpl implements AuthorityService {


    @Resource(name = "geetionRoleService")
    private RoleService roleService;

    @Resource(name = "geetionPermissionService")
    private PermissionService permissionService;

    @Resource(name = "geetionUserRoleRelativeService")
    private UserRoleRelativeService userRoleRelativeService;

    @Resource(name = "geetionRolePermissionRelativeService")
    private RolePermissionRelativeService rolePermissionRelativeService;

    @Resource(name = "geetionPermissionUrlService")
    private PermissionUrlService permissionUrlService;

    //参数列表
    Map<String, Object> params;

    @Override
    public List<Role> getAllRoleByUser(Long userId) {

        return userRoleRelativeService.getRoleByUserId(userId);

    }

    @Override
    public boolean setRoleForUser(Long userId, Long roleId) {

        //先查询是否用户已经有了该角色
        params = new HashMap<>();
        params.put("userId", userId);
        params.put("roleId", roleId);
        List<UserRoleRelative> userRoleRelativeList = userRoleRelativeService.getByParam(params);

        if (userRoleRelativeList != null && userRoleRelativeList.size() != 0) {
            return false;
        }

        //用户没有该角色时，执行插入操作
        UserRoleRelative userRoleRelative = new UserRoleRelative();
        userRoleRelative.setUserId(userId);
        userRoleRelative.setRoleId(roleId);

        if (userRoleRelativeService.add(userRoleRelative)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addRoleByUserIdBatch(Long[] userIds, Long roleId) {

        params = new HashMap<>();
        params.put("userIds", userIds);
        params.put("roleId", roleId);

        return userRoleRelativeService.addRoleByUserIdBatch(params);

    }

    @Override
    public boolean removeRoleFromUser(Long userId, Long roleId) {

        params = new HashMap<>();
        params.put("userId", userId);
        params.put("roleId", roleId);
        List<UserRoleRelative> geetionRoleList = userRoleRelativeService.getByParam(params);

        if (geetionRoleList != null && geetionRoleList.size() != 0) {
            for (int i = 0; i < geetionRoleList.size(); i++) {
                userRoleRelativeService.remove(geetionRoleList.get(i).getId());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Permission> getAllPermissionByUser(Long userId) {

        List<Role> roleList = userRoleRelativeService.getRoleByUserId(userId);
        List<Permission> permissionList = new ArrayList<>();
        if (roleList != null && roleList.size() != 0) {
            for (int i = 0; i < roleList.size(); i++) {
                permissionList.addAll(rolePermissionRelativeService.getAllPermissionBelongRole(roleList.get(i)));
            }
        }
        return permissionList;
    }

    @Override
    public List<Role> getAllRoleContainPermission(Long permissionId) {
        Permission permission = new Permission();
        permission.setId(permissionId);
        return rolePermissionRelativeService.getAllRoleContainPermission(permission);
    }

    @Override
    public List<Permission> getAllPermissionBelongRole(Long roleId) {
        Role role = new Role();
        role.setId(roleId);
        return rolePermissionRelativeService.getAllPermissionBelongRole(role);
    }

    @Override
    public Map<String, String> getUrlPermissionMap() {
        return permissionUrlService.putUrlPermissionToMap();
    }

    @Override
    public boolean addRole(String role, String name) {

        if (role != null) {
            Role geetionRole = new Role();
            geetionRole.setRole(role);
            geetionRole.setName(name == null ? null : name);
            return roleService.add(geetionRole);
        }
        return false;
    }

    @Override
    public boolean addRole(Role role) {

        if (role != null) {
            return roleService.add(role);
        }
        return false;
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleService.getById(roleId);
    }

    @Override
    public boolean addPermission(String permission, String name) {
        if (permission != null) {
            Permission geetionPermission = new Permission();
            geetionPermission.setPermission(permission);
            geetionPermission.setName(name == null ? null : name);
            return permissionService.add(geetionPermission);
        }
        return false;
    }

    @Override
    public boolean addPermissionToRole(Long[] permissionIds, Long roleId) {
        return rolePermissionRelativeService.addPermissionToRole(permissionIds, roleId);
    }

    @Override
    public boolean removePermissionFromRole(Long[] permissionIds, Long roleId) {
        return rolePermissionRelativeService.removePermissionFromRole(permissionIds, roleId);
    }

    @Override
    public boolean setRoleUrl(Long roleId, String url) {

        Role role = new Role();
        role.setId(roleId);

        //查询该角色所有的权限
        List<Permission> permissionList =
                rolePermissionRelativeService.getAllPermissionBelongRole(role);

        List<PermissionUrl> list = new ArrayList<>();

        //如果该角色有权限，则批量插入所有权限和url的对应关系
        if (permissionList != null && permissionList.size() > 0) {

            Iterator<Permission> iterator = permissionList.iterator();
            while (iterator.hasNext()) {
                PermissionUrl permissionUrl = new PermissionUrl();
                permissionUrl.setPermissionId(iterator.next().getId());
                permissionUrl.setUrl(url);
                list.add(permissionUrl);
            }
        }

        return permissionUrlService.addBatch(list);
    }

    @Override
    public boolean setPermissionUrl(Long permissionId, String url) {

        //添加需要权限的路径
        PermissionUrl permissionUrl = new PermissionUrl();
        permissionUrl.setPermissionId(permissionId);
        permissionUrl.setUrl(url);
        return permissionUrlService.add(permissionUrl);
    }


    @Override
    public Role getByRole(String role) {
        return roleService.getByRole(role);
    }

    @Override
    public boolean deleteBatchByUserId(Long[] userIds) {
        return userRoleRelativeService.deleteBatchByUserId(userIds);
    }

    @Override
    public boolean deleteRoleBatchByUserId(Long[] userIds, Long roleId) {
        return userRoleRelativeService.deleteRoleBatchByUserId(userIds,roleId);
    }

    @Override
    public List<UserBase> getAllUserNotRole() {
        return userRoleRelativeService.getAllUserNotRole();
    }

    @Override
    public List<UserBase> getAllUserByRoleId(Long roleId) {
        return userRoleRelativeService.getAllUserByRoleId(roleId);
    }

    @Override
    public List<UserBase> getAllUserByRole(String role) {
        return userRoleRelativeService.getAllUserByRole(role);
    }
}

