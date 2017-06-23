package com.task;

import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.service.ActivityHistoryService;
import com.geetion.puputuan.service.ActivityMemberHisService;
import com.geetion.puputuan.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guodikai on 2016/10/21.
 */
public class CleanActivityJob {

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityHistoryService activityHistoryService;

    @Resource
    private ActivityMemberHisService activityMemberHisService;

    private int perCount = 20;

    private final  static Logger logger = Logger.getLogger(CleanActivityJob.class);

    public void excuteCleanJob(){
        logger.info("CleanActivityJob.excuteClaenJob begin");
        int[] types = new int[]{ActivityTypeAndStatus.SUCCESS, ActivityTypeAndStatus.BEGIN};
        int hours = 24;

        Map<String, Object> params = new HashMap<>();
        params.put("types", types);
        params.put("hours", hours);
        params.put("isExpire", "0");

        List<Activity> activityList = activityService.getActivityList(params);
        List<Activity> waitToUpdateList = new ArrayList<>();
        logger.info("CleanActivityJob.excuteClaenJob activityList size" + activityList.size());
        for (Activity activity : activityList){

            activity.setType(ActivityTypeAndStatus.DISSOLUTION);
            activity.setExpireType(0);


            waitToUpdateList.add(activity);
            // 分批更新
            if(perCount == waitToUpdateList.size()){
//                activityHistoryService.insertActivityHistory(waitToUpdateList);
//                deleteChatGroup(waitToUpdateList);
                cleanActivity(waitToUpdateList);
                waitToUpdateList.clear();
            }

        }

        if(0 < waitToUpdateList.size()){
//            activityHistoryService.insertActivityHistory(waitToUpdateList);
//            deleteChatGroup(waitToUpdateList);
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
