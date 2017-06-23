package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.ActivityMember;
import com.geetion.puputuan.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityMemberDAO extends BaseDAO<ActivityMember, Long> {

    List<ActivityMember> selectActivityMemberByParam(Map param);

    void update(ActivityMember activityMember);

    int deleteActivityMemberBatch(List<Activity> list);

}