package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.MatchWeightDAO;
import com.geetion.puputuan.model.MatchWeight;
import com.geetion.puputuan.service.MatchWeightService;
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
public class MatchWeightServiceImpl implements MatchWeightService {

    @Resource
    private MatchWeightDAO matchWeightDAO;

    @Override
    public MatchWeight getMatchWeightById(Long id) {
        return matchWeightDAO.selectByPrimaryKey(id);
    }

    @Override
    public MatchWeight getMatchWeight(Map param){
        return matchWeightDAO.selectOne(param);
    }

    @Override
    public List<MatchWeight> getMatchWeightList(Map param) {
        return matchWeightDAO.selectParam(param);
    }

    @Override
    public PagingResult<MatchWeight> getMatchWeightPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<MatchWeight> list = getMatchWeightList(pageEntity.getParam());
        PageInfo<MatchWeight> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addMatchWeight(MatchWeight object) {
        return matchWeightDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateMatchWeight(MatchWeight object) {
        return matchWeightDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeMatchWeight(Long id) {
        return matchWeightDAO.deleteByPrimaryKey(id) == 1;
    }
}
