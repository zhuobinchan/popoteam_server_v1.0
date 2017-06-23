package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Job;

import java.util.List;
import java.util.Map;

public interface JobService {

    Job getJobById(Long id);

    Job getJob(Map param);

    List<Job> getJobList(Map param);

    PagingResult<Job> getJobPage(PageEntity pageEntity);

    boolean addJob(Job object);

    boolean updateJob(Job object);

    boolean removeJob(Long id);

    /**
     * 批量更新
     *
     * @param param
     * @return
     */
    public boolean updateJobBatch(Map param);

    /**
     * 查询系统创建的还有用户自己的职业
     *
     * @param userId
     * @return
     */
    public List<Job> getJobWithSystem(Long userId);

}
