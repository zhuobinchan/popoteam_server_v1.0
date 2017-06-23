package com.geetion.puputuan.supervene.recommend.model;

import com.geetion.puputuan.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mac on 16/4/19.
 */
public class UserRecommend extends User {

    public UserRecommend(User user) {
        this.setId(user.getId());
        this.setUserId(user.getUserId());
        this.setNickName(user.getNickName());
        this.setIdentify(user.getIdentify());
        this.setSex(user.getSex());
        this.setSign(user.getSign());
        this.setBirthday(user.getBirthday());
        this.setHeadId(user.getHeadId());
        this.setConstellation(user.getConstellation());
        this.setProvinceId(user.getProvinceId());
        this.setCityId(user.getCityId());
        this.setAreaId(user.getAreaId());
        this.setType(user.getType());
        this.setDevice(user.getDevice());
        this.setCreateTime(user.getCreateTime());
        this.setUserBase(user.getUserBase());
        this.setProvince(user.getProvince());
        this.setCity(user.getCity());
        this.setArea(user.getArea());
    }

    //用户点赞信息
    public List<HashMap<String, Long>> likeList;

    // 0 : [0 ~ 4)
    // 1 : [4 ~ 8)
    // 2 : [8 ~ 12)
    // 3 : [12 ~ 16)
    // 4 : [16 ~ 20)
    // 5 : [20 ~ 24)
    public int SignInfo;

    public List<Long> friendList = new ArrayList<>();

    public List<Long> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Long> friendList) {
        this.friendList = friendList;
    }

    public List<HashMap<String, Long>> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<HashMap<String, Long>> likeList) {
        this.likeList = likeList;
    }

    public int getSignInfo() {
        return SignInfo;
    }

    public void setSignInfo(int signInfo) {
        SignInfo = signInfo;
    }
}
