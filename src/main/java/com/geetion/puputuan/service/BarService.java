package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Bar;

import java.util.List;
import java.util.Map;

public interface BarService {

    Bar getBarById(Long id);

    Bar getBar(Map param);

    List<Bar> getBarList(Map param);

    PagingResult<Bar> getBarPage(PageEntity pageEntity);

    boolean addBar(Bar object);

    boolean updateBar(Bar object);

    boolean removeBar(Long id);

    List<Bar> getOtherBars(Map param);
}
