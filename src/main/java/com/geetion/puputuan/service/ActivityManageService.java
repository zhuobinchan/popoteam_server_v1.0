package com.geetion.puputuan.service;

import com.geetion.puputuan.model.Activity;

import java.util.List;

/**
 * Created by chenzhuobin on 2017/4/14.
 */
public interface ActivityManageService {
    void cleanActivityList(List<Activity> activityList);
}
