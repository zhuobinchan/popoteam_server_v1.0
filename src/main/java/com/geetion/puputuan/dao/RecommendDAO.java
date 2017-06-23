package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Recommend;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecommendDAO extends BaseDAO<Recommend, Long> {
    public int deleteByMainId(Long id);

    /**
     * 查询推荐队伍，根据计算出来的分数排序
     */
    public List<Recommend> selectParamByScore(Map param);

    /**
     * 汇总某个时间段的队伍推荐信息
     * @param param
     * @return
     */
    int sumGroupRecommend(Map param);
}