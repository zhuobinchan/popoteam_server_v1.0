package com.geetion.puputuan.model;

import com.geetion.generic.districtmodule.pojo.base.BaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by guodikai on 2016/10/26.
 */
@Table(name = "pu_user_like_group")
public class UserLikeGroup extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;
    @Column
    private Long groupId;
    @Column
    private Long likeGroupId;
    @Column
    private Long userId;
    @Column
    private Integer isLike;
    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getLikeGroupId() {
        return likeGroupId;
    }

    public void setLikeGroupId(Long likeGroupId) {
        this.likeGroupId = likeGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
