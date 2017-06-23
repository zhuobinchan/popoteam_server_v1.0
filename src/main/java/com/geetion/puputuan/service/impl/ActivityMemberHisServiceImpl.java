package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.ActivityHistoryDAO;
import com.geetion.puputuan.dao.ActivityMemberHisDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.ActivityMemberHis;
import com.geetion.puputuan.service.ActivityHistoryService;
import com.geetion.puputuan.service.ActivityMemberHisService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ActivityMemberHisServiceImpl implements ActivityMemberHisService {

    @Resource
    private ActivityMemberHisDAO activityMemberHisDAO;

    @Override
    public int insertActivityMemberHistory(List<Activity> list) {
        return activityMemberHisDAO.insertIntoFromActivityMember(list);
    }
}
