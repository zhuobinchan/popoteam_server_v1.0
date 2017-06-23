package com.geetion.puputuan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.geetion.puputuan.common.utils.JSONPropertyFilter;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.model.jsonModel.JSONUserBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jian on 2016/4/29.
 */
public class MessageParam {


    //好友添加的申请的id
    private Long friendApplyId;
    //群组id
    private Long groupId;
    //约会id
    private Long activityId;
    //用户
    //private User user;
    private JSONUserBase userBase;
    //用户
    private String announcementContent;
    //网站链接
    private String announcementUrl;
    //公告图片
    private String announcementImageUrl;
    public MessageParam() {
    }

    public MessageParam(Long friendApplyId, Long groupId, Long activityId, JSONUserBase userBase, String announcementContent) {
        this.friendApplyId = friendApplyId;
        this.groupId = groupId;
        this.activityId = activityId;
        this.userBase = userBase;
        this.announcementContent = announcementContent;
    }

    /**
     * 将该对象转化为json字符串
     * WriteDateUseDateFormat 表示输入日期为时间格式，否则会输出毫秒数
     * WriteMapNullValue 表示输入null值
     *
     * @return
     */
    public String toJSONString() {
        List<String> strings = new ArrayList<>();
        strings.add("token");
        strings.add("password");
        //过滤掉token跟password
        JSONPropertyFilter filter = new JSONPropertyFilter(strings);

        return JSON.toJSONString(this, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);

    }

    public Long getFriendApplyId() {
        return friendApplyId;
    }

    public void setFriendApplyId(Long friendApplyId) {
        this.friendApplyId = friendApplyId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public JSONUserBase getUserBase() {
        return userBase;
    }

    public void setUserBase(JSONUserBase userBase) {
        this.userBase = userBase;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }

    public String getAnnouncementUrl() {
        return announcementUrl;
    }

    public void setAnnouncementUrl(String announcementUrl) {
        this.announcementUrl = announcementUrl;
    }

    public String getAnnouncementImageUrl() {
        return announcementImageUrl;
    }

    public void setAnnouncementImageUrl(String announcementImageUrl) {
        this.announcementImageUrl = announcementImageUrl;
    }
}
