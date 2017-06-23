package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Recommend;

import java.util.List;
import java.util.Map;

public interface RecommendService {

    Recommend getRecommendById(Long id);

    Recommend getRecommend(Map param);

    List<Recommend> getRecommendList(Map param);

    PagingResult<Recommend> getRecommendPage(PageEntity pageEntity);

    boolean addRecommend(Recommend object);

    boolean updateRecommend(Recommend object);

    boolean removeRecommend(Long id);

    int removeMainIdRecommend(Long id);

    int removeMainIdMatchIdRecommend(Long mainId, Long matchId);

    /**
     * 查询推荐队伍，根据计算出来的分数排序
     */
    public List<Recommend> getRecommendListByScore(Map param);

    boolean addRecommendBatch(List<Recommend> recommends);

    int sumGroupRecommend(Long groupId);
}
