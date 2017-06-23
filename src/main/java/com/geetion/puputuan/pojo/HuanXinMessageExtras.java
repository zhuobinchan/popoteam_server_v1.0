package com.geetion.puputuan.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 环信发送消息带的extras
 */
public class HuanXinMessageExtras implements Serializable {

    //账户
    private String account;
    //头像id
    private Long headId;
    //用户id
    private Long userId;
    //用户昵称
    private String nickName;
    //群组id
    private Long groupId;
    //头像的ids
    private Long[] headIds;
    //约会id
    private Long activityId;
    //推荐id
    private Long recommendId;
    //地点
    private String location;
    //用户的ids
    private Long[] userIds;
    //头像URL列表
    private String[] heads;
    //是否喜欢
    private Boolean like;
    //群聊id
    private String roomId;
    //是否重置队伍
    private Boolean ifReset;
    public String toJSONString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long[] getHeadIds() {
        return headIds;
    }

    public void setHeadIds(Long[] headIds) {
        this.headIds = headIds;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }

    public String[] getHeads() {
        return heads;
    }

    public void setHeads(String[] heads) {
        this.heads = heads;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Boolean getIfReset() {
        return ifReset;
    }

    public void setIfReset(Boolean ifReset) {
        this.ifReset = ifReset;
    }
}