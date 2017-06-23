package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.MatchWeight;

import java.util.List;
import java.util.Map;

public interface MatchWeightService {

    MatchWeight getMatchWeightById(Long id);

    MatchWeight getMatchWeight(Map param);

    List<MatchWeight> getMatchWeightList(Map param);

    PagingResult<MatchWeight> getMatchWeightPage(PageEntity pageEntity);

    boolean addMatchWeight(MatchWeight object);

    boolean updateMatchWeight(MatchWeight object);

    boolean removeMatchWeight(Long id);
}
