package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.base.BaseModel;

public class GroupMemberVote extends BaseModel {


    private Long userId;

    private String head;

    private Long groupId;

    private Integer type;

    private Integer hasVote;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHasVote() {
        return hasVote;
    }

    public void setHasVote(Integer hasVote) {
        this.hasVote = hasVote;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "GroupMemberVote{" +
                "userId=" + userId +
                ", head='" + head + '\'' +
                ", groupId=" + groupId +
                ", type=" + type +
                ", hasVote=" + hasVote +
                '}';
    }
}