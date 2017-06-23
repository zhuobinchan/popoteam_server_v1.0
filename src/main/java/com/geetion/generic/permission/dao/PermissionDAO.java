package com.geetion.generic.permission.dao;

import com.geetion.generic.permission.dao.base.BaseDAO;
import com.geetion.generic.permission.pojo.Permission;
import org.springframework.stereotype.Repository;

/**
 * Created by jian on 2015/6/17.
 */
@Repository("geetionPermissionDAO")
public interface PermissionDAO extends BaseDAO<Permission, Long> {

}