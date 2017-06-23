package com.geetion.puputuan.engine.handler.impl;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.engine.handler.Matcher;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.model.UserRecommend;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 同市不同类型
 */
public class SameCityDifTypeMatcher extends Matcher {
    private final Logger logger = Logger.getLogger(SameCitySameTypeMatcher.class);
    @Override
    public synchronized boolean matchGroup(Long groupId, Long userId, Date time) {
        logger.info("==========SameCityDifTypeMatcher,Thread name: " + Thread.currentThread().getName() +", groupId: " + groupId);
        Group mainGroup = groupService.getGroupById(groupId);
        // 当队伍已经解散直接返回false
        if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("cityId", mainGroup.getCityId() == null ? " " : mainGroup.getCityId());
        params.put("difBarId", mainGroup.getBarId());
        params.put("type", mainGroup.getType());
        params.put("status", GroupTypeAndStatus.GROUP_MATCHING);
        params.put("groupId", groupId);


        List<Group> matchGroup = groupService.selectMatchGroupByParam(params);
        // 匹配不到队伍，执行下一个匹配算法
        if(matchGroup.size() == 0 ){
            return this.nextMatchGroup(groupId, userId,time);
        }

        List<Group> voteGroup = this.selectVoteGroup(groupId);
        List<Group> mergeGroup = this.mergeGroup(matchGroup, voteGroup);
        this.excludeSuperLikeGroup(mainGroup, mergeGroup);
        this.excludeDislikeGroup(userId, mainGroup, mergeGroup);
        this.pollLikeGroupToFirst(userId, mainGroup, mergeGroup);
        List<Recommend> recommends = new ArrayList<>();
        List<UserRecommend> userRecommends = new ArrayList<>();

        if (this.ifNewestGroup(mainGroup, time)) {
            this.addRecommend(mainGroup, mergeGroup, recommends);
            // 如果过滤之后，没有生成新的推荐信息，执行下一个算法
            if(recommends.size() == 0 ){
                return this.nextMatchGroup(groupId, userId, time);
            }
            this.addUserRecommend(mainGroup, recommends, userRecommends);
            if(ShareCacheVar.ifUseNew){
                this.sendRecommendMsgHuanxinByType(mainGroup, userId, false);
            }else{
                this.sendRecommendMsgHuanxin(mainGroup, userId);
            }
            return true;
        }

        return false;
    }
}
