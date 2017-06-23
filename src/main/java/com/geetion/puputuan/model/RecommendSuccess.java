package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "pu_recommend_success")
public class RecommendSuccess extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long mainRecommendId;

    @Column
    private Long matchRecommendId;

    @Column
    private Long mainGroupId;

    @Column
    private Long matchGroupId;

    @Column
    private Boolean type;

    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMainRecommendId() {
        return mainRecommendId;
    }

    public void setMainRecommendId(Long mainRecommendId) {
        this.mainRecommendId = mainRecommendId;
    }

    public Long getMatchRecommendId() {
        return matchRecommendId;
    }

    public void setMatchRecommendId(Long matchRecommendId) {
        this.matchRecommendId = matchRecommendId;
    }

    public Long getMainGroupId() {
        return mainGroupId;
    }

    public void setMainGroupId(Long mainGroupId) {
        this.mainGroupId = mainGroupId;
    }

    public Long getMatchGroupId() {
        return matchGroupId;
    }

    public void setMatchGroupId(Long matchGroupId) {
        this.matchGroupId = matchGroupId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}