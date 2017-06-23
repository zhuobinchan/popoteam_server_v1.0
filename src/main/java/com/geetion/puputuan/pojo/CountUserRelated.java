package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.base.BaseModel;

/**
 * 所有跟用户相关的统计字段
 */
public class CountUserRelated extends BaseModel {

    //用户总数
    private Integer userTotal;
    //日新增用户数
    private Integer userDayTotal;
    //总相片数
    private Integer photoTotal;
    //总点赞数
    private Integer photoLikeTotal;
    //日新增点赞数
    private Integer photoLikeDayTotal;
    //邀请总数
    private Integer invitedTotal;
    //人均好友数
    private Integer friendPersonNumber;
    //人均标签数
    private Integer labPerson;
    //人均相片数
    private Integer photoPersonTotal;
    //人均被浏览数
    private Integer beViewedTotal;
    //人均被赞数
    private Integer photoLikePersonTotal;
    //人均邀请数
    private Integer beInvitedTotal;

    public Integer getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(Integer userTotal) {
        this.userTotal = userTotal;
    }

    public Integer getUserDayTotal() {
        return userDayTotal;
    }

    public void setUserDayTotal(Integer userDayTotal) {
        this.userDayTotal = userDayTotal;
    }

    public Integer getPhotoTotal() {
        return photoTotal;
    }

    public void setPhotoTotal(Integer photoTotal) {
        this.photoTotal = photoTotal;
    }

    public Integer getPhotoLikeTotal() {
        return photoLikeTotal;
    }

    public void setPhotoLikeTotal(Integer photoLikeTotal) {
        this.photoLikeTotal = photoLikeTotal;
    }

    public Integer getPhotoLikeDayTotal() {
        return photoLikeDayTotal;
    }

    public void setPhotoLikeDayTotal(Integer photoLikeDayTotal) {
        this.photoLikeDayTotal = photoLikeDayTotal;
    }

    public Integer getInvitedTotal() {
        return invitedTotal;
    }

    public void setInvitedTotal(Integer invitedTotal) {
        this.invitedTotal = invitedTotal;
    }

    public Integer getFriendPersonNumber() {
        return friendPersonNumber;
    }

    public void setFriendPersonNumber(Integer friendPersonNumber) {
        this.friendPersonNumber = friendPersonNumber;
    }

    public Integer getLabPerson() {
        return labPerson;
    }

    public void setLabPerson(Integer labPerson) {
        this.labPerson = labPerson;
    }

    public Integer getPhotoPersonTotal() {
        return photoPersonTotal;
    }

    public void setPhotoPersonTotal(Integer photoPersonTotal) {
        this.photoPersonTotal = photoPersonTotal;
    }

    public Integer getBeViewedTotal() {
        return beViewedTotal;
    }

    public void setBeViewedTotal(Integer beViewedTotal) {
        this.beViewedTotal = beViewedTotal;
    }

    public Integer getPhotoLikePersonTotal() {
        return photoLikePersonTotal;
    }

    public void setPhotoLikePersonTotal(Integer photoLikePersonTotal) {
        this.photoLikePersonTotal = photoLikePersonTotal;
    }

    public Integer getBeInvitedTotal() {
        return beInvitedTotal;
    }

    public void setBeInvitedTotal(Integer beInvitedTotal) {
        this.beInvitedTotal = beInvitedTotal;
    }
}