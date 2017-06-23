package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDAO extends BaseDAO<User, Long> {

    /**
     * 根据用户id更新信息
     *
     * @param user
     * @return
     */
    public int updateByUserId(User user);

    /**
     * 根据用户userId查询用户
     *
     * @param userId
     * @return
     */
    public User selectByUserId(@Param("userId") Long userId);

    /**
     * 根据param查询用户
     *
     * @param param
     * @return
     */
    public User selectByParam(Map param);

    /**
     * 根据用户userId查询用户，并且带有兴趣职业中还包含了自己没有选择但是是属于系统的兴趣职业
     *
     * @param userId
     * @return
     */
    public User selectByUserIdWithSystem(@Param("userId") Long userId);

    /**
     * 根据用户的identity或是昵称查询用户
     *
     * @param param
     * @return
     */
    public List<User> selectByIdentifyName(Map param);

    // ================================ 跟每一个用户各自相关的统计=====================================

    /**
     * 查询用户，带上各种计算 -- 单个
     *
     * @param userId
     * @return
     */
    public UserWithCount selectWithCountByUserId(@Param("userId") Long userId);

    /**
     * 查询用户，带上各种计算 -- 列表
     *
     * @param param
     * @return
     */
    public List<UserWithCount> selectParamWithCount(Map param);


    /**
     * 通过id和电话号码集合
     * 批量 查询用户，带上各种计算 -- 列表
     * @param param
     * @return
     */
    public List<UserWithCount> selectUserListWithCountByIdsOrPhones(Map param);

    /**
     * 查询用户详细的个人资料，带上所有计算 -- 单个
     *
     * @param userId
     * @return
     */
    public UserDetailWithCount selectWithDetailCountByUserId(@Param("userId") Long userId);


    // ==============================  所有用户的统计 ============================================

    /**
     * 查询所有跟用户相关的统计字段
     *
     * @return
     */
    public CountUserRelated selectCountUserRelated();

    List<UserStatisData> selectUserStaticData(Map param);

    List<StatisData> selectUserJobStaticData(Map param);

    List<StatisData> selectUserInterestStaticData(Map param);

    UserStatisData selectUserSumStatisData();

    UserStatisData selectUserSumForCharts(Map param);

    List<UserStatisData> selectUserSumForBar(Map param);

    void callUserDailyLiving(Map param);
}