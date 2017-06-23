package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.SignDAO;
import com.geetion.puputuan.model.Sign;
import com.geetion.puputuan.service.SignService;
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
public class SignServiceImpl implements SignService {

    @Resource
    private SignDAO signDAO;

    @Override
    public Sign getSignById(Long id) {
        return signDAO.selectByPrimaryKey(id);
    }

    @Override
    public Sign getSign(Map param) {
        return signDAO.selectOne(param);
    }

    @Override
    public List<Sign> getSignList(Map param) {
        return signDAO.selectParam(param);
    }

    @Override
    public PagingResult<Sign> getSignPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Sign> list = getSignList(pageEntity.getParam());
        PageInfo<Sign> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addSign(Sign object) {
        return signDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateSign(Sign object) {
        return signDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeSign(Long id) {
        return signDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Map getMaxArea(Long userId) {
        return signDAO.selectMaxSignArea(userId);
    }

    @Override
    public int getLatAndLngDistance(Map param) {
        return signDAO.selectLatAndLngDistance(param);
    }

    @Override
    public boolean removeSignByParam(Map param) {
        return signDAO.deleteByParam(param) > 0;
    }
}
