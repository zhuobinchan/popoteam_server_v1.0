package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.dao.RoleDAO;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.service.RoleService;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
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
@Service("geetionRoleService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class RoleServiceImpl implements RoleService {


    @Resource(name = "geetionRoleDAO")
    private RoleDAO roleDAO;

    @Override
    public boolean add(Role object) {
        return roleDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return roleDAO.delete(id) > 0;
    }

    @Override
    public boolean update(Role object) {
        return roleDAO.update(object) > 0;
    }

    @Override
    public Role getById(Long id) {
        return roleDAO.selectPk(id);
    }

    @Override
    public Role getByRole(String role) {
        return roleDAO.selectByRole(role);
    }

    @Override
    public List<Role> getAll() {
        return roleDAO.select();
    }

    @Override
    public PagingResult<Role> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Role> list = roleDAO.selectParam(pageEntity.getParam());
        PageInfo<Role> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<Role> getByParam(Map<String, Object> params) {
        return roleDAO.selectParam(params);
    }
}

