package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.InterestDAO;
import com.geetion.puputuan.model.Interest;
import com.geetion.puputuan.service.InterestService;
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
public class InterestServiceImpl implements InterestService {

    @Resource
    private InterestDAO interestDAO;

    @Override
    public Interest getInterestById(Long id) {
        return interestDAO.selectByPrimaryKey(id);
    }

    @Override
    public Interest getInterest(Map param) {
        return interestDAO.selectOne(param);
    }

    @Override
    public List<Interest> getInterestList(Map param) {
        return interestDAO.selectParam(param);
    }

    @Override
    public PagingResult<Interest> getInterestPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Interest> list = getInterestList(pageEntity.getParam());
        PageInfo<Interest> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addInterest(Interest object) {
        return interestDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateInterest(Interest object) {
        return interestDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeInterest(Long id) {
        return interestDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean updateInterestBatch(Map param) {
        return interestDAO.updateBatch(param) > 0;
    }

    @Override
    public List<Interest> getInterestWithSystem(Long userId) {
        return interestDAO.selectParamWithSystem(userId);
    }
}
