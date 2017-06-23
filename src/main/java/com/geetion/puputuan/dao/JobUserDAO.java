package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.model.JobUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JobUserDAO extends BaseDAO<JobUser, Long> {

    /**
     * 批量添加
     *
     * @param param
     * @return
     */
    public int insertBatch(Map param);

    /**
     * 查询用户的职业
     *
     * @param param
     * @return
     */
    public List<Job> selectWithJobByParam(Map param);

    /**
     * 查询userids里面所有的job_id
     *
     * @param ids
     * @return
     */
    public List<Long> selectJobsByIds(@Param("ids") List<Long> ids);

    /**
     * 根据用户id，删除所有的相关的 job
     *
     * @param userId
     * @return
     */
    public int deleteByUserId(@Param("userId") Long userId);
}