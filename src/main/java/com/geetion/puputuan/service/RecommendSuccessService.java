package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.RecommendSuccess;

import java.util.List;
import java.util.Map;

public interface RecommendSuccessService {

    RecommendSuccess getRecommendSuccessById(Long id);

    RecommendSuccess getRecommendSuccess(Map param);

    List<RecommendSuccess> getRecommendSuccessList(Map param);

    PagingResult<RecommendSuccess> getRecommendSuccessPage(PageEntity pageEntity);

    boolean addRecommendSuccess(RecommendSuccess object);

    boolean updateRecommendSuccess(RecommendSuccess object);

    boolean removeRecommendSuccess(Long id);

    int removeRecommendSuccessByMainGroup(Long mainGroupId);
}
