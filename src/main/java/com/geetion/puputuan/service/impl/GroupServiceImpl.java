package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.constant.GroupMemberTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.GroupDAO;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.GroupDetailWithCount;
import com.geetion.puputuan.pojo.GroupStatisData;
import com.geetion.puputuan.pojo.GroupWithNumberList;
import com.geetion.puputuan.service.GroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupDAO groupDAO;

    @Override
    public Group getGroupById(Long id) {
        return groupDAO.selectByPrimaryKey(id);
    }

    @Override
    public Group getGroup(Map param) {
        return groupDAO.selectOne(param);
    }

    @Override
    public List<Group> getGroupList(Map param) {
        return groupDAO.selectParam(param);
    }

    @Override
    public PagingResult<Group> getGroupPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Group> list = getGroupList(pageEntity.getParam());
        PageInfo<Group> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addGroup(Group object) {
        return groupDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateGroup(Group object) {
        return groupDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeGroup(Long id) {
        return groupDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public List<User> getUserByStatusList(Map param) {
        return groupDAO.selectUserByStatus(param);
    }

    @Override
    public List<Group> getGroupByStatusList(Map param) {
        return groupDAO.selectGroupByStatus(param);
    }

    @Override
    public PagingResult<Group> getGroupByStatusPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Group> list = getGroupByStatusList(pageEntity.getParam());
        PageInfo<Group> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<GroupWithNumberList> getGroupWithNumberList(Map param) {
        return groupDAO.selectWithNumberList(param);
    }

    @Override
    public PagingResult<GroupWithNumberList> getGroupWithNumberPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<GroupWithNumberList> list = getGroupWithNumberList(pageEntity.getParam());
        PageInfo<GroupWithNumberList> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public GroupWithNumberList getGroupWithSignNumber(Map param) {
        return groupDAO.selectWithSignNumber(param);
    }

    @Override
    public List<Group> selectVoteGroup(Map param) {
        return groupDAO.selectVoteGroup(param);
    }

    @Override
    public List<Group> selectMatchGroupByParam(Map param) {
        return groupDAO.selectMatchGroupByParam(param);
    }

    @Override
    public Group getRunningGroup(Long userId) {
        List<Integer> inStatus = new ArrayList<>();
        //查询在创建中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_CREATE);
        //查询在匹配中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
        //查询在组队中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_TEAM);

        List<Integer> inMemberStatus = new ArrayList<>();
        //查询加入状态的用户
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("inStatus", inStatus);
        param.put("inMemberStatus", inMemberStatus);

        //如果用户已经在正在进行的队中，则list不为空
        List<Group> list = this.getGroupByStatusList(param);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<GroupDetailWithCount> getGroupDetailCount(Map param) {
        return groupDAO.selectGroupDetailCount(param);
//        return null;
    }

    @Override
    public PagingResult<GroupDetailWithCount> getGroupDetailCountPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        if(null != pageEntity.getOrderColumn() && null != pageEntity.getOrderTurn()){
            String orderColumn = pageEntity.getOrderColumn();
            if("bar.name".equals(orderColumn)){
                orderColumn = "pu_bar_name";
            }
            if("createTime".equals(orderColumn)){
                orderColumn = "create_time";
            }
            if("modifyTime".equals(orderColumn)){
                orderColumn = "modify_time";
            }
            if("recommendCount".equals(orderColumn)){
                orderColumn = "recommend_count";
            }
            if("activityCount".equals(orderColumn)){
                orderColumn = "activity_count";
            }
            if("pu_bar_name".equals(orderColumn)){
                PageHelper.orderBy(orderColumn + " " + pageEntity.getOrderTurn());
            }else {
                PageHelper.orderBy("pu_group_" + orderColumn + " " + pageEntity.getOrderTurn());
            }
        }

        List<GroupDetailWithCount> list = getGroupDetailCount(pageEntity.getParam());
        PageInfo<GroupDetailWithCount> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<GroupStatisData> getGroupStatisData(Map param) {
        return groupDAO.selectGroupStatisData(param);
    }

    @Override
    public PagingResult<GroupStatisData> getGroupStatisDataPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<GroupStatisData> list = getGroupStatisData(pageEntity.getParam());
        PageInfo<GroupStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<GroupStatisData> getGroupBarStatisData(Map param) {
        return groupDAO.selectGroupBarStatisData(param);
    }

    @Override
    public PagingResult<GroupStatisData> getGroupBarStatisDataPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<GroupStatisData> list = getGroupBarStatisData(pageEntity.getParam());
        PageInfo<GroupStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<GroupStatisData> getGroupRegionStatisData(Map param) {
        return groupDAO.selectGroupRegionStatisData(param);
    }

    @Override
    public GroupStatisData getGroupSumStatisData() {
        return groupDAO.selectGroupSumStatisData();
    }

    @Override
    public GroupStatisData getGroupSumForCharts(Map param) {

        return groupDAO.selectGroupSumForCharts(param);
    }

    @Override
    public List<GroupStatisData> getGroupSumForBar(Map param) {
        return groupDAO.selectGroupSumForBar(param);
    }

    @Override
    public void updateGroupDailyLiving(Long groupId) {
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        groupDAO.callGroupDailyLiving(param);
    }

    @Override
    public List<GroupDetailWithCount> getGroupDetailCountByIds(Map param) {
        return groupDAO.selectGroupDetailCountByIds(param);
    }

    @Override
    public List<Long> getGroupIdByParam(Map param) {
        return groupDAO.selectGroupIdByParam(param);
    }

    @Override
    public boolean updateGroupBarId(List<Long> groupIds, Long otherBarId) {
        Map<String, Object> param = new HashMap<>();
        param.put("groupIds", groupIds);
        param.put("otherBarId", otherBarId);
        return groupDAO.updateGroupBarId(param) > 0;
    }
}
