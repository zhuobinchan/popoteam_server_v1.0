package com.geetion.puputuan.model.jsonModel;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;

/**
 * Created by quanjianan on 16/8/26.
 */
public class JSONUserBase extends BaseModel {

    @Column
    private Long userId;

    @Column
    private String identify;

    /**
     * 对于用户自己,这里放的是Photo的ID,方便后台切换头像时查找,
     * 而对于好友,这个字段其实没什么意义,为了能快速拿到URL,这里的是User里边的HeadId,也就是pojoFile的ID
     */
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

    private Long groupId;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "JSONUser{" +
                "userId= " + userId +
                ", identify= " + identify +
                ", avatarId= " + avatarId +
                ", avatarUrl= " + avatarUrl +
                ", nickName= " + nickName +
                ", nickNameChar= " + nickNameChar +
                ", sex= " + sex +
                ", imUser= " + imUser +
                "}";
    }
}
