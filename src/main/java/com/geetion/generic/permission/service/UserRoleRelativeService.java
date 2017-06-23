package com.geetion.generic.permission.service;

import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;
import com.geetion.generic.userbase.pojo.UserBase;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
public interface UserRoleRelativeService {

    /**
     * 添加用户
     *
     * @param object
     * @return 是否添加成功
     */
    public boolean add(UserRoleRelative object);

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
    public boolean update(UserRoleRelative object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return 该用户信息
     */
    public UserRoleRelative getById(Long id);

    /**
     * 不分页查询全部用户
     *
     * @return 全部用户信息
     */
    public List<UserRoleRelative> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     * @return 分页后的信息
     */
    public PagingResult<UserRoleRelative> getByPagination(PageEntity pageEntity);

    /**
     * 根据条件查询用户
     *
     * @param params
     * @return 查询后的用户信息
     */
    public List<UserRoleRelative> getByParam(Map<String, Object> params);



    /**
     * 根据用户Id获得用户的权限
     * @param userId
     * @return
     */
    public List<Role> getRoleByUserId(Long userId);


    /**
     * 批量删除用户与角色关系（删掉用户所有角色）
     * @param userIds
     * @return
     */
    public boolean deleteBatchByUserId(Long[] userIds);

    /**
     * 批量删除用户与角色关系（删掉用户对应角色）
     * @param userIds
     * @return
     */
    public boolean deleteRoleBatchByUserId(Long[] userIds, Long roleId);

    /**
     * 根据用户Id获得用户的权限
     * @param params
     * @return
     */
    public boolean addRoleByUserIdBatch(Map<String, Object> params);



    /**
     * 获取没有角色的用户
     *
     * @return
     */
    public List<UserBase> getAllUserNotRole();


    /**
     * 获取该角色底下的所有用户
     *
     * @param roleId
     * @return
     */
    public List<UserBase> getAllUserByRoleId(Long roleId);

    /**
     * 分页获取角色下的用户
     * @param roleId
     * @return
     */
    PagingResult<UserBase> getAllUserByRoleIdPagination(Long roleId, PageEntity pageEntity);

    /**
     * 获取该角色底下的所有用户
     *
     * @param role
     * @return
     */
    public List<UserBase> getAllUserByRole(String role);

}

