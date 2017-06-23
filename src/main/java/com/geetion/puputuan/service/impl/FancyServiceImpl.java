package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.FancyDAO;
import com.geetion.puputuan.model.Fancy;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.service.FancyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class FancyServiceImpl implements FancyService {

    @Resource
    private FancyDAO fancyDAO;

    private Map<String, Object> params;

    @Override
    public Fancy getFancyByName(String name) {
        params = new HashMap<>();
        params.put("name", name);
        return fancyDAO.selectOne(params);
    }

    @Override
    public boolean addFancy(Fancy fancy) {

        return fancyDAO.insertSelective(fancy) == 1;

    }

    @Override
    public List<Fancy> getFancyWithSystem(Long userId) {
         return fancyDAO.selectParamWithSystem(userId);
    }

    @Override
    public Fancy getFancyById(Long id) {
        return fancyDAO.selectByPrimaryKey(id);
    }

    @Override
    public Fancy getFancy(Map param) {
        return fancyDAO.selectOne(param);
    }

    @Override
    public List<Fancy> getFancyList(Map param) {
        return fancyDAO.selectParam(param);
    }

    @Override
    public PagingResult<Fancy> getFancyPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Fancy> list = getFancyList(pageEntity.getParam());
        PageInfo<Fancy> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean updateFancy(Fancy object) {
        return fancyDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFancy(Long id) {
        return fancyDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean updateFancyBatch(Map param) {
        return fancyDAO.updateBatch(param) > 0;
    }
}
