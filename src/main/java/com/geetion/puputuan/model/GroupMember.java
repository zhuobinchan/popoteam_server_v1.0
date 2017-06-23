package com.geetion.puputuan.model;

import com.geetion.puputuan.common.constant.GroupMemberTypeAndStatus;
import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pu_group_member")
public class GroupMember extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long groupId;

    @Column
    private Integer status;

    @Column
    private Integer type;

    @Column
    private Date createTime;

    @Transient
    private User user;

    @Transient
    private boolean isLeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLeader() {
        if (type == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER){
            return true;
        }else{
            return false;
        }
    }

}