package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.JobDAO;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.service.JobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class JobServiceImpl implements JobService {

    @Resource
    private JobDAO jobDAO;

    @Override
    public Job getJobById(Long id) {
        return jobDAO.selectByPrimaryKey(id);
    }

    @Override
    public Job getJob(Map param) {
        return jobDAO.selectOne(param);
    }

    @Override
    public List<Job> getJobList(Map param) {
        return jobDAO.selectParam(param);
    }

    @Override
    public PagingResult<Job> getJobPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Job> list = getJobList(pageEntity.getParam());
        PageInfo<Job> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addJob(Job object) {
        return jobDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateJob(Job object) {
        return jobDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeJob(Long id) {
        return jobDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean updateJobBatch(Map param) {
        return jobDAO.updateBatch(param) > 0;
    }

    @Override
    public List<Job> getJobWithSystem(Long userId) {
        return jobDAO.selectParamWithSystem(userId);
    }
}
