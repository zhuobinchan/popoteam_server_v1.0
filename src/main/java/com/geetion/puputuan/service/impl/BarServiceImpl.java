package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.BarDAO;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.service.BarService;
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
public class BarServiceImpl implements BarService {

    @Resource
    private BarDAO barDAO;

    @Override
    public Bar getBarById(Long id) {
        return barDAO.selectByPrimaryKey(id);
    }

    @Override
    public Bar getBar(Map param){
        return barDAO.selectOne(param);
    }

    @Override
    public List<Bar> getBarList(Map param) {
        return barDAO.selectParam(param);
    }

    @Override
    public PagingResult<Bar> getBarPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Bar> list = getBarList(pageEntity.getParam());
        PageInfo<Bar> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addBar(Bar object) {
        return barDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateBar(Bar object) {
        return barDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeBar(Long id) {
        return barDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public List<Bar> getOtherBars(Map param) {
        return barDAO.selectBarNotInIds(param);
    }
}
