package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.AppSetting;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSettingDAO extends BaseDAO<AppSetting, Long> {

}