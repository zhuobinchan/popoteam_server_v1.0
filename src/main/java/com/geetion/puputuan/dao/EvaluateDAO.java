package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Evaluate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluateDAO extends BaseDAO<Evaluate, Long> {

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public int insertBatch(List<Evaluate> list);


    /**
     * 计算评价的数量
     *
     * @param evaluate
     * @return
     */
    public int countNum(Evaluate evaluate);


}