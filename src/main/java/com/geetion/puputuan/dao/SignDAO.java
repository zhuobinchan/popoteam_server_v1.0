package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Sign;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SignDAO extends BaseDAO<Sign, Long> {

    public Map selectMaxSignArea(Long user_id);


    /**
     * 查询在一个约会中，男女双方是否有一对，之间的距离小于给定的值，无则返回0，有则返回多少人
     *
     * @param param 包括 activityId 约会id 和 distance 距离
     * @return
     */
    public int selectLatAndLngDistance(Map param);

    /**
     * 删除签到记录
     *
     * @param param
     * @return
     */
    public int deleteByParam(Map param);

}