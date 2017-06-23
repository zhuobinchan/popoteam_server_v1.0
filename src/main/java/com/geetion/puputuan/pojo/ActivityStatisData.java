package com.geetion.puputuan.pojo;

/**
 * Created by guodikai on 2016/11/2.
 */
public class ActivityStatisData {
    // 按月、日统计数据
    private String date;
    // 约会总数
    private Integer activityTotal;
    // superLike约会总数
    private Integer activitySuperLike;
    // 平均约会人数
    private Integer activityMemberAvg;
    // 有效约会
    private Integer activityValidTotal;
    // 约会市
    private String activityCity;
    private Integer activitySasb;
    private Integer activitySadb;
    private Integer activityScsb;
    private Integer activityScdb;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getActivityTotal() {
        return activityTotal;
    }

    public void setActivityTotal(Integer activityTotal) {
        this.activityTotal = activityTotal;
    }

    public Integer getActivitySuperLike() {
        return activitySuperLike;
    }

    public void setActivitySuperLike(Integer activitySuperLike) {
        this.activitySuperLike = activitySuperLike;
    }

    public Integer getActivityMemberAvg() {
        return activityMemberAvg;
    }

    public void setActivityMemberAvg(Integer activityMemberAvg) {
        this.activityMemberAvg = activityMemberAvg;
    }

    public Integer getActivitySasb() {
        return activitySasb;
    }

    public void setActivitySasb(Integer activitySasb) {
        this.activitySasb = activitySasb;
    }

    public Integer getActivitySadb() {
        return activitySadb;
    }

    public void setActivitySadb(Integer activitySadb) {
        this.activitySadb = activitySadb;
    }

    public Integer getActivityScsb() {
        return activityScsb;
    }

    public void setActivityScsb(Integer activityScsb) {
        this.activityScsb = activityScsb;
    }

    public Integer getActivityScdb() {
        return activityScdb;
    }

    public void setActivityScdb(Integer activityScdb) {
        this.activityScdb = activityScdb;
    }

    public String getActivityCity() {
        return activityCity;
    }

    public void setActivityCity(String activityCity) {
        this.activityCity = activityCity;
    }

    public Integer getActivityValidTotal() {
        return activityValidTotal;
    }

    public void setActivityValidTotal(Integer activityValidTotal) {
        this.activityValidTotal = activityValidTotal;
    }
}
