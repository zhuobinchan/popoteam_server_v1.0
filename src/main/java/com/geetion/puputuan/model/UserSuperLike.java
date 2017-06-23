package com.geetion.puputuan.model;

import com.geetion.generic.districtmodule.pojo.base.BaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by guodikai on 2016/10/25.
 */
@Table(name = "pu_user_super_like")
public class UserSuperLike extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;
    @Column
    private Long recommendId;
    @Column
    private Long userId;
    @Column
    private Date createTime;
    @Column
    private Long groupId;
    @Column
    private Long matchGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getMatchGroupId() {
        return matchGroupId;
    }

    public void setMatchGroupId(Long matchGroupId) {
        this.matchGroupId = matchGroupId;
    }
}
