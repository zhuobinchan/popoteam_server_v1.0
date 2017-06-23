package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.RecommendSuccess;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendSuccessDAO extends BaseDAO<RecommendSuccess, Long> {
    int deleteRecommendSuccessByMainGroup(Long mainGroupId);
}