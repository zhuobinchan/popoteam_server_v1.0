package com.geetion.puputuan.service.impl;

import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.service.ActivityHistoryService;
import com.geetion.puputuan.service.ActivityManageService;
import com.geetion.puputuan.service.ActivityMemberHisService;
import com.geetion.puputuan.service.ActivityService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhuobin on 2017/4/14.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ActivityManageServiceImpl implements ActivityManageService {

    @Resource
    private ActivityHistoryService activityHistoryService;

    @Resource
    private ActivityMemberHisService activityMemberHisService;

    @Resource
    private ActivityService activityService;

    private int perCount = 20;

    @Override
    public void cleanActivityList(List<Activity> activityList) {
        List<Activity> waitToUpdateList = new ArrayList<>();
        for (Activity activity : activityList){
            activity.setType(ActivityTypeAndStatus.DISSOLUTION);
            activity.setExpireType(0);

            waitToUpdateList.add(activity);
            // 分批更新
            if(perCount == waitToUpdateList.size()){
                cleanActivity(waitToUpdateList);
                waitToUpdateList.clear();
            }
        }
        if(0 < waitToUpdateList.size()){
            cleanActivity(waitToUpdateList);
        }
    }

    private void cleanActivity(List<Activity> list){
        activityHistoryService.insertActivityHistory(list);
        activityMemberHisService.insertActivityMemberHistory(list);
        activityService.deleteActivityAndMember(list);
        deleteChatGroup(list);
    }

    /**
     * //解散约会
     * @param activityList
     */
    private void deleteChatGroup(List<Activity> activityList){
        for (Activity activity : activityList){
            try {

                HuanXinChatGroupService.deleteChatGroup(activity.getRoomId());
            } catch (HuanXinChatGroupException e) {
                e.printStackTrace();
            }
        }
    }
}
