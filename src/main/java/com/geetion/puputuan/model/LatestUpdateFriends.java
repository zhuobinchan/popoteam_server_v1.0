package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;

/**
 * Created by dazai on 2016/7/27.
 */
public class LatestUpdateFriends extends BaseModel {

private  Integer userId;

private  Integer identify;

private  Integer avatarId;

private  String avatarUrl;

private  String nickName;

private  String nickNameChar;

private  String sex;




    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIdentify() {
        return identify;
    }

    public void setIdentify(Integer identify) {
        this.identify = identify;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickNameChar() {
        return nickNameChar;
    }

    public void setNickNameChar(String nickNameChar) {
        this.nickNameChar = nickNameChar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }




}
