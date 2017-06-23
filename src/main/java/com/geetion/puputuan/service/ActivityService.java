package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ActivityDetailWithCount;
import com.geetion.puputuan.pojo.ActivityStatisData;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    Activity getActivityById(Long id);

    Activity getActivity(Map param);

    List<Activity> getActivityList(Map param);

    PagingResult<Activity> getActivityPage(PageEntity pageEntity);

    boolean addActivity(Activity object);

    boolean updateActivity(Activity object);

    /**
     * 批量设置约会为解散
     * @param list
     * @return
     */
    boolean updateActivityDissBatch(List<Activity> list);

    boolean removeActivity(Long id);


    /**
     * 根据用户id查询当前用户是否有在进行中的约会
     *
     * @param param
     * @return
     */
    public Activity  getActivityByUser(Map param);


    /**
     * 根据activityId查询约会中的用户
     *
     * @param param
     * @return
     */
    public List<User> getActivityMember(Map param);


    /**
     * 查询可以评价的异性非好友用户，如果某异性用户签到过，就算退出也可以被评价
     *
     * @param param
     * @return
     */
    public List<User> getEvaluateUserInActivity(Map param);

    /**
     * 查询用户当前正在进行中的约会
     *
     * @return
     */
    public List<Activity> getRunningActivityList();

    public PagingResult<Activity> getRunningActivityPage(PageEntity pageEntity);

    List<ActivityDetailWithCount> getActivityDetailWithCount(Map param);

    PagingResult<ActivityDetailWithCount> getActivityDetailWithCountPage(PageEntity pageEntity);

    List<ActivityStatisData> getActivityStatisData(Map param);

    PagingResult<ActivityStatisData> getActivityStatisDataPage(PageEntity pageEntity);

    List<ActivityStatisData> getActivityMatchStatisData(Map param);

    PagingResult<ActivityStatisData> getActivityMatchStatisDataPage(PageEntity pageEntity);

    List<ActivityStatisData> getActivityAreaStatisData(Map param);

    PagingResult<ActivityStatisData> getActivityAreaStatisDataPage(PageEntity pageEntity);

    ActivityStatisData getActivitySumStatisData();

    List<ActivityStatisData> getActivitySumForBar(Map param);

    void deleteActivityAndMember(List<Activity> list);

    boolean updateActivityIsExpireByGroupIds(List<Long> groupIds, Integer isExpire);
}
