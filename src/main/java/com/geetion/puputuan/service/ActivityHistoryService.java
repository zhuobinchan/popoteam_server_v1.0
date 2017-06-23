package com.geetion.puputuan.service;

import com.geetion.puputuan.model.Activity;

import java.util.List;

public interface ActivityHistoryService {

    int insertActivityHistory(List<Activity> list);
}
