package com.geetion.puputuan.engine.handler.impl;

import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.RecommendType;
import com.geetion.puputuan.engine.handler.Matcher;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.RecommendSuccess;
import com.geetion.puputuan.model.UserRecommend;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guodikai on 2016/9/12.
 */
public class ResetUserRecommendMatcher extends Matcher {
    private final Logger logger = Logger.getLogger(SameCitySameTypeMatcher.class);
    @Override
    public synchronized boolean matchGroup(Long groupId, Long userId, Date time) {

        logger.info("==========ResetUserRecommendMatcher,Thread name: " + Thread.currentThread().getName() +", groupId: " + groupId);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserRecommend> userRecommendList = userRecommendService.getUserRecommendByParams(params);
        int notSendRecommend = 0 ;
        //清空pu_user_like_group
        userLikeGroupService.setUserLikeGroupToInvalid(groupId);

        Group mainGroup = groupService.getGroupById(groupId);

        params.clear();
        if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
            params.put("femaleGroupId", mainGroup.getId());
        }else {
            params.put("maleGroupId", mainGroup.getId());
        }
        params.put("type", ActivityTypeAndStatus.BEGIN);
        params.put("superLike", 1);

        List<Activity> activityList = activityService.getActivityList(params);

//        Map<Long, Object> superLikeGroupMap = new HashMap<>();
//        for (Activity a : activityList){
////            if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
////                superLikeGroupMap.put(a.getMaleGroupId(), true);
////            }else {
////                superLikeGroupMap.put(a.getFemaleGroupId(), true);
////            }
//        }
        if(!ifNewestGroup(mainGroup,time)){
            return false;
        }
        for(UserRecommend ur : userRecommendList){
            // 确保取的是当前队伍的推荐信息
            if(ur.getRecommend().getMainGroupId().equals(groupId)){
                params.clear();
                params.put("mainGroupId", ur.getRecommend().getMainGroupId());
                params.put("matchGroupId", ur.getRecommend().getMatchGroupId());
                Group matchGroup = groupService.getGroupById(ur.getRecommend().getMatchGroupId());
                RecommendSuccess recommendSuccess = recommendSuccessService.getRecommendSuccess(params);
                // 只有当推荐给用户的队伍，未与用户所有队伍组成约会，以前匹配队伍未解散，才将推荐队伍重新推荐给用户
                if(matchGroup.getStatus().equals(GroupTypeAndStatus.GROUP_MATCHING)
                        && null == recommendSuccess){
                    notSendRecommend ++;
                    ur.setStatus(RecommendType.NOT_SEND);
                    userRecommendService.updateUserRecommend(ur);
                }
            }
        }

        // 当推荐信息不为空时，给队伍成员发送环信信息
        if(notSendRecommend > 0){
            if(ShareCacheVar.ifUseNew){
                this.sendRecommendMsgHuanxinByType(groupService.getGroupById(groupId), userId, true);
            }else{
                this.sendRecommendMsgHuanxin(groupService.getGroupById(groupId), userId);
            }
        }
        return notSendRecommend > 0 ;
    }

}
