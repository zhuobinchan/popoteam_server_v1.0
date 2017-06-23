package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.base.BaseModel;

import java.util.List;

/**
 * Created by chenzhuobin on 2017/3/28.
 */
public class DailyLivingStatisData  extends BaseModel {
    // 按月、日统计数据
    private String date;

    //活跃的用户数
    private Integer userDailyLivingTotal;

    //活跃的用户数
    private Integer groupDailyLivingTotal;

    //当天的活跃的用户编号集合
    private List<String> userIds;

    //当天的活跃的队伍编号集合
    private List<String> groupIds;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUserDailyLivingTotal() {
        return userDailyLivingTotal;
    }

    public void setUserDailyLivingTotal(Integer userDailyLivingTotal) {
        this.userDailyLivingTotal = userDailyLivingTotal;
    }

    public Integer getGroupDailyLivingTotal() {
        return groupDailyLivingTotal;
    }

    public void setGroupDailyLivingTotal(Integer groupDailyLivingTotal) {
        this.groupDailyLivingTotal = groupDailyLivingTotal;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
