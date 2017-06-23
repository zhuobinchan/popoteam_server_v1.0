package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.AnnouncementDAO;
import com.geetion.puputuan.model.Announcement;
import com.geetion.puputuan.service.AnnouncementService;
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
public class AnnouncementServiceImpl implements AnnouncementService {

    @Resource
    private AnnouncementDAO announcementDAO;

    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementDAO.selectByPrimaryKey(id);
    }

    @Override
    public Announcement getAnnouncement(Map param){
        return announcementDAO.selectOne(param);
    }

    @Override
    public List<Announcement> getAnnouncementList(Map param) {
        return announcementDAO.selectParam(param);
    }

    @Override
    public PagingResult<Announcement> getAnnouncementPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Announcement> list = getAnnouncementList(pageEntity.getParam());
        PageInfo<Announcement> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addAnnouncement(Announcement object) {
        return announcementDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateAnnouncement(Announcement object) {
        return announcementDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeAnnouncement(Long id) {
        return announcementDAO.deleteByPrimaryKey(id) == 1;
    }
}
