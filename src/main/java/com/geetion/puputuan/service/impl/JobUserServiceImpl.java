package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.JobUserDAO;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.model.JobUser;
import com.geetion.puputuan.service.JobUserService;
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
public class JobUserServiceImpl implements JobUserService {

    @Resource
    private JobUserDAO jobUserDAO;

    @Override
    public JobUser getJobUserById(Long id) {
        return jobUserDAO.selectByPrimaryKey(id);
    }

    @Override
    public JobUser getJobUser(Map param) {
        return jobUserDAO.selectOne(param);
    }

    @Override
    public List<JobUser> getJobUserList(Map param) {
        return jobUserDAO.selectParam(param);
    }

    @Override
    public PagingResult<JobUser> getJobUserPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<JobUser> list = getJobUserList(pageEntity.getParam());
        PageInfo<JobUser> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addJobUser(JobUser object) {
        return jobUserDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateJobUser(JobUser object) {
        return jobUserDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeJobUser(Long id) {
        return jobUserDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addJobUserBatch(Map param) {
        return jobUserDAO.insertBatch(param) > 0;
    }

    @Override
    public List<Job> getJobByParam(Map param) {
        return jobUserDAO.selectWithJobByParam(param);
    }

    @Override
    public List<Long> getJobIdsByUserIds(List<Long> ids) {
        return jobUserDAO.selectJobsByIds(ids);
    }

    @Override
    public boolean removeJobUserByUserId(Long userId) {
        return jobUserDAO.deleteByUserId(userId) > 0;
    }
}
