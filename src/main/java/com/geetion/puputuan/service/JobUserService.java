package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.model.JobUser;

import java.util.List;
import java.util.Map;

public interface JobUserService {

    JobUser getJobUserById(Long id);

    JobUser getJobUser(Map param);

    List<JobUser> getJobUserList(Map param);

    PagingResult<JobUser> getJobUserPage(PageEntity pageEntity);

    boolean addJobUser(JobUser object);

    boolean updateJobUser(JobUser object);

    boolean removeJobUser(Long id);


    /**
     * 批量添加
     * @param param
     * @return
     */
    public boolean addJobUserBatch(Map param);

    /**
     * 查询用户的职业
     *
     * @param param
     * @return
     */
    public List<Job> getJobByParam(Map param);


    /**
     * 根据ids找出所有的jobsIds
     *
     * @param ids
     * @return
     */
    public List<Long> getJobIdsByUserIds(List<Long> ids);

    /**
     * 根据用户id，删除所有的相关的 Job
     *
     * @param userId
     * @return
     */
    public boolean removeJobUserByUserId(Long userId);


}
