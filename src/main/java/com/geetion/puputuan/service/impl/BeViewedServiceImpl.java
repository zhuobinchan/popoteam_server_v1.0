package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.BeViewedDAO;
import com.geetion.puputuan.model.BeViewed;
import com.geetion.puputuan.service.BeViewedService;
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
public class BeViewedServiceImpl implements BeViewedService {

    @Resource
    private BeViewedDAO beViewedDAO;

    @Override
    public BeViewed getBeViewedById(Long id) {
        return beViewedDAO.selectByPrimaryKey(id);
    }

    @Override
    public BeViewed getBeViewed(Map param){
        return beViewedDAO.selectOne(param);
    }

    @Override
    public List<BeViewed> getBeViewedList(Map param) {
        return beViewedDAO.selectParam(param);
    }

    @Override
    public PagingResult<BeViewed> getBeViewedPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<BeViewed> list = getBeViewedList(pageEntity.getParam());
        PageInfo<BeViewed> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addBeViewed(BeViewed object) {
        return beViewedDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateBeViewed(BeViewed object) {
        return beViewedDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeBeViewed(Long id) {
        return beViewedDAO.deleteByPrimaryKey(id) == 1;
    }
}
