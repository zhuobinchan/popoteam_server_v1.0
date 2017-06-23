package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.Activity;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by guodikai on 2016/11/1.
 */
public class ActivityDetailWithCount extends Activity {
    private Integer activityMemberCount;

    private Integer duration;

    private String activityStatus;

    public Integer getActivityMemberCount() {
        return activityMemberCount;
    }

    public void setActivityMemberCount(Integer activityMemberCount) {
        this.activityMemberCount = activityMemberCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }
}
