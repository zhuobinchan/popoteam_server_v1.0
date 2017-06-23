package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.User;

import java.util.Date;

/**
 * 用户个人信息以及 某些 需要显示的统计字段
 */
public class UserWithCount extends User {


    //邀请次数
    private Integer invitedTimes;
    //7天内邀请次数
    private Integer invitedTimesInWeek;
    //最近发起邀请时间
    private Date invitedLatelyDate;
    //被邀请次数
    private Integer beInvitedTimes;
    //7天内被邀请次数
    private Integer beInvitedTimesInWeek;
    //最近被邀请时间
    private Date beInvitedLatelyDate;
    //好友数
    private Integer friendsNumber;
    //7天内好友新增量
    private Integer friendsNumberInWeek;
    //最近一次新增好友时间
    private Date friendsNewestDate;
    //点赞别人相册次数
    private Integer photoLiked;

    //被别人点赞相册次数
    private Integer photoBeLiked;

    //相册数
    private Integer photoNumber;

    //被投诉次数
    private Integer complainTimes;
    // 年龄
    private Integer age;

    public Integer getInvitedTimes() {
        return invitedTimes;
    }

    public void setInvitedTimes(Integer invitedTimes) {
        this.invitedTimes = invitedTimes;
    }

    public Integer getBeInvitedTimes() {
        return beInvitedTimes;
    }

    public void setBeInvitedTimes(Integer beInvitedTimes) {
        this.beInvitedTimes = beInvitedTimes;
    }

    public Integer getFriendsNumber() {
        return friendsNumber;
    }

    public void setFriendsNumber(Integer friendsNumber) {
        this.friendsNumber = friendsNumber;
    }

    public Integer getPhotoLiked() {
        return photoLiked;
    }

    public void setPhotoLiked(Integer photoLiked) {
        this.photoLiked = photoLiked;
    }

    public Integer getPhotoBeLiked() {
        return photoBeLiked;
    }

    public void setPhotoBeLiked(Integer photoBeLiked) {
        this.photoBeLiked = photoBeLiked;
    }

    public Integer getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(Integer photoNumber) {
        this.photoNumber = photoNumber;
    }

    public Integer getComplainTimes() {
        return complainTimes;
    }

    public void setComplainTimes(Integer complainTimes) {
        this.complainTimes = complainTimes;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getInvitedTimesInWeek() {
        return invitedTimesInWeek;
    }

    public void setInvitedTimesInWeek(Integer invitedTimesInWeek) {
        this.invitedTimesInWeek = invitedTimesInWeek;
    }

    public Integer getBeInvitedTimesInWeek() {
        return beInvitedTimesInWeek;
    }

    public void setBeInvitedTimesInWeek(Integer beInvitedTimesInWeek) {
        this.beInvitedTimesInWeek = beInvitedTimesInWeek;
    }

    public Date getInvitedLatelyDate() {
        return invitedLatelyDate;
    }

    public void setInvitedLatelyDate(Date invitedLatelyDate) {
        this.invitedLatelyDate = invitedLatelyDate;
    }

    public Date getBeInvitedLatelyDate() {
        return beInvitedLatelyDate;
    }

    public void setBeInvitedLatelyDate(Date beInvitedLatelyDate) {
        this.beInvitedLatelyDate = beInvitedLatelyDate;
    }

    public Integer getFriendsNumberInWeek() {
        return friendsNumberInWeek;
    }

    public void setFriendsNumberInWeek(Integer friendsNumberInWeek) {
        this.friendsNumberInWeek = friendsNumberInWeek;
    }

    public Date getFriendsNewestDate() {
        return friendsNewestDate;
    }

    public void setFriendsNewestDate(Date friendsNewestDate) {
        this.friendsNewestDate = friendsNewestDate;
    }
}