package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    User getUserById(Long id);

    User getUserByIdentifyId(String identifyId);
    
    User getUser(Map param);

    User getUserByParam(Map param);

    List<User> getUserList(Map param);

    PagingResult<User> getUserPage(PageEntity pageEntity);

    boolean addUser(User object);

    boolean updateUser(User object);

    boolean updateUserByUserId(User object);

    boolean removeUser(Long id);

    /**
     * 根据用户userId查询用户
     *
     * @param userId
     * @return
     */
    public User getByUserId(Long userId);


    /**
     * 根据用户userId查询用户，并且带有兴趣职业中还包含了自己没有选择但是是属于系统的兴趣职业     *
     *
     * @param userId
     * @return
     */
    public User getByUserIdWithSystem(Long userId);

    /**
     * 根据用户的identity或是昵称查询用户
     *
     * @param param
     * @return
     */
    public List<User> getByIdentifyNameList(Map param);

    /**
     * 根据用户的identity或是昵称查询用户，分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<User> getByIdentifyNamePage(PageEntity pageEntity);


    //==================================== 查询用户，带上各种计算 =================================


    /**
     * 查询用户，带上各种计算 -- 单个
     *
     * @param userId
     * @return
     */
    public UserWithCount getWithCountByUserId(Long userId);


    /**
     * 查询用户，带上各种计算 -- 不分页
     *
     * @param param
     * @return
     */
    public List<UserWithCount> getParamWithCount(Map param);


    /**
     * 通过批量的id 或者 电话号码 进行查询
     * 批量 查询用户，带上各种计算 -- 列表
     * @param param
     * @return
     */
    public List<UserWithCount> getUserListWithCountByIdsOrPhones(Map param);


    /**
     * 查询用户，带上各种计算 -- 分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<UserWithCount> getParamWithCountPage(PageEntity pageEntity);


    //==================================== 查询用户详细的个人资料，带上所有计算 =================================

    /**
     * 查询用户详细的个人资料，带上所有计算 -- 单个
     *
     * @param userId
     * @return
     */
    public UserDetailWithCount getWithDetailCountByUserId(Long userId);


    // ==============================  所有用户的统计 ============================================

    /**
     * 查询所有跟用户相关的统计字段
     *
     * @return
     */
    public CountUserRelated getAllUserRelatedCount();

    List<UserStatisData> getUserStaticDataByParam(Map param);

    PagingResult<UserStatisData> getUserStaticDataByParamPage(PageEntity pageEntity);

    List<StatisData> getUserJobStaticDataByParam(Map param);

    PagingResult<StatisData> getUserJobStaticDataByParamPage(PageEntity pageEntity);

    List<StatisData> getUserInterestStaticDataByParam(Map param);

    PagingResult<StatisData> getUserInterestStaticDataByParamPage(PageEntity pageEntity);

    UserStatisData getUserSumStatisData();

    UserStatisData getUserSumForCharts(Map param);

    List<UserStatisData> getUserSumForBar(Map param);

    User getUserByIdentify(String identify);

    User getUserByPhone(String phone);

    void updateUserDailyLiving(Long userId);
}
