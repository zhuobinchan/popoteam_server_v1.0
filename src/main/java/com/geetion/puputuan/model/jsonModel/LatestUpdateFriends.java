package com.geetion.puputuan.model.jsonModel;

import javax.persistence.Column;

/**
 * Created by admin on 2016/7/29.
 */
public class LatestUpdateFriends {

    @Column
    private Long userId;

    @Column
    private String identify;

    @Column
    private Long avatarId;

    @Column
    private String avatarUrl;

    @Column
    private String nickName;

    @Column
    private String nickNameChar;

    @Column
    private String sex;

    @Column
    private ImUser imUser;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
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

    public ImUser getImUser() {
        return imUser;
    }

    public void setImUser(ImUser imUser) {
        this.imUser = imUser;
    }

}
