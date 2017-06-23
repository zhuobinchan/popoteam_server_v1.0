package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.EvaluateDAO;
import com.geetion.puputuan.model.Evaluate;
import com.geetion.puputuan.service.EvaluateService;
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
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateDAO evaluateDAO;

    @Override
    public Evaluate getEvaluateById(Long id) {
        return evaluateDAO.selectByPrimaryKey(id);
    }

    @Override
    public Evaluate getEvaluate(Map param) {
        return evaluateDAO.selectOne(param);
    }

    @Override
    public List<Evaluate> getEvaluateList(Map param) {
        return evaluateDAO.selectParam(param);
    }

    @Override
    public PagingResult<Evaluate> getEvaluatePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Evaluate> list = getEvaluateList(pageEntity.getParam());
        PageInfo<Evaluate> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addEvaluate(Evaluate object) {
        return evaluateDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateEvaluate(Evaluate object) {
        return evaluateDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeEvaluate(Long id) {
        return evaluateDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addEvaluateBatch(List<Evaluate> list) {
        return evaluateDAO.insertBatch(list) == list.size();
    }

    @Override
    public int countEvaluateNum(Evaluate evaluate) {
        return evaluateDAO.countNum(evaluate);
    }
}
