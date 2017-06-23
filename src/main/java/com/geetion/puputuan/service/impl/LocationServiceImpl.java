package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.LocationDAO;
import com.geetion.puputuan.model.Location;
import com.geetion.puputuan.service.LocationService;
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
public class LocationServiceImpl implements LocationService {

    @Resource
    private LocationDAO locationDAO;

    @Override
    public Location getLocationById(Long id) {
        return locationDAO.selectByPrimaryKey(id);
    }

    @Override
    public Location getLocation(Map param){
        return locationDAO.selectOne(param);
    }

    @Override
    public List<Location> getLocationList(Map param) {
        return locationDAO.selectParam(param);
    }

    @Override
    public PagingResult<Location> getLocationPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Location> list = getLocationList(pageEntity.getParam());
        PageInfo<Location> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addLocation(Location object) {
        return locationDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateLocation(Location object) {
        return locationDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeLocation(Long id) {
        return locationDAO.deleteByPrimaryKey(id) == 1;
    }
}
