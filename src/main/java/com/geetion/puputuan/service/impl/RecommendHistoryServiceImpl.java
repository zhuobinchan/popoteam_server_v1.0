package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.RecommendHistoryDAO;
import com.geetion.puputuan.model.RecommendHistory;
import com.geetion.puputuan.service.RecommendHistoryService;
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
public class RecommendHistoryServiceImpl implements RecommendHistoryService {

    @Resource
    private RecommendHistoryDAO recommendHistoryDAO;

    @Override
    public RecommendHistory getRecommendHistoryById(Long id) {
        return recommendHistoryDAO.selectByPrimaryKey(id);
    }

    @Override
    public RecommendHistory getRecommendHistory(Map param){
        return recommendHistoryDAO.selectOne(param);
    }

    @Override
    public List<RecommendHistory> getRecommendHistoryList(Map param) {
        return recommendHistoryDAO.selectParam(param);
    }

    @Override
    public PagingResult<RecommendHistory> getRecommendHistoryPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<RecommendHistory> list = getRecommendHistoryList(pageEntity.getParam());
        PageInfo<RecommendHistory> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addRecommendHistory(RecommendHistory object) {
        return recommendHistoryDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateRecommendHistory(RecommendHistory object) {
        return recommendHistoryDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeRecommendHistory(Long id) {
        return recommendHistoryDAO.deleteByPrimaryKey(id) == 1;
    }
}
