package com.geetion.generic.userbase.service;


import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.utils.mybatis.PageEntity;
import com.geetion.generic.userbase.utils.mybatis.PagingResult;
import com.geetion.puputuan.pojo.UserStatisData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
public interface UserBaseService {

    /**
     * 添加用户
     *
     * @param object
     * @return 是否添加成功
     */
    public boolean add(UserBase object) throws SQLException, Exception;

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
    public boolean update(UserBase object);

    /**
     * 修改用户信息（不包括密码）
     *
     * @param object
     * @return 是否修改成功
     */
    public boolean updateWithoutPassword(UserBase object);

    /**
     * 修改用户密码
     *
     * @param account
     * @param password
     * @return
     */
    public boolean updatePassword(String account, String password);

    /**
     * 根据Id查询
     *
     * @param id
     * @return 该用户信息
     */
    public UserBase getById(Long id);


    /**
     * 根据account查询
     *
     * @param account
     * @return 该用户信息
     */
    public UserBase getByAccount(String account);


    /**
     * 根据token查询
     *
     * @param token
     * @return 该用户信息
     */
    public UserBase getByToken(String token);


    /**
     * 不分页查询全部用户
     *
     * @return 全部用户信息
     */
    public List<UserBase> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     * @return 分页后的信息
     */
    public PagingResult<UserBase> getByPagination(PageEntity pageEntity);

    /**
     * 根据条件查询用户
     *
     * @param params
     * @return 查询后的用户信息
     */
    public List<UserBase> getByParam(Map<String, Object> params);


    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    public boolean deleteUserBaseBatch(Long[] ids);

    /**
     * 批量冻结用户
     *
     * @return
     */
    boolean freezeUserBaseBatch(List<Long> ids, boolean freeze);
    /**
     * 批量冻结用户
     *
     * @return
     */
    boolean freezeUserBaseBatch(List<Long> ids, boolean freeze, String token);

    /**
     * 判断用户旧密码是否正确
     * @param account
     * @param password
     * @return
     */
    boolean checkOldPassword(String account, String password);

}

