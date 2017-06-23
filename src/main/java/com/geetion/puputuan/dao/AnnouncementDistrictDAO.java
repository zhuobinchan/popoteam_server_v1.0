package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.AnnouncementDistrict;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AnnouncementDistrictDAO extends BaseDAO<AnnouncementDistrict, Long> {


    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public int insertBatch(List<AnnouncementDistrict> list);
}