package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.GroupMemberHistory;

import java.util.List;
import java.util.Map;

public interface GroupMemberHistoryService {

    GroupMemberHistory getGroupMemberHistoryById(Long id);

    GroupMemberHistory getGroupMemberHistory(Map param);

    List<GroupMemberHistory> getGroupMemberHistoryList(Map param);

    PagingResult<GroupMemberHistory> getGroupMemberHistoryPage(PageEntity pageEntity);

    boolean addGroupMemberHistory(GroupMemberHistory object);

    boolean updateGroupMemberHistory(GroupMemberHistory object);

    boolean removeGroupMemberHistory(Long id);


    /**
     * 批量插入群组成员
     *
     * @param list
     * @return
     */
    public boolean addGroupMemberHistoryBatch(List<GroupMemberHistory> list);
}
