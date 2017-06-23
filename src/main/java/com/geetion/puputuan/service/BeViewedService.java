package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.BeViewed;

import java.util.List;
import java.util.Map;

public interface BeViewedService {

    BeViewed getBeViewedById(Long id);

    BeViewed getBeViewed(Map param);

    List<BeViewed> getBeViewedList(Map param);

    PagingResult<BeViewed> getBeViewedPage(PageEntity pageEntity);

    boolean addBeViewed(BeViewed object);

    boolean updateBeViewed(BeViewed object);

    boolean removeBeViewed(Long id);
}
