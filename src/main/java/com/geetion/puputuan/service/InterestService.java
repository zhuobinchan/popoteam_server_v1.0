package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Interest;

import java.util.List;
import java.util.Map;

public interface InterestService {

    Interest getInterestById(Long id);

    Interest getInterest(Map param);

    List<Interest> getInterestList(Map param);

    PagingResult<Interest> getInterestPage(PageEntity pageEntity);

    boolean addInterest(Interest object);

    boolean updateInterest(Interest object);

    boolean removeInterest(Long id);


    /**
     * 批量更新
     *
     * @param param
     * @return
     */
    public boolean updateInterestBatch(Map param);


    /**
     * 查询系统创建的还有用户自己的兴趣
     *
     * @param userId
     * @return
     */
    public List<Interest> getInterestWithSystem(Long userId);

}
