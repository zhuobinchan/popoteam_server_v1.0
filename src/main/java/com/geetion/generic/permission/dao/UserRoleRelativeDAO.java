package com.geetion.generic.permission.dao;

import com.geetion.generic.permission.dao.base.BaseDAO;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.userbase.pojo.UserBase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
@Repository("geetionUserRoleRelativeDAO")
public interface UserRoleRelativeDAO extends BaseDAO<UserRoleRelative, Long> {

    /**
     * 根据用户id查询用户所有角色
     * @param userId
     * @return
     */
    public List<Role> selectRoleByUserId(@Param("userId") Long userId);

    /**
     * 批量删除用户与角色关系（删掉用户所有角色）
     */
    public int deleteBatchByUserId(@Param("userIds")Long[] userIds);


    /**
     * 批量删除用户与角色关系（删掉用户对应角色）
     */
    public int deleteRoleBatchByUserId(Map params);



    /**
     * 根据用户id查询用户所有角色
     * @param params
     * @return
     */
    public int insertRoleByUserIdBatch(Map params);


    /**
     * 查询所有没有角色的用户
     * @return
     */
    public List<UserBase> selectAllUserNotRole();


    /**
     * 根据roleId查询所有该角色下的用户
     * @param roleId
     * @return
     */
    public List<UserBase> selectAllUserByRoleId(@Param("roleId") Long roleId);


    /**
     * 根据role查询所有该角色下的用户
     * @param role
     * @return
     */
    public List<UserBase> selectAllUserByRole(@Param("role") String role);


}