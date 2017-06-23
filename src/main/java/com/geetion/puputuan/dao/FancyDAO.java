package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Fancy;
import com.geetion.puputuan.model.Job;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FancyDAO extends BaseDAO<Fancy, Long> {
    /**
     * 查询系统创建的还有用户自己的职业
     *
     * @param userId
     * @return
     */
    public List<Fancy> selectParamWithSystem(@Param("userId") Long userId);

    /**
     * 批量更新
     *
     * @param param
     * @return
     */
    public int updateBatch(Map param);
}