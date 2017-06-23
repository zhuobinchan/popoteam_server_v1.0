package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.UpdateLocationDAO;
import com.geetion.puputuan.model.UpdateLocation;
import com.geetion.puputuan.service.UpdateLocationService;
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
public class UpdateLocationServiceImpl implements UpdateLocationService {

    @Resource
    private UpdateLocationDAO updateLocationDAO;

    @Override
    public UpdateLocation getUpdateLocationById(Long id) {
        return updateLocationDAO.selectByPrimaryKey(id);
    }

    @Override
    public UpdateLocation getUpdateLocation(Map param){
        return updateLocationDAO.selectOne(param);
    }

    @Override
    public List<UpdateLocation> getUpdateLocationList(Map param) {
        return updateLocationDAO.selectParam(param);
    }

    @Override
    public PagingResult<UpdateLocation> getUpdateLocationPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UpdateLocation> list = getUpdateLocationList(pageEntity.getParam());
        PageInfo<UpdateLocation> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addUpdateLocation(UpdateLocation object) {
        return updateLocationDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateUpdateLocation(UpdateLocation object) {
        return updateLocationDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeUpdateLocation(Long id) {
        return updateLocationDAO.deleteByPrimaryKey(id) == 1;
    }
}
