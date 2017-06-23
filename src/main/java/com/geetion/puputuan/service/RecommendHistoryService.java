package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.RecommendHistory;

import java.util.List;
import java.util.Map;

public interface RecommendHistoryService {

    RecommendHistory getRecommendHistoryById(Long id);

    RecommendHistory getRecommendHistory(Map param);

    List<RecommendHistory> getRecommendHistoryList(Map param);

    PagingResult<RecommendHistory> getRecommendHistoryPage(PageEntity pageEntity);

    boolean addRecommendHistory(RecommendHistory object);

    boolean updateRecommendHistory(RecommendHistory object);

    boolean removeRecommendHistory(Long id);
}
