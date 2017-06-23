package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.AppVersion;

import java.util.List;
import java.util.Map;

public interface AppVersionService {

    AppVersion getAppVersionById(Long id);

    AppVersion getAppVersion(Map param);

    List<AppVersion> getAppVersionList(Map param);

    PagingResult<AppVersion> getAppVersionPage(PageEntity pageEntity);

    boolean addAppVersion(AppVersion object);

    boolean updateAppVersion(AppVersion object);

    boolean removeAppVersion(Long id);
}
