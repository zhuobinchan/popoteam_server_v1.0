package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.dao.PermissionDAO;
import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.service.PermissionService;
import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
@Service("geetionPermissionService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class PermissionServiceImpl implements PermissionService {


    @Resource(name = "geetionPermissionDAO")
    private PermissionDAO permissionDAO;

    @Override
    public boolean add(Permission object) {
        return permissionDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return permissionDAO.delete(id) > 0;
    }

    @Override
    public boolean update(Permission object) {
        return permissionDAO.update(object) > 0;
    }

    @Override
    public Permission getById(Long id) {
        return permissionDAO.selectPk(id);
    }

    @Override
    public List<Permission> getAll() {
        return permissionDAO.select();
    }

    @Override
    public PagingResult<Permission> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Permission> list = permissionDAO.selectParam(pageEntity.getParams());
        PageInfo<Permission> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<Permission> getByParam(Map<String, Object> params) {
        return permissionDAO.selectParam(params);
    }
}

