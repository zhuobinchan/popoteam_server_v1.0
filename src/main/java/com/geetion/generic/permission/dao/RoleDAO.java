package com.geetion.generic.permission.dao;

import com.geetion.generic.permission.dao.base.BaseDAO;
import com.geetion.generic.permission.pojo.Role;
import org.springframework.stereotype.Repository;

/**
 * Created by jian on 2015/6/17.
 */
@Repository("geetionRoleDAO")
public interface RoleDAO extends BaseDAO<Role, Long> {

    public Role selectByRole(String role);
}