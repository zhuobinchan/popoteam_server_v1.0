package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.GroupMemberHistoryDAO;
import com.geetion.puputuan.model.GroupMemberHistory;
import com.geetion.puputuan.service.GroupMemberHistoryService;
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
public class GroupMemberHistoryServiceImpl implements GroupMemberHistoryService {

    @Resource
    private GroupMemberHistoryDAO groupMemberHistoryDAO;

    @Override
    public GroupMemberHistory getGroupMemberHistoryById(Long id) {
        return groupMemberHistoryDAO.selectByPrimaryKey(id);
    }

    @Override
    public GroupMemberHistory getGroupMemberHistory(Map param){
        return groupMemberHistoryDAO.selectOne(param);
    }

    @Override
    public List<GroupMemberHistory> getGroupMemberHistoryList(Map param) {
        return groupMemberHistoryDAO.selectParam(param);
    }

    @Override
    public PagingResult<GroupMemberHistory> getGroupMemberHistoryPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<GroupMemberHistory> list = getGroupMemberHistoryList(pageEntity.getParam());
        PageInfo<GroupMemberHistory> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addGroupMemberHistory(GroupMemberHistory object) {
        return groupMemberHistoryDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateGroupMemberHistory(GroupMemberHistory object) {
        return groupMemberHistoryDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeGroupMemberHistory(Long id) {
        return groupMemberHistoryDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addGroupMemberHistoryBatch(List<GroupMemberHistory> list) {
        return groupMemberHistoryDAO.insertBatch(list) == list.size();
    }
}
