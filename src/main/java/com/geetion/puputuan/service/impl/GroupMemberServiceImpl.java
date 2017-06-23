package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.constant.GroupMemberTypeAndStatus;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.GroupMemberDAO;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.pojo.GroupMemberWithSign;
import com.geetion.puputuan.service.GroupMemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class GroupMemberServiceImpl implements GroupMemberService {

    private final Logger logger = Logger.getLogger(GroupMemberServiceImpl.class);

    @Resource
    private GroupMemberDAO groupMemberDAO;

    @Override
    public GroupMember getGroupMemberById(Long id) {
        return groupMemberDAO.selectByPrimaryKey(id);
    }

    @Override
    public GroupMember getGroupMember(Map param) {
        return groupMemberDAO.selectOne(param);
    }

    @Override
    public List<GroupMember> getGroupMemberList(Map param) {
        return groupMemberDAO.selectParam(param);
    }

    @Override
    public PagingResult<GroupMember> getGroupMemberPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<GroupMember> list = getGroupMemberList(pageEntity.getParam());
        PageInfo<GroupMember> page = new PageInfo<>(list);
        // 如果当前查询的页数大于数据库的最大页数，返回null
        if(pageEntity.getPage() > page.getPages()){
            return null;
        }
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addGroupMember(GroupMember object) {
        try {
            return groupMemberDAO.insertSelective(object) == 1;
        } catch (DuplicateKeyException e) {
//            System.out.println("\n 重复邀请，不做处理 \n");
            logger.error("GroupMemberServiceImpl addGroupMember DuplicateKeyException error: " + e);
//            object.setCreateTime(new Date());
//            groupMemberDAO.updateGroupMember(object);
            return false;
        } catch (Exception ex) {
            logger.error("GroupMemberServiceImpl addGroupMember Exception error: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateGroupMember(GroupMember object) {
        return groupMemberDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeGroupMember(Long id) {
        return groupMemberDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean removeGroupMemberByParam(Map param) {
        return groupMemberDAO.deleteByParam(param) == 1;
    }

    @Override
    public boolean addGroupMemberBatch(List<GroupMember> list) {
        return groupMemberDAO.insertBatch(list) == list.size();
    }

    @Override
    public List<Long> getGroupMemberWithSignUserId(Map param) {
        return groupMemberDAO.selectWithSignUserId(param);
    }

    @Override
    public List<GroupMemberWithSign> getGroupMemberWithSign(Map param) {
        return groupMemberDAO.selectWithSign(param);
    }

    @Override
    public boolean ifInOtherGroup(Long userId) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS,
                GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        params.put("isRunningGroup", true);
        List<GroupMember> groupMemberList = this.getGroupMemberList(params);
        if(groupMemberList == null || groupMemberList.size() <= 0){
            return false;
        }
        return true;
    }
}
