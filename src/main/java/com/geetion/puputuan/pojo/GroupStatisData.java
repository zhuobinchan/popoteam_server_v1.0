package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.Group;

/**
 * Created by guodikai on 2016/11/1.
 */
public class GroupStatisData extends Group{
    // 统计时间，按日、月
    private String date;
    // 队伍总量
    private Integer groupTotal;
    // 活跃队伍数
    private Integer groupValidTotal;
    // 男性用户量
    private Integer maleTotal;
    // 男性用户占比
    private String maleRatio;
    // 女性用户量
    private Integer femaleTotal;
    // 女性用户占比
    private String femaleRatio;
    // 组成约会数
    private Integer activityGroupTotal;
    // 队伍平均人数
    private String groupMemberAvg;
    // 类型：一起出去唱k
    private Integer groupBarOne;
    // 类型：今晚去蹦迪
    private Integer groupBarTwo;
    // 类型：出去喝一杯
    private Integer groupBarThree;
    // 类型：我们出去吧
    private Integer groupBarFour;
    // 地区级别， 0为市、1为地区
    private String regionLevel;
    // 市
    private String groupCity;
    // 区
    private String groupArea;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(Integer groupTotal) {
        this.groupTotal = groupTotal;
    }

    public Integer getMaleTotal() {
        return maleTotal;
    }

    public void setMaleTotal(Integer maleTotal) {
        this.maleTotal = maleTotal;
    }

    public String getMaleRatio() {
        return maleRatio;
    }

    public void setMaleRatio(String maleRatio) {
        this.maleRatio = maleRatio;
    }

    public Integer getFemaleTotal() {
        return femaleTotal;
    }

    public void setFemaleTotal(Integer femaleTotal) {
        this.femaleTotal = femaleTotal;
    }

    public String getFemaleRatio() {
        return femaleRatio;
    }

    public void setFemaleRatio(String femaleRatio) {
        this.femaleRatio = femaleRatio;
    }

    public Integer getActivityGroupTotal() {
        return activityGroupTotal;
    }

    public void setActivityGroupTotal(Integer activityGroupTotal) {
        this.activityGroupTotal = activityGroupTotal;
    }

    public String getGroupMemberAvg() {
        return groupMemberAvg;
    }

    public void setGroupMemberAvg(String groupMemberAvg) {
        this.groupMemberAvg = groupMemberAvg;
    }

    public Integer getGroupValidTotal() {
        return groupValidTotal;
    }

    public void setGroupValidTotal(Integer groupValidTotal) {
        this.groupValidTotal = groupValidTotal;
    }

    public Integer getGroupBarOne() {
        return groupBarOne;
    }

    public void setGroupBarOne(Integer groupBarOne) {
        this.groupBarOne = groupBarOne;
    }

    public Integer getGroupBarTwo() {
        return groupBarTwo;
    }

    public void setGroupBarTwo(Integer groupBarTwo) {
        this.groupBarTwo = groupBarTwo;
    }

    public Integer getGroupBarThree() {
        return groupBarThree;
    }

    public void setGroupBarThree(Integer groupBarThree) {
        this.groupBarThree = groupBarThree;
    }

    public Integer getGroupBarFour() {
        return groupBarFour;
    }

    public void setGroupBarFour(Integer groupBarFour) {
        this.groupBarFour = groupBarFour;
    }

    public String getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(String regionLevel) {
        this.regionLevel = regionLevel;
    }

    public String getGroupCity() {
        return groupCity;
    }

    public void setGroupCity(String groupCity) {
        this.groupCity = groupCity;
    }

    public String getGroupArea() {
        return groupArea;
    }

    public void setGroupArea(String groupArea) {
        this.groupArea = groupArea;
    }
}
