package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AppVersionDAO;
import com.geetion.puputuan.model.AppVersion;
import com.geetion.puputuan.service.AppVersionService;
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
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionDAO appVersionDAO;

    @Override
    public AppVersion getAppVersionById(Long id) {
        return appVersionDAO.selectByPrimaryKey(id);
    }

    @Override
    public AppVersion getAppVersion(Map param){
        return appVersionDAO.selectOne(param);
    }

    @Override
    public List<AppVersion> getAppVersionList(Map param) {
        return appVersionDAO.selectParam(param);
    }

    @Override
    public PagingResult<AppVersion> getAppVersionPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<AppVersion> list = getAppVersionList(pageEntity.getParam());
        PageInfo<AppVersion> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAppVersion(AppVersion object) {
        return appVersionDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAppVersion(AppVersion object) {
        return appVersionDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAppVersion(Long id) {
        return appVersionDAO.deleteByPrimaryKey(id) == 1;
    }
}
