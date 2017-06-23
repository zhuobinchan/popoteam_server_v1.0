package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.User;

/**
 * 用户个人信息以及 所有 需要显示的统计字段
 */
public class UserDetailWithCount extends User {


    //邀请次数
    private Integer invitedTimes;

    //被邀请次数
    private Integer beInvitedTimes;

    //好友数
    private Integer friendsNumber;

    //点赞别人相册次数
    private Integer photoLiked;

    //被别人点赞相册次数
    private Integer photoBeLiked;

    //匹配总数
    private Integer matchTimes;

    //匹配成功数
    private Integer matchSuccessTimes;

    //匹配成功率
    private String matchSuccessRatio;

    //参与组队数
    private Integer joinGroup;

    //当天参与组队数
    private Integer joinGroupCurrent;

    //好友来源为搜索的数
    private Integer friendFromSearch;

    //好友来源为约会成功互评成功的数
    private Integer friendFromActivity;

    //约会数
    private Integer activityTimes;
    // 当天约会数
    private Integer activityCurrentTimes;
    //约会成功数
    private Integer activitySuccessTimes;

    //约会成功率
    private String activitySuccessRatio;

    //相册来源为即时拍照的数
    private Integer photoByTake;

    //相册来源为存储选取的数
    private Integer photoInAlbum;

    // 被浏览数
    private Integer beViewedNumber;

    // 资料完善程度
    private String dataFullRatio;
    // 是否在队伍中
    private String isInGroup;
    // 是否在约会中
    private String isInActivity;
    // superLike次数
    private Integer superLikeTimes;

    public Integer getInvitedTimes() {
        return invitedTimes;
    }

    public void setInvitedTimes(Integer invitedTimes) {
        this.invitedTimes = invitedTimes;
    }

    public Integer getBeInvitedTimes() {
        return beInvitedTimes;
    }

    public void setBeInvitedTimes(Integer beInvitedTimes) {
        this.beInvitedTimes = beInvitedTimes;
    }

    public Integer getFriendsNumber() {
        return friendsNumber;
    }

    public void setFriendsNumber(Integer friendsNumber) {
        this.friendsNumber = friendsNumber;
    }

    public Integer getPhotoLiked() {
        return photoLiked;
    }

    public void setPhotoLiked(Integer photoLiked) {
        this.photoLiked = photoLiked;
    }

    public Integer getPhotoBeLiked() {
        return photoBeLiked;
    }

    public void setPhotoBeLiked(Integer photoBeLiked) {
        this.photoBeLiked = photoBeLiked;
    }

    public Integer getMatchTimes() {
        return matchTimes;
    }

    public void setMatchTimes(Integer matchTimes) {
        this.matchTimes = matchTimes;
    }

    public Integer getMatchSuccessTimes() {
        return matchSuccessTimes;
    }

    public void setMatchSuccessTimes(Integer matchSuccessTimes) {
        this.matchSuccessTimes = matchSuccessTimes;
    }

    public String getMatchSuccessRatio() {
        return matchSuccessRatio;
    }

    public void setMatchSuccessRatio(String matchSuccessRatio) {
        this.matchSuccessRatio = matchSuccessRatio;
    }

    public Integer getJoinGroup() {
        return joinGroup;
    }

    public void setJoinGroup(Integer joinGroup) {
        this.joinGroup = joinGroup;
    }

    public Integer getFriendFromSearch() {
        return friendFromSearch;
    }

    public void setFriendFromSearch(Integer friendFromSearch) {
        this.friendFromSearch = friendFromSearch;
    }

    public Integer getFriendFromActivity() {
        return friendFromActivity;
    }

    public void setFriendFromActivity(Integer friendFromActivity) {
        this.friendFromActivity = friendFromActivity;
    }

    public Integer getActivityTimes() {
        return activityTimes;
    }

    public void setActivityTimes(Integer activityTimes) {
        this.activityTimes = activityTimes;
    }

    public Integer getActivitySuccessTimes() {
        return activitySuccessTimes;
    }

    public void setActivitySuccessTimes(Integer activitySuccessTimes) {
        this.activitySuccessTimes = activitySuccessTimes;
    }

    public String getActivitySuccessRatio() {
        return activitySuccessRatio;
    }

    public void setActivitySuccessRatio(String activitySuccessRatio) {
        this.activitySuccessRatio = activitySuccessRatio;
    }

    public Integer getPhotoByTake() {
        return photoByTake;
    }

    public void setPhotoByTake(Integer photoByTake) {
        this.photoByTake = photoByTake;
    }

    public Integer getPhotoInAlbum() {
        return photoInAlbum;
    }

    public void setPhotoInAlbum(Integer photoInAlbum) {
        this.photoInAlbum = photoInAlbum;
    }

    public Integer getBeViewedNumber() {
        return beViewedNumber;
    }

    public void setBeViewedNumber(Integer beViewedNumber) {
        this.beViewedNumber = beViewedNumber;
    }

    public String getDataFullRatio() {
        return dataFullRatio;
    }

    public void setDataFullRatio(String dataFullRatio) {
        this.dataFullRatio = dataFullRatio;
    }

    public String getIsInGroup() {
        return isInGroup;
    }

    public void setIsInGroup(String isInGroup) {
        this.isInGroup = isInGroup;
    }

    public String getIsInActivity() {
        return isInActivity;
    }

    public void setIsInActivity(String isInActivity) {
        this.isInActivity = isInActivity;
    }

    public Integer getJoinGroupCurrent() {
        return joinGroupCurrent;
    }

    public void setJoinGroupCurrent(Integer joinGroupCurrent) {
        this.joinGroupCurrent = joinGroupCurrent;
    }

    public Integer getActivityCurrentTimes() {
        return activityCurrentTimes;
    }

    public void setActivityCurrentTimes(Integer activityCurrentTimes) {
        this.activityCurrentTimes = activityCurrentTimes;
    }

    public Integer getSuperLikeTimes() {
        return superLikeTimes;
    }

    public void setSuperLikeTimes(Integer superLikeTimes) {
        this.superLikeTimes = superLikeTimes;
    }
}