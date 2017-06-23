package com.geetion.puputuan.engine.handlernew.impl;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.engine.handler.impl.SameCitySameTypeMatcher;
import com.geetion.puputuan.engine.handlernew.RedisMatcher;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.model.UserRecommend;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * 匹配同地区同类型
 */
public class SameAreaSameTypeMatcher extends RedisMatcher {
    private final Logger logger = Logger.getLogger(SameCitySameTypeMatcher.class);
    private final int MATCHER_RULE_INDEX = 0;
    @Override
    public boolean matchGroup(Long groupId, Long userId, Date time) {
        logger.info("==========SameAreaSameTypeMatcher,Thread name: " + Thread.currentThread().getName() +", groupId: " + groupId);
        Group mainGroup = groupService.getGroupById(groupId);
        // 当队伍已经解散直接返回false
        if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
            logger.info("==========SameAreaSameTypeMatcher, group is dissolution =================");
            // 当队伍解散时，会触发清理对应redis的缓存数据
            // 但可能存在解散之前，刚好队伍中的缓存被取出，由于bean的缓存是由id + modifyTime组成
            // 当bean有多条数据时，需要在线程执行中，将对应的bean进行删除。
            runMatchUtil.cleanRedis(groupId);
            return false;
        }
        // 判断算法开关是否有开
        if(!checkRuleIfOpen(MATCHER_RULE_INDEX)){
            logger.info("==========SameAreaSameTypeMatcher, close =================");
            return this.nextMatchGroup(groupId, userId, time);
        }

        if(mainGroup.getAreaId().equals(0)){
            // 当地区为所有时，直接执行下一个算法
            return this.nextMatchGroup(groupId, userId, time);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("areaId", mainGroup.getAreaId());
        params.put("barId", mainGroup.getBarId());
        params.put("type", this.getMatchGroupType(mainGroup));
        params.put("status", GroupTypeAndStatus.GROUP_MATCHING);
        params.put("groupId", groupId);


        List<Group> matchGroup = groupService.selectMatchGroupByParam(params);
        // 匹配不到队伍，执行下一个匹配算法
        if(matchGroup.size() == 0 ){
            return this.nextMatchGroup(groupId, userId, time);
        }

        List<Group> voteGroup = this.selectVoteGroup(groupId);
        List<Group> mergeGroup = this.mergeGroup(matchGroup, voteGroup);
        this.excludeSuperLikeGroup(mainGroup, mergeGroup);
        this.excludeDislikeGroup(userId, mainGroup, mergeGroup);
        this.pollLikeGroupToFirst(userId, mainGroup, mergeGroup);
        List<Recommend> recommends = new ArrayList<>();
        List<UserRecommend> userRecommends = new ArrayList<>();
        this.addRecommend(mainGroup, mergeGroup, recommends);

        // 如果过滤之后，没有生成新的推荐信息，执行下一个算法
        if(recommends.size() == 0 ){
            return this.nextMatchGroup(groupId, userId, time);
        }

        if (this.ifNewestGroup(mainGroup, time)) {
            this.addUserRecommend(mainGroup, recommends, userRecommends);
            this.sendRecommendMsgHuanxin(mainGroup, userId, time, false);
            return true;
        }
        return false;
    }
}

