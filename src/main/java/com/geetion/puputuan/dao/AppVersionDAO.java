package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.AppVersion;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionDAO extends BaseDAO<AppVersion, Long> {

}