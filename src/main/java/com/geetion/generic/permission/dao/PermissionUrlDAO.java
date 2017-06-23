package com.geetion.generic.permission.dao;

import com.geetion.generic.permission.dao.base.BaseDAO;
import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.PermissionUrl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jian on 2015/6/17.
 */
@Repository("geetionPermissionUrlDAO")
public interface PermissionUrlDAO extends BaseDAO<PermissionUrl, Long> {

    /**
     * 获取所有需要权限的url
     * @return
     */
    public List<PermissionUrl> selectAllUrlNeedPermission();

    List<PermissionUrl> selectUrlByPermissionList(@Param("permissionList") List<Permission> list);
}