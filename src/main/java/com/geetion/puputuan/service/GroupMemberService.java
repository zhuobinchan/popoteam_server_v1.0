package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.pojo.GroupMemberWithSign;

import java.util.List;
import java.util.Map;

public interface GroupMemberService {

    GroupMember getGroupMemberById(Long id);

    GroupMember getGroupMember(Map param);

    List<GroupMember> getGroupMemberList(Map param);

    PagingResult<GroupMember> getGroupMemberPage(PageEntity pageEntity);

    boolean addGroupMember(GroupMember object);

    boolean updateGroupMember(GroupMember object);

    boolean removeGroupMember(Long id);

    boolean removeGroupMemberByParam(Map param);


    /**
     * 批量插入群组成员
     *
     * @param list
     * @return
     */
    public boolean addGroupMemberBatch(List<GroupMember> list);

    /**
     * 查询在群组内，并且有过签到记录的用户userId
     *
     * @param param
     * @return
     */
    public List<Long> getGroupMemberWithSignUserId(Map param);

    /**
     * 查询群组成员，并且查询该成员是否已签到过
     *
     * @param param
     * @return
     */
    public List<GroupMemberWithSign> getGroupMemberWithSign(Map param);

    public boolean ifInOtherGroup(Long userId);

}
