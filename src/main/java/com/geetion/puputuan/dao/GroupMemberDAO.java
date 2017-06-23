package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.pojo.GroupMemberWithSign;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupMemberDAO extends BaseDAO<GroupMember, Long> {


    /**
     * 批量插入群组成员
     *
     * @param list
     * @return
     */
    public int insertBatch(List<GroupMember> list);

    /**
     * 查询在群组内，并且有过签到记录的用户userId
     *
     * @param param
     * @return
     */
    public List<Long> selectWithSignUserId(Map param);

    /**
     * 查询群组成员，并且查询该成员是否已签到过
     *
     * @param param
     * @return
     */
    public List<GroupMemberWithSign> selectWithSign(Map param);

    public int updateGroupMember(GroupMember groupMember);

    public int deleteByParam (Map param);
}