package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.ActivityDAO;
import com.geetion.puputuan.dao.ActivityHistoryDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.service.ActivityHistoryService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ActivityHistoryServiceImpl implements ActivityHistoryService {

    @Resource
    private ActivityHistoryDAO activityHistoryDAO;
    @Override
    public int insertActivityHistory(List<Activity> list) {

        return activityHistoryDAO.insertIntoFromActivity(list);
    }
}
