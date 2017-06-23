package com.geetion.generic.permission.service;


import com.geetion.generic.permission.pojo.Role;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
public interface RoleService {

    /**
     * 添加用户
     *
     * @param object
     * @return 是否添加成功
     */
    public boolean add(Role object);

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
    public boolean update(Role object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return 该用户信息
     */
    public Role getById(Long id);

    /**
     * 根据角色查询
     *
     * @param role
     * @return 该用户信息
     */
    public Role getByRole(String role);

    /**
     * 不分页查询全部用户
     *
     * @return 全部用户信息
     */
    public List<Role> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     * @return 分页后的信息
     */
    public PagingResult<Role> getByPagination(PageEntity pageEntity);

    /**
     * 根据条件查询用户
     *
     * @param params
     * @return 查询后的用户信息
     */
    public List<Role> getByParam(Map<String, Object> params);


}

