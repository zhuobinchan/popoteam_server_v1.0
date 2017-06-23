package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AdminDAO;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDAO adminDAO;

    @Override
    public Admin getAdminById(Long id) {
        return adminDAO.selectByPrimaryKey(id);
    }

    @Override
    public Admin getAdmin(Map param){
        return adminDAO.selectOne(param);
    }

    @Override
    public List<Admin> getAdminList(Map param) {
        return adminDAO.selectParam(param);
    }

    @Override
    public PagingResult<Admin> getAdminPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Admin> list = getAdminList(pageEntity.getParam());
        PageInfo<Admin> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAdmin(Admin object) {
        return adminDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAdmin(Admin object) {
        return adminDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAdmin(Long id) {
        return adminDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Admin getAdminByUserId(Long userId) {
        return adminDAO.selectByUserId(userId);
    }
}
