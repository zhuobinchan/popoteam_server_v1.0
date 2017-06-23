package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.Complain;
import com.geetion.puputuan.model.User;

/**
 * 查询被投诉的相册 和 发布者 ，相册点赞数和已被投诉次数
 */
public class ComplainWithPhotoAndUser extends Complain {


    //相册点赞数
    private Integer photoLikeTotal;

    //被投诉的相册的发布者
    private User photoUser;

    //被投诉次数
    private Integer complainTimes;

    public Integer getPhotoLikeTotal() {
        return photoLikeTotal;
    }

    public void setPhotoLikeTotal(Integer photoLikeTotal) {
        this.photoLikeTotal = photoLikeTotal;
    }

    public User getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(User photoUser) {
        this.photoUser = photoUser;
    }

    public Integer getComplainTimes() {
        return complainTimes;
    }

    public void setComplainTimes(Integer complainTimes) {
        this.complainTimes = complainTimes;
    }
}