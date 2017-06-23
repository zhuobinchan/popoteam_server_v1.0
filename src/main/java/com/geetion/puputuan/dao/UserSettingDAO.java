package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.jsonModel.UserSetting;
import org.springframework.stereotype.Repository;

/**
 * Created by Simon on 2016/8/3.
 */
@Repository
public interface UserSettingDAO extends BaseDAO<UserSetting, Long> {

}
