package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "pu_match_weight")
public class MatchWeight extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    //匹配权重 -- 相互点赞 -- 单位是百分比
    @Column
    private Integer mutualLike;

    //匹配权重 -- 关系度 -- 单位是百分比
    @Column
    private Integer relationship;

    //匹配权重 -- 已投票结果 -- 单位是百分比
    @Column
    private Integer voteResult;

    //匹配权重 -- 星座 -- 单位是百分比
    @Column
    private Integer constellation;

    //匹配权重 -- 职业兴趣 -- 单位是百分比
    @Column
    private Integer interestionJob;

    //匹配权重 -- 约会签到 -- 单位是百分比
    @Column
    private Integer dataSign;

    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMutualLike() {
        return mutualLike;
    }

    public void setMutualLike(Integer mutualLike) {
        this.mutualLike = mutualLike;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public Integer getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(Integer voteResult) {
        this.voteResult = voteResult;
    }

    public Integer getConstellation() {
        return constellation;
    }

    public void setConstellation(Integer constellation) {
        this.constellation = constellation;
    }

    public Integer getInterestionJob() {
        return interestionJob;
    }

    public void setInterestionJob(Integer interestionJob) {
        this.interestionJob = interestionJob;
    }

    public Integer getDataSign() {
        return dataSign;
    }

    public void setDataSign(Integer dataSign) {
        this.dataSign = dataSign;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}