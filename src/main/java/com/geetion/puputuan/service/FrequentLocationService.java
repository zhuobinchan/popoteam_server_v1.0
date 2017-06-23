package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.FrequentLocation;

import java.util.List;
import java.util.Map;

public interface FrequentLocationService {

    FrequentLocation getFrequentLocationById(Long id);

    FrequentLocation getFrequentLocation(Map param);

    List<FrequentLocation> getFrequentLocationList(Map param);

    PagingResult<FrequentLocation> getFrequentLocationPage(PageEntity pageEntity);

    boolean addFrequentLocation(FrequentLocation object);

    boolean updateFrequentLocation(FrequentLocation object);

    boolean removeFrequentLocation(Long id);
}
