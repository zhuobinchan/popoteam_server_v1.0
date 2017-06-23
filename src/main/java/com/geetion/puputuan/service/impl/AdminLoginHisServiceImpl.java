package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AdminLoginHisDAO;
import com.geetion.puputuan.model.AdminLoginHis;
import com.geetion.puputuan.service.AdminLoginHisService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class AdminLoginHisServiceImpl implements AdminLoginHisService {

    @Resource
    private AdminLoginHisDAO adminLoginHisDAO;

    @Override
    public boolean addAdminLoginHis(AdminLoginHis object) {
        return adminLoginHisDAO.insert(object) == 1;
    }

    @Override
    public List<AdminLoginHis> getAdminLoginHis(Map param) {
        return adminLoginHisDAO.selectAdminLoginHisByParam(param);
    }

    @Override
    public PagingResult<AdminLoginHis> getAdminLoginHisPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<AdminLoginHis> list = getAdminLoginHis(pageEntity.getParam());
        PageInfo<AdminLoginHis> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public void deleteAdminLoginBatch(List<Long> userIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("userIds", userIdList);
        adminLoginHisDAO.deleteBatch(params);
    }
}
