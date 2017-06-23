package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Location;

import java.util.List;
import java.util.Map;

public interface LocationService {

    Location getLocationById(Long id);

    Location getLocation(Map param);

    List<Location> getLocationList(Map param);

    PagingResult<Location> getLocationPage(PageEntity pageEntity);

    boolean addLocation(Location object);

    boolean updateLocation(Location object);

    boolean removeLocation(Long id);
}
