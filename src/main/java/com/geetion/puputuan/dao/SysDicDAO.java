package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.AppSetting;
import com.geetion.puputuan.model.SysDic;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDicDAO extends BaseDAO<SysDic, Long> {
    SysDic selectByKey(String key);
}