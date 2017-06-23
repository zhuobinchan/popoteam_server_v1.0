package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "pu_recommend")
public class Recommend extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long mainGroupId;

    @Column
    private Long matchGroupId;

    @Column
    private Integer scoreA;

    @Column
    private Integer scoreB;

    @Column
    private Integer scoreC;

    @Column
    private Integer scoreD;

    @Column
    private Integer scoreE;

    @Column
    private Integer scoreF;

    @Column
    private Double weight;

    @Column
    private Integer type;

    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getScoreA() {
        return scoreA;
    }

    public void setScoreA(Integer scoreA) {
        this.scoreA = scoreA;
    }

    public Integer getScoreB() {
        return scoreB;
    }

    public void setScoreB(Integer scoreB) {
        this.scoreB = scoreB;
    }

    public Integer getScoreC() {
        return scoreC;
    }

    public void setScoreC(Integer scoreC) {
        this.scoreC = scoreC;
    }

    public Integer getScoreD() {
        return scoreD;
    }

    public void setScoreD(Integer scoreD) {
        this.scoreD = scoreD;
    }

    public Integer getScoreE() {
        return scoreE;
    }

    public void setScoreE(Integer scoreE) {
        this.scoreE = scoreE;
    }

    public Integer getScoreF() {
        return scoreF;
    }

    public void setScoreF(Integer scoreF) {
        this.scoreF = scoreF;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Recommend{" +
                "id=" + id +
                ", mainGroupId=" + mainGroupId +
                ", matchGroupId=" + matchGroupId +
                ", scoreA=" + scoreA +
                ", scoreB=" + scoreB +
                ", scoreC=" + scoreC +
                ", scoreD=" + scoreD +
                ", scoreE=" + scoreE +
                ", scoreF=" + scoreF +
                ", weight=" + weight +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}