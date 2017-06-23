package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Evaluate;

import java.util.List;
import java.util.Map;

public interface EvaluateService {

    Evaluate getEvaluateById(Long id);

    Evaluate getEvaluate(Map param);

    List<Evaluate> getEvaluateList(Map param);

    PagingResult<Evaluate> getEvaluatePage(PageEntity pageEntity);

    boolean addEvaluate(Evaluate object);

    boolean updateEvaluate(Evaluate object);

    boolean removeEvaluate(Long id);


    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean addEvaluateBatch(List<Evaluate> list);

    /**
     * 计算评价的数量
     *
     * @param evaluate
     * @return
     */
    public int countEvaluateNum(Evaluate evaluate);
}
