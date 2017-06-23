package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.UserDAO;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.*;
import com.geetion.puputuan.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Override
    public User getUserById(Long id) {
        return userDAO.selectByPrimaryKey(id);
    }

    @Override
    public User getUserByIdentifyId(String identifyId) {
        Map<String, String> param = new HashMap<>();
        param.put("identify", identifyId);
        return getUser(param);
    }

    @Override
    public User getUser(Map param) {
        return userDAO.selectOne(param);
    }

    @Override
    public User getUserByParam(Map param) {
        return userDAO.selectByParam(param);
    }

    @Override
    public List<User> getUserList(Map param) {
        return userDAO.selectParam(param);
    }

    @Override
    public PagingResult<User> getUserPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<User> list = getUserList(pageEntity.getParam());
        PageInfo<User> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addUser(User object) {
        return userDAO.insertSelective(object) == 1;
    }


    @Override
    public boolean updateUser(User object) {
        return userDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean updateUserByUserId(User object) {
        return userDAO.updateByUserId(object) == 1;
    }

    @Override
    public boolean removeUser(Long id) {
        return userDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public User getByUserId(Long userId) {
        return userDAO.selectByUserId(userId);
    }

    @Override
    public User getByUserIdWithSystem(Long userId) {
        return userDAO.selectByUserIdWithSystem(userId);
    }

    @Override
    public List<User> getByIdentifyNameList(Map param) {
        return userDAO.selectByIdentifyName(param);
    }

    @Override
    public PagingResult<User> getByIdentifyNamePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<User> list = getByIdentifyNameList(pageEntity.getParam());
        PageInfo<User> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }


    @Override
    public UserWithCount getWithCountByUserId(Long userId) {
        return userDAO.selectWithCountByUserId(userId);
    }

    @Override
    public List<UserWithCount> getParamWithCount(Map param) {
        return userDAO.selectParamWithCount(param);
    }

    @Override
    public PagingResult<UserWithCount> getParamWithCountPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());

        if(null != pageEntity.getOrderColumn() && null != pageEntity.getOrderTurn() ){
            String orderColumn = pageEntity.getOrderColumn();
            // 特殊处理排序字段
            if("nickName".equals(orderColumn)){
                orderColumn = "nick_name";
            }
            if("createTime".equals(orderColumn)){
                orderColumn = "create_time";
            }
            if("userBase.freeze".equals(orderColumn)){
                orderColumn = "geetion_user_base_freeze";
            }
            if("invitedTimes".equals(orderColumn)){
                orderColumn = "invited_times";
            }
            if("beInvitedTimes".equals(orderColumn)){
                orderColumn = "be_invited_times";
            }
            if("friendsNumber".equals(orderColumn)){
                orderColumn = "friends_number";
            }

            if("geetion_user_base_freeze".equals(orderColumn)){
                PageHelper.orderBy(orderColumn + " " + pageEntity.getOrderTurn());
            }else {
                PageHelper.orderBy("pu_user_" + orderColumn + " " + pageEntity.getOrderTurn());
            }

        }
        List<UserWithCount> list = getParamWithCount(pageEntity.getParam());
        PageInfo<UserWithCount> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public UserDetailWithCount getWithDetailCountByUserId(Long userId) {

        return userDAO.selectWithDetailCountByUserId(userId);
    }

    @Override
    public CountUserRelated getAllUserRelatedCount() {
        return userDAO.selectCountUserRelated();
    }

    @Override
    public List<UserStatisData> getUserStaticDataByParam(Map param) {
        return userDAO.selectUserStaticData(param);
    }

    @Override
    public PagingResult<UserStatisData> getUserStaticDataByParamPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserStatisData> list = getUserStaticDataByParam(pageEntity.getParam());
        PageInfo<UserStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<StatisData> getUserJobStaticDataByParam(Map param) {
        return userDAO.selectUserJobStaticData(param);
    }

    @Override
    public PagingResult<StatisData> getUserJobStaticDataByParamPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<StatisData> list = getUserJobStaticDataByParam(pageEntity.getParam());
        PageInfo<StatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<StatisData> getUserInterestStaticDataByParam(Map param) {
        return userDAO.selectUserInterestStaticData(param);
    }

    @Override
    public PagingResult<StatisData> getUserInterestStaticDataByParamPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<StatisData> list = getUserInterestStaticDataByParam(pageEntity.getParam());
        PageInfo<StatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public UserStatisData getUserSumStatisData() {
        return userDAO.selectUserSumStatisData();
    }

    @Override
    public UserStatisData getUserSumForCharts(Map param) {
        return userDAO.selectUserSumForCharts(param);
    }

    @Override
    public List<UserStatisData> getUserSumForBar(Map param) {
        return userDAO.selectUserSumForBar(param);
    }

    @Override
    public User getUserByIdentify(String identify) {
        Map<String, Object> params = new HashMap<>();
        params.put("identify", identify);
        return this.getUser(params);

    }

    @Override
    public User getUserByPhone(String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "+86" + phone);
        return this.getUser(params);
    }



    @Override
    public void updateUserDailyLiving(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        userDAO.callUserDailyLiving(params);
    }
    @Override
    public List<UserWithCount> getUserListWithCountByIdsOrPhones(Map param) {
        return userDAO.selectUserListWithCountByIdsOrPhones(param);
    }
}
