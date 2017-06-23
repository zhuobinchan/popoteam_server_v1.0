package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.AppSetting;

import java.util.List;
import java.util.Map;

public interface AppSettingService {

    AppSetting getAppSettingById(Long id);

    AppSetting getAppSetting(Map param);

    List<AppSetting> getAppSettingList(Map param);

    PagingResult<AppSetting> getAppSettingPage(PageEntity pageEntity);

    boolean addAppSetting(AppSetting object);

    boolean updateAppSetting(AppSetting object);

    boolean removeAppSetting(Long id);
}
