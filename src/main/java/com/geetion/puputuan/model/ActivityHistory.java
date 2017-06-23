package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;
import com.geetion.puputuan.model.jsonModel.JSONUserBase;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "pu_activity_history")
public class ActivityHistory extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private String name;

    @Column
    private Long groupAId;
//    private Long maleGroupId;

    @Column
    private Long groupBId;
//    private Long femaleGroupId;

    @Column
    private String roomId;

    @Column
    private Integer type;

    @Column
    private Date createTime;

    @Column
    private Integer superLike;

    @Column
    private Date expireTime;

    @Column
    private Integer expireType;

    @Column
    /*是否永久有效*/
    private Integer isExpire;

    private String maleGroupName;

    private String femaleGroupName;

    @Transient
    private List<JSONUserBase> activityMemberList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getGroupBId() {
        return groupBId;
    }

    public void setGroupBId(Long groupBId) {
        this.groupBId = groupBId;
    }

    public Long getGroupAId() {
        return groupAId;
    }

    public void setGroupAId(Long groupAId) {
        this.groupAId = groupAId;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<JSONUserBase> getActivityMemberList() {
        return activityMemberList;
    }

    public void setActivityMemberList(List<JSONUserBase> activityMemberList) {
        this.activityMemberList = activityMemberList;
    }

    public Integer getSuperLike() {
        return superLike;
    }

    public void setSuperLike(Integer superLike) {
        this.superLike = superLike;
    }

    public String getMaleGroupName() {
        return maleGroupName;
    }

    public void setMaleGroupName(String maleGroupName) {
        this.maleGroupName = maleGroupName;
    }

    public String getFemaleGroupName() {
        return femaleGroupName;
    }

    public void setFemaleGroupName(String femaleGroupName) {
        this.femaleGroupName = femaleGroupName;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getExpireType() {
        return expireType;
    }

    public void setExpireType(Integer expireType) {
        this.expireType = expireType;
    }

    public Integer getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Integer isExpire) {
        this.isExpire = isExpire;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupAId=" + groupAId +
                ", groupBId=" + groupBId +
                ", roomId='" + roomId + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}