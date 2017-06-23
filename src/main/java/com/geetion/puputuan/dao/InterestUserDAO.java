package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Interest;
import com.geetion.puputuan.model.InterestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InterestUserDAO extends BaseDAO<InterestUser, Long> {

    /**
     * 批量添加
     *
     * @param param
     * @return
     */
    public int insertBatch(Map param);

    /**
     * 查询用户的兴趣
     *
     * @param param
     * @return
     */
    public List<Interest> selectWithInterestByParam(Map param);

    /**
     * 查出userids里面所有的兴趣集合只返回interest_id
     *
     * @param ids
     * @return
     */
    public List<Long> selectInterestIds(@Param("ids") List<Long> ids);

    /**
     * 根据用户id，删除所有的相关的 interest
     *
     * @param userId
     * @return
     */
    public int deleteByUserId(@Param("userId") Long userId);

}