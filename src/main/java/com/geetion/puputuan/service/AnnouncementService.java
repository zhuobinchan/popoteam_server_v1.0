package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Announcement;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {

    Announcement getAnnouncementById(Long id);

    Announcement getAnnouncement(Map param);

    List<Announcement> getAnnouncementList(Map param);

    PagingResult<Announcement> getAnnouncementPage(PageEntity pageEntity);

    boolean addAnnouncement(Announcement object);

    boolean updateAnnouncement(Announcement object);

    boolean removeAnnouncement(Long id);
}
