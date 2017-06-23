package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Fancy;
import com.geetion.puputuan.model.Fancy;

import java.util.List;
import java.util.Map;

public interface FancyService {

    Fancy getFancyByName(String name);

    boolean addFancy(Fancy fancy);

    List<Fancy> getFancyWithSystem(Long userId);

    Fancy getFancyById(Long id);

    Fancy getFancy(Map param);

    List<Fancy> getFancyList(Map param);

    PagingResult<Fancy> getFancyPage(PageEntity pageEntity);

    boolean updateFancy(Fancy object);

    boolean removeFancy(Long id);
    /**
     * 批量更新
     *
     * @param param
     * @return
     */
    public boolean updateFancyBatch(Map param);
}
