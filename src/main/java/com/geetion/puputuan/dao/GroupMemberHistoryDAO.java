package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.GroupMemberHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberHistoryDAO extends BaseDAO<GroupMemberHistory, Long> {


    /**
     * 批量插入群组成员进历史记录
     *
     * @param list
     * @return
     */
    public int insertBatch(List<GroupMemberHistory> list);
}