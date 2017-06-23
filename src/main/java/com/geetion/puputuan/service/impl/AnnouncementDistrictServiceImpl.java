package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AnnouncementDistrictDAO;
import com.geetion.puputuan.model.AnnouncementDistrict;
import com.geetion.puputuan.service.AnnouncementDistrictService;
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
public class AnnouncementDistrictServiceImpl implements AnnouncementDistrictService {

    @Resource
    private AnnouncementDistrictDAO announcementDistrictDAO;

    @Override
    public AnnouncementDistrict getAnnouncementDistrictById(Long id) {
        return announcementDistrictDAO.selectByPrimaryKey(id);
    }

    @Override
    public AnnouncementDistrict getAnnouncementDistrict(Map param) {
        return announcementDistrictDAO.selectOne(param);
    }

    @Override
    public List<AnnouncementDistrict> getAnnouncementDistrictList(Map param) {
        return announcementDistrictDAO.selectParam(param);
    }

    @Override
    public PagingResult<AnnouncementDistrict> getAnnouncementDistrictPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<AnnouncementDistrict> list = getAnnouncementDistrictList(pageEntity.getParam());
        PageInfo<AnnouncementDistrict> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAnnouncementDistrict(AnnouncementDistrict object) {
        return announcementDistrictDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAnnouncementDistrict(AnnouncementDistrict object) {
        return announcementDistrictDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAnnouncementDistrict(Long id) {
        return announcementDistrictDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addAnnouncementDistrictBatch(List<AnnouncementDistrict> list) {
        return announcementDistrictDAO.insertBatch(list) == list.size();
    }
}
