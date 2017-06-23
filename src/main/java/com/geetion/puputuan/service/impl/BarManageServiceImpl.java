package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.ListSpiltUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/4/12.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class BarManageServiceImpl implements BarManageService {

    @Resource
    BarService barService;

    @Resource
    GroupService groupService;

    @Resource
    GroupManageService groupManageService;

    @Resource
    ActivityService activityService;

    @Resource
    ActivityManageService activityManageService;



    @Override
    public boolean updateBarStatus(Long barId, Integer status) {
        Bar bar = barService.getBarById(barId);
        bar.setStatus(status);
        return barService.updateBar(bar);
    }

    @Override
    public boolean updateGroupBarId(Long barId, Long otherBarId) {
        List<Long> groupIds = getGroupIdsByBarId(barId);
        if (groupIds!=null && groupIds.size()>0) {
            List<List<Long>> groupIdLists = ListSpiltUtil.listSpilt(groupIds);
            for (List<Long> groupIdList : groupIdLists) {
                groupService.updateGroupBarId(groupIdList, otherBarId);
            }
        }
        return true;
    }

    @Override
    public void chgActivityIsExpire(Long barId, Integer isExpire) {
        List<Long> groupIds = getGroupIdsByBarId(barId);
        if (groupIds!=null && groupIds.size()>0){
            List<List<Long>> groupIdLists =  ListSpiltUtil.listSpilt(groupIds);
            for (List<Long> groupIdList:groupIdLists) {
                activityService.updateActivityIsExpireByGroupIds(groupIdList,isExpire);
            }
        }
    }

    @Override
    public void romoveActivity(Long barId) {
        List<Long> groupIds = getGroupIdsByBarId(barId);
        if (groupIds!=null && groupIds.size()>0) {
            List<Activity> activityList = new ArrayList<>();
            for (Long groupId : groupIds) {
                Map param = new HashMap();
                param.put("selectBoth", true);
                param.put("groupAId", groupId);
                param.put("groupBId", groupId);
                List<Activity> activities = activityService.getActivityList(param);
                activityList.addAll(activities);
            }
            activityManageService.cleanActivityList(activityList);
        }
    }

    @Override
    public void dissolutionGroupByBarId(Long barId){
        List<Long> groupIds = getGroupIdsByBarId(barId);
        for (Long groupId:groupIds) {
            try {
                groupManageService.dissolutionGroup(groupId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Long> getGroupIdsByBarId(Long barId){
        Map<String,Object> param = new HashMap();
        param.put("barId",barId);
        param.put("status", GroupTypeAndStatus.GROUP_MATCHING);
        return groupService.getGroupIdByParam(param);
    }

}
