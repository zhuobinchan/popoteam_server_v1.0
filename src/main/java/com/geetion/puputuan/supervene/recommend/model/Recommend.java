package com.geetion.puputuan.supervene.recommend.model;

import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/3/30.
 */
public class Recommend extends BaseModel {
    public Group mainGroup;
    public Group matchGroup;
    //主队用户
    public List<UserRecommend> mainUser = new ArrayList<>();
    //匹配队用户
    public List<UserRecommend> matchUser = new ArrayList<>();
    //匹配队一级朋友关系
    public List<Long> firstFriend = new ArrayList<>();
    //匹配对二级朋友关系
    public List<Long> secondFriend = new ArrayList<>();
    //主队兴趣爱好
    public List<Long> mainInterest = new ArrayList<>();
    //匹配对兴趣爱好
    public List<Long> matchInterest = new ArrayList<>();
    //主队兴趣爱好
    public List<Long> mainJob = new ArrayList<>();
    //匹配对兴趣爱好
    public List<Long> matchJob = new ArrayList<>();
    public long totalA;
    public long totalB;
    public long totalC;
    public long totalD;
    public long totalE;
    public long totalF;
    public long totalF2;
    public float weight;
    public long matchSize;
    public long areaId;
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getMatchSize() {
        return matchSize;
    }

    public void setMatchSize(long matchSize) {
        this.matchSize = matchSize;
    }

    public Group getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(Group mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Group getMatchGroup() {
        return matchGroup;
    }

    public void setMatchGroup(Group matchGroup) {
        this.matchGroup = matchGroup;
    }

    public long getTotalA() {
        return totalA;
    }

    public void setTotalA(long totalA) {
        this.totalA = totalA;
    }

    public long getTotalB() {
        return totalB;
    }

    public void setTotalB(long totalB) {
        this.totalB = totalB;
    }

    public long getTotalC() {
        return totalC;
    }

    public void setTotalC(long totalC) {
        this.totalC = totalC;
    }

    public long getTotalD() {
        return totalD;
    }

    public void setTotalD(long totalD) {
        this.totalD = totalD;
    }

    public long getTotalE() {
        return totalE;
    }

    public void setTotalE(long totalE) {
        this.totalE = totalE;
    }

    public long getTotalF() {
        return totalF;
    }

    public void setTotalF(long totalF) {
        this.totalF = totalF;
    }

    public List<UserRecommend> getMainUser() {
        return mainUser;
    }

    public void setMainUser(List<UserRecommend> mainUser) {
        this.mainUser = mainUser;
    }

    public List<UserRecommend> getMatchUser() {
        return matchUser;
    }

    public void setMatchUser(List<UserRecommend> matchUser) {
        this.matchUser = matchUser;
    }

    public List<Long> getFirstFriend() {
        return firstFriend;
    }

    public void addFristFriend(long fristId) {
        firstFriend.add(fristId);
    }

    public List<Long> getSecondFriend() {
        return secondFriend;
    }

    public void addSecondFriend(long secondId) {
        this.secondFriend.add(secondId);
    }

    public List<Long> getMainInterest() {
        return mainInterest;
    }

    public void setMainInterest(List<Long> mainInterest) {
        this.mainInterest = mainInterest;
    }

    public List<Long> getMatchInterest() {
        return matchInterest;
    }

    public void setMatchInterest(List<Long> matchInterest) {
        this.matchInterest = matchInterest;
    }

    public List<Long> getMatchJob() {
        return matchJob;
    }

    public void setMatchJob(List<Long> matchJob) {
        this.matchJob = matchJob;
    }

    public List<Long> getMainJob() {
        return mainJob;
    }

    public void setMainJob(List<Long> mainJob) {
        this.mainJob = mainJob;
    }

    public void setSecondFriend(List<Long> secondFriend) {
        this.secondFriend = secondFriend;
    }

    public long getTotalF2() {
        return totalF2;
    }

    public void setTotalF2(long totalF2) {
        this.totalF2 = totalF2;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
