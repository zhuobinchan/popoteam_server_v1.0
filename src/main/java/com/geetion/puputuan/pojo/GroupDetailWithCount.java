package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;

/**
 * 用户个人信息以及 所有 需要显示的统计字段
 */
public class GroupDetailWithCount extends Group {


    // 推荐次数
    private Integer recommendCount;
    // 约会次数
    private Integer activityCount;

    private String groupType;

    private String groupStatus;

    private String barName;

    public Integer getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }
}