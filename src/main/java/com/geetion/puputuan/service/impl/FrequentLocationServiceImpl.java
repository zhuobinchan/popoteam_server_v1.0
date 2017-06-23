package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.FrequentLocationDAO;
import com.geetion.puputuan.model.FrequentLocation;
import com.geetion.puputuan.service.FrequentLocationService;
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
public class FrequentLocationServiceImpl implements FrequentLocationService {

    @Resource
    private FrequentLocationDAO frequentLocationDAO;

    @Override
    public FrequentLocation getFrequentLocationById(Long id) {
        return frequentLocationDAO.selectByPrimaryKey(id);
    }

    @Override
    public FrequentLocation getFrequentLocation(Map param){
        return frequentLocationDAO.selectOne(param);
    }

    @Override
    public List<FrequentLocation> getFrequentLocationList(Map param) {
        return frequentLocationDAO.selectParam(param);
    }

    @Override
    public PagingResult<FrequentLocation> getFrequentLocationPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<FrequentLocation> list = getFrequentLocationList(pageEntity.getParam());
        PageInfo<FrequentLocation> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addFrequentLocation(FrequentLocation object) {
        return frequentLocationDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateFrequentLocation(FrequentLocation object) {
        return frequentLocationDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFrequentLocation(Long id) {
        return frequentLocationDAO.deleteByPrimaryKey(id) == 1;
    }
}
