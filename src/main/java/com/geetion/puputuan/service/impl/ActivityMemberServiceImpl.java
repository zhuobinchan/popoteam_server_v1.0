package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.ActivityMemberDAO;
import com.geetion.puputuan.model.ActivityMember;
import com.geetion.puputuan.service.ActivityMemberService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ActivityMemberServiceImpl implements ActivityMemberService {

    @Resource
    private ActivityMemberDAO activityMemberDAO;


    @Override
    public boolean addActivityMemberBatch(List<ActivityMember> activityMemberList) {
        return activityMemberDAO.insertList(activityMemberList) == activityMemberList.size();
    }

    @Override
    public List<ActivityMember> getActivityMemberByParam(Map param) {
        return activityMemberDAO.selectActivityMemberByParam(param);
    }

    @Override
    public void update(ActivityMember activityMember) {
        activityMemberDAO.updateByPrimaryKey(activityMember);
    }
}
