package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.UpdateLocation;

import java.util.List;
import java.util.Map;

public interface UpdateLocationService {

    UpdateLocation getUpdateLocationById(Long id);

    UpdateLocation getUpdateLocation(Map param);

    List<UpdateLocation> getUpdateLocationList(Map param);

    PagingResult<UpdateLocation> getUpdateLocationPage(PageEntity pageEntity);

    boolean addUpdateLocation(UpdateLocation object);

    boolean updateUpdateLocation(UpdateLocation object);

    boolean removeUpdateLocation(Long id);
}
