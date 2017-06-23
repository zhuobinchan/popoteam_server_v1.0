package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.ActivityMember;
import com.geetion.puputuan.model.User;

import java.util.List;
import java.util.Map;

public interface ActivityMemberService {

    boolean addActivityMemberBatch(List<ActivityMember> activityMemberList);

    List<ActivityMember> getActivityMemberByParam(Map param);

    void update(ActivityMember activityMember);
}
