package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Sign;

import java.util.List;
import java.util.Map;

public interface SignService {

    Sign getSignById(Long id);

    Sign getSign(Map param);

    List<Sign> getSignList(Map param);

    PagingResult<Sign> getSignPage(PageEntity pageEntity);

    boolean addSign(Sign object);

    boolean updateSign(Sign object);

    boolean removeSign(Long id);

    Map getMaxArea(Long userId);


    /**
     * 查询在一个约会中，男女双方是否有一对，之间的距离小于给定的值，无则返回0，有则返回多少人
     *
     * @param param 包括 activityId 约会id 和 distance 距离
     * @return
     */
    public int getLatAndLngDistance(Map param);

    /**
     * 删除签到记录
     *
     * @param param
     * @return
     */
    public boolean removeSignByParam(Map param);
}
