package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.ComplainDAO;
import com.geetion.puputuan.model.Complain;
import com.geetion.puputuan.pojo.ComplainWithPhotoAndUser;
import com.geetion.puputuan.service.ComplainService;
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
public class ComplainServiceImpl implements ComplainService {

    @Resource
    private ComplainDAO complainDAO;

    @Override
    public Complain getComplainById(Long id) {
        return complainDAO.selectByPrimaryKey(id);
    }

    @Override
    public Complain getComplain(Map param) {
        return complainDAO.selectOne(param);
    }

    @Override
    public List<Complain> getComplainList(Map param) {
        return complainDAO.selectParam(param);
    }

    @Override
    public PagingResult<Complain> getComplainPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Complain> list = getComplainList(pageEntity.getParam());
        PageInfo<Complain> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addComplain(Complain object) {
        return complainDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateComplain(Complain object) {
        return complainDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeComplain(Long id) {
        return complainDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public List<ComplainWithPhotoAndUser> getPhotoUserList(Map param) {
        return complainDAO.selecParamtWithPhotoUser(param);
    }

    @Override
    public PagingResult<ComplainWithPhotoAndUser> getPhotoUserPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<ComplainWithPhotoAndUser> list = getPhotoUserList(pageEntity.getParam());
        PageInfo<ComplainWithPhotoAndUser> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<Complain> getComplainUserList(Map param) {
        return complainDAO.selectParamWithComplainUser(param);
    }
}
