package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ActivityDetailWithCount;
import com.geetion.puputuan.pojo.ActivityStatisData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityDAO extends BaseDAO<Activity, Long> {


    /**
     * 根据用户id查询当前用户是否有在进行中的约会
     *
     * @param param
     * @return
     */
    public Activity selectByUser(Map param);

    /**
     * 根据activityId查询约会中的用户
     *
     * @param param
     * @return
     */
    public List<User> selectMember(Map param);

    /**
     * 查询可以评价的异性非好友用户，如果某异性用户签到过，就算退出也可以被评价
     *
     * @param param
     * @return
     */
    public List<User> selectEvaluateUser(Map param);

    /**
     * 批量更新约会信息
     * @param list
     * @return
     */
    public int updateActivityDissBatch(List<Activity> list);

    /**
     * 查询约会信息，带统计数据
     * @param param
     * @return
     */
    List<ActivityDetailWithCount> selectActivityDetailCount(Map param);

    List<ActivityStatisData> selectActivityStatisData(Map param);

    List<ActivityStatisData> selectActivityMatchStatisData(Map param);

    List<ActivityStatisData> selectActivityAreaStatisData(Map param);

    ActivityStatisData selectActivitySumStatisData();

    List<ActivityStatisData> selectActivitySumForBar(Map param);

    int deleteActivityBatch(List<Activity> list);

    int updateActivityIsExpireByGroupIds(Map param);
}