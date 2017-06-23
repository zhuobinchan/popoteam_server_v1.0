package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AppSettingDAO;
import com.geetion.puputuan.model.AppSetting;
import com.geetion.puputuan.service.AppSettingService;
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
public class AppSettingServiceImpl implements AppSettingService {

    @Resource
    private AppSettingDAO appSettingDAO;

    @Override
    public AppSetting getAppSettingById(Long id) {
        return appSettingDAO.selectByPrimaryKey(id);
    }

    @Override
    public AppSetting getAppSetting(Map param){
        return appSettingDAO.selectOne(param);
    }

    @Override
    public List<AppSetting> getAppSettingList(Map param) {
        return appSettingDAO.selectParam(param);
    }

    @Override
    public PagingResult<AppSetting> getAppSettingPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<AppSetting> list = getAppSettingList(pageEntity.getParam());
        PageInfo<AppSetting> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAppSetting(AppSetting object) {
        return appSettingDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAppSetting(AppSetting object) {
        return appSettingDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAppSetting(Long id) {
        return appSettingDAO.deleteByPrimaryKey(id) == 1;
    }
}
