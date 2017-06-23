package com.geetion.generic.permission.service;


import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.PermissionUrl;
import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
public interface PermissionUrlService {

    /**
     * 添加用户
     *
     * @param object
     * @return 是否添加成功
     */
    public boolean add(PermissionUrl object);

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
    public boolean update(PermissionUrl object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return 该用户信息
     */
    public PermissionUrl getById(Long id);

    /**
     * 不分页查询全部用户
     *
     * @return 全部用户信息
     */
    public List<PermissionUrl> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     * @return 分页后的信息
     */
    public PagingResult<PermissionUrl> getByPagination(PageEntity pageEntity);

    /**
     * 根据条件查询用户
     *
     * @param params
     * @return 查询后的用户信息
     */
    public List<PermissionUrl> getByParam(Map<String, Object> params);


    /**
     * 查询所有需要权限认证的URL
     * @return
     */
    public List<PermissionUrl> getAllUrlNeedPermission();


    /**
     * 将所有url以及Permission放进Map ，以url为key，permission为value
     *
     * @return
     */
    public Map<String, String> putUrlPermissionToMap();

    /**
     * 批量插入
     *
     * @return
     */
    public boolean addBatch(List<PermissionUrl> list);

    /**
     * 根据权限列表获取所属权限的url
     * @param list
     * @return
     */
    List<PermissionUrl> getPermissionUrlsByPermissionList(List<Permission> list);

}

