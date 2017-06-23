package com.geetion.puputuan.pojo;

/**
 * Created by guodikai on 2016/11/1.
 */
public class UserStatisData {
    // 统计时间，按日、月
    private String date;
    // 用户量
    private Integer userTotal;
    // 微信绑定量
    private Integer wechatTotal;
    // 男性用户量
    private Integer maleTotal;
    // 男性用户占比
    private String maleRatio;
    // 女性用户量
    private Integer femaleTotal;
    // 女性用户占比
    private String femaleRatio;
    // 年龄段 <18
    private Integer ageOneTotal;
    // 年龄段 18-29
    private Integer ageTwoTotal;
    // 年龄段 30-45
    private Integer ageThreeTotal;
    // 年龄段 >45
    private Integer ageFourTotal;
    // 当天用户量
    private Integer userTodayTotal;
    // 当天男性用户量
    private Integer maleTodayTotal;
    // 当天女性用户量
    private Integer femaleTodayTotal;

    private Integer friendOneTotal;

    private Integer friendTwoTotal;

    private Integer friendThreeTotal;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(Integer userTotal) {
        this.userTotal = userTotal;
    }

    public Integer getWechatTotal() {
        return wechatTotal;
    }

    public void setWechatTotal(Integer wechatTotal) {
        this.wechatTotal = wechatTotal;
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

    public Integer getAgeOneTotal() {
        return ageOneTotal;
    }

    public void setAgeOneTotal(Integer ageOneTotal) {
        this.ageOneTotal = ageOneTotal;
    }

    public Integer getAgeTwoTotal() {
        return ageTwoTotal;
    }

    public void setAgeTwoTotal(Integer ageTwoTotal) {
        this.ageTwoTotal = ageTwoTotal;
    }

    public Integer getAgeThreeTotal() {
        return ageThreeTotal;
    }

    public void setAgeThreeTotal(Integer ageThreeTotal) {
        this.ageThreeTotal = ageThreeTotal;
    }

    public Integer getAgeFourTotal() {
        return ageFourTotal;
    }

    public void setAgeFourTotal(Integer ageFourTotal) {
        this.ageFourTotal = ageFourTotal;
    }

    public Integer getUserTodayTotal() {
        return userTodayTotal;
    }

    public void setUserTodayTotal(Integer userTodayTotal) {
        this.userTodayTotal = userTodayTotal;
    }

    public Integer getMaleTodayTotal() {
        return maleTodayTotal;
    }

    public void setMaleTodayTotal(Integer maleTodayTotal) {
        this.maleTodayTotal = maleTodayTotal;
    }

    public Integer getFemaleTodayTotal() {
        return femaleTodayTotal;
    }

    public void setFemaleTodayTotal(Integer femaleTodayTotal) {
        this.femaleTodayTotal = femaleTodayTotal;
    }

    public Integer getFriendOneTotal() {
        return friendOneTotal;
    }

    public void setFriendOneTotal(Integer friendOneTotal) {
        this.friendOneTotal = friendOneTotal;
    }

    public Integer getFriendTwoTotal() {
        return friendTwoTotal;
    }

    public void setFriendTwoTotal(Integer friendTwoTotal) {
        this.friendTwoTotal = friendTwoTotal;
    }

    public Integer getFriendThreeTotal() {
        return friendThreeTotal;
    }

    public void setFriendThreeTotal(Integer friendThreeTotal) {
        this.friendThreeTotal = friendThreeTotal;
    }
}
