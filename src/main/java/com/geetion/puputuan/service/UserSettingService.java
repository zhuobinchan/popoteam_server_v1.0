package com.geetion.puputuan.service;

import com.geetion.puputuan.model.jsonModel.UserSetting;

import java.util.Map;

/**
 * Created by Simon on 2016/8/3.
 */
public interface UserSettingService {

    UserSetting getSettingById(Long id);

    UserSetting getSetting(Map param);

    boolean addSetting(UserSetting object);

    boolean updateSetting(UserSetting object);
}
