package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.RecommendSuccessDAO;
import com.geetion.puputuan.model.RecommendSuccess;
import com.geetion.puputuan.service.RecommendSuccessService;
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
public class RecommendSuccessServiceImpl implements RecommendSuccessService {

    @Resource
    private RecommendSuccessDAO recommendSuccessDAO;

    @Override
    public RecommendSuccess getRecommendSuccessById(Long id) {
        return recommendSuccessDAO.selectByPrimaryKey(id);
    }

    @Override
    public RecommendSuccess getRecommendSuccess(Map param){
        return recommendSuccessDAO.selectOne(param);
    }

    @Override
    public List<RecommendSuccess> getRecommendSuccessList(Map param) {
        return recommendSuccessDAO.selectParam(param);
    }

    @Override
    public PagingResult<RecommendSuccess> getRecommendSuccessPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<RecommendSuccess> list = getRecommendSuccessList(pageEntity.getParam());
        PageInfo<RecommendSuccess> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addRecommendSuccess(RecommendSuccess object) {
        return recommendSuccessDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateRecommendSuccess(RecommendSuccess object) {
        return recommendSuccessDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeRecommendSuccess(Long id) {
        return recommendSuccessDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public int removeRecommendSuccessByMainGroup(Long mainGroupId) {
        return recommendSuccessDAO.deleteRecommendSuccessByMainGroup(mainGroupId);
    }
}
