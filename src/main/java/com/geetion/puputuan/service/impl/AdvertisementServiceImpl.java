package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AdvertisementDAO;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.service.AdvertisementService;
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
public class AdvertisementServiceImpl implements AdvertisementService {

    @Resource
    private AdvertisementDAO advertisementDAO;

    @Override
    public Advertisement getAdvertisementById(Long id) {
        return advertisementDAO.selectByPrimaryKey(id);
    }

    @Override
    public Advertisement getAdvertisement(Map param){
        return advertisementDAO.selectOne(param);
    }

    @Override
    public List<Advertisement> getAdvertisementList(Map param) {
        return advertisementDAO.selectParam(param);
    }

    @Override
    public PagingResult<Advertisement> getAdvertisementPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Advertisement> list = getAdvertisementList(pageEntity.getParam());
        PageInfo<Advertisement> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAdvertisement(Advertisement object) {
        return advertisementDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAdvertisement(Advertisement object) {
        return advertisementDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAdvertisement(Long id) {
        return advertisementDAO.deleteByPrimaryKey(id) == 1;
    }
}
