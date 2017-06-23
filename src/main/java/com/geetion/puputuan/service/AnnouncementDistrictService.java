package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.AnnouncementDistrict;

import java.util.List;
import java.util.Map;

public interface AnnouncementDistrictService {

    AnnouncementDistrict getAnnouncementDistrictById(Long id);

    AnnouncementDistrict getAnnouncementDistrict(Map param);

    List<AnnouncementDistrict> getAnnouncementDistrictList(Map param);

    PagingResult<AnnouncementDistrict> getAnnouncementDistrictPage(PageEntity pageEntity);

    boolean addAnnouncementDistrict(AnnouncementDistrict object);

    boolean updateAnnouncementDistrict(AnnouncementDistrict object);

    boolean removeAnnouncementDistrict(Long id);


    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean addAnnouncementDistrictBatch(List<AnnouncementDistrict> list);
}
