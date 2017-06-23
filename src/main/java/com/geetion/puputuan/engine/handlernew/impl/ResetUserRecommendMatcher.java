package com.geetion.puputuan.engine.handlernew.impl;

import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.RecommendType;
import com.geetion.puputuan.engine.handler.impl.SameCitySameTypeMatcher;
import com.geetion.puputuan.engine.handlernew.RedisMatcher;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.RecommendSuccess;
import com.geetion.puputuan.model.UserRecommend;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guodikai on 2016/9/12.
 */
public class ResetUserRecommendMatcher extends RedisMatcher {
    private final Logger logger = Logger.getLogger(SameCitySameTypeMatcher.class);
    private final int MATCHER_RULE_INDEX = 6;
    @Override
    public boolean matchGroup(Long groupId, Long userId, Date time) {

        logger.info("==========ResetUserRecommendMatcher,Thread name: " + Thread.currentThread().getName() +", groupId: " + groupId);
        Group mainGroup = groupService.getGroupById(groupId);

        if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
            logger.info("==========ResetUserRecommendMatcher, " + groupId + " is dissolution =================");
            // 当队伍解散时，会触发清理对应redis的缓存数据
            // 但可能存在解散之前，刚好队伍中的缓存被取出，由于bean的缓存是由id + modifyTime组成
            // 当bean有多条数据时，需要在线程执行中，将对应的bean进行删除。
            runMatchUtil.cleanRedis(groupId);
            return false;
        }
        // 判断算法开关是否有开
        if(!checkRuleIfOpen(MATCHER_RULE_INDEX)){
            logger.info("==========ResetUserRecommendMatcher, close =================");
            return false;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("currentGroupId", groupId);
        List<UserRecommend> userRecommendList = userRecommendService.getUserRecommendByParams(params);
        int notSendRecommend = 0 ;
        //清空pu_user_like_group
        userLikeGroupService.setUserLikeGroupToInvalid(groupId);

        params.clear();
//        if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
//            params.put("femaleGroupId", mainGroup.getId());
//        }else {
//            params.put("maleGroupId", mainGroup.getId());
//        }
        params.put("groupAId", mainGroup.getId());
        params.put("type", ActivityTypeAndStatus.BEGIN);
        // 这里不再根据superlike与否来查询.对于正常匹配成功的队伍也应该排除.
        // 避免当前队伍修改配置后vote success表被清掉,导致已经匹配成功的约会队伍又重新推送给当前队伍.
//        params.put("superLike", 1);

        List<Activity> activityList = activityService.getActivityList(params);

        params.clear();
        params.put("groupBId", mainGroup.getId());
        params.put("type", ActivityTypeAndStatus.BEGIN);
        activityList.addAll(activityService.getActivityList(params));

        Map<Long, Object> superLikeGroupMap = new HashMap<>();
        for (Activity a : activityList){
            superLikeGroupMap.put(a.getGroupBId(), true);
//            if (mainGroup.getType().equals(GroupTypeAndStatus.GROUP_FEMALE)){
//                superLikeGroupMap.put(a.getMaleGroupId(), true);
//            }else {
//                superLikeGroupMap.put(a.getFemaleGroupId(), true);
//            }
        }
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
                        && null == recommendSuccess && !superLikeGroupMap.containsKey(ur.getRecommend().getMatchGroupId())){

                    if(null == mainGroup.getRecommendSex()){
                        if(GroupTypeAndStatus.GROUP_MIX == matchGroup.getType()
                                || mainGroup.getType().intValue() == matchGroup.getType().intValue()){
                            continue;
                        }
                    }else {
                        if(mainGroup.getRecommendSex() != GroupTypeAndStatus.GROUP_ALL &&
                                mainGroup.getRecommendSex().intValue() != matchGroup.getType().intValue()){
                            continue;
                        }
                    }

                    notSendRecommend ++;
                    ur.setStatus(RecommendType.NOT_SEND);
                    userRecommendService.updateUserRecommend(ur);
                }
            }
        }

        // 当推荐信息不为空时，给队伍成员发送环信信息
        if(notSendRecommend > 0){
            this.sendRecommendMsgHuanxin(groupService.getGroupById(groupId), userId, time, true);
        }
        return notSendRecommend > 0;
    }

}
