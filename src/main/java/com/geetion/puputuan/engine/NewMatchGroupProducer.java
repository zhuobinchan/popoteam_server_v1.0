package com.geetion.puputuan.engine;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.thread.MatchThread;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.RecommendSuccessService;
import com.geetion.puputuan.service.UserRecommendService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by guodikai on 2016/9/9.
 */
public class NewMatchGroupProducer {
    private EngineRuleStrategy engineRuleStrategy;

    public NewMatchGroupProducer(EngineRuleStrategy engineRuleStrategy) {
        this.engineRuleStrategy = engineRuleStrategy;
    }
    private final Logger logger = Logger.getLogger(NewMatchGroupProducer.class);
    private GroupService groupService = SpringLoader.getInstance().getBean(GroupService.class);
    /**
     * 匹配队伍
     * @param groupId
     */
    @Transactional(timeout = 10000)
    public void match(Long groupId, Long userId, Date time){

        logger.debug("================MatchGroupProducer.match begin, groupId: " + groupId);

        Group mainGroup = groupService.getGroupById(groupId);

        //　队伍已经不存在，不进行匹配，直接返回
        if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
            return ;
        }

        if(this.engineRuleStrategy.matchGroup(groupId, userId, time)){
            logger.info("=============NewMatchGroupProducer groupId " + groupId + " userId " + userId + " return true");
            return;
        }else{
            //如果匹配失败，将当前队伍重新放入队列中，等待再次执行
            synchronized(ShareCacheVar.groupNoMatchWaitingQueue) {
                Iterator iterator = ShareCacheVar.groupNoMatchWaitingQueue.iterator();
                Map<String, Long> it;
                while (iterator.hasNext()){
                    it = (Map<String, Long>) iterator.next();
                    Long groupIdWaiting = it.get("groupId");
                    Long userIdWaiting = it.get("userId");
                    logger.info("==================groupNoMatchWaitingQueue groupId " + groupIdWaiting);
                    if (groupIdWaiting.equals(groupId)){
                        if(!userIdWaiting.equals(userId)){
                            logger.info("===================add group member to memberWaitingMap");
                            List<Long> memberWaiting = (List<Long>) ShareCacheVar.memberWaitingMap.get(groupId);

                            if (null == memberWaiting){
                                List<Long> memberList = new ArrayList<>();
                                memberList.add(userId);
                                ShareCacheVar.memberWaitingMap.put(groupId, memberList);
                            }else{
                                //如果当前在队列中的进程，是当前用户触发的，不进行另外的等待队列
                                if (!userIdWaiting.equals(userId)){
                                    for(Long l : memberWaiting){
                                        if(l.equals(userId)){
                                            return;
                                        }
                                    }
                                    logger.info("================memberWaitingMap add groupId ");
                                    // 等待队伍中没有当前用户，添加至等待队伍中
                                    memberWaiting.add(userId);
                                }
                            }
                        }
                        logger.info("===============groupWaitingQueue has groupId " + groupIdWaiting);
                        return;
                    }
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("groupId", groupId);
                map.put("userId", userId);
                map.put("isReMatch", 0L);
                map.put("isValid", 1L);
                map.put("time", time);
                ShareCacheVar.groupNoMatchWaitingQueue.add(map);
            }

        }

    }

    /**
     * 重新匹配
     * @param groupId
     */
    @Transactional(timeout = 10000)
    public void reMatch(Long groupId, Long userId, Date time){

        logger.debug("================MatchGroupProducer.reMatch begin, groupId: " + groupId + " userId: " + userId);
        RecommendService recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
        RecommendSuccessService recommendSuccessService =  SpringLoader.getInstance().getBean(RecommendSuccessService.class);
        UserRecommendService userRecommendService =  SpringLoader.getInstance().getBean(UserRecommendService.class);

        Map<String, Object> recommonParams = new HashMap<>();
        recommonParams.put("mainGroupId", groupId);
        List<Recommend> recommendList = recommendService.getRecommendList(recommonParams);

        recommendService.sumGroupRecommend(groupId);

        // 删除user_recommend
        if(null != recommendList && recommendList.size() > 0){
            userRecommendService.removeUserRecommendBatch(recommendList);
        }

        // 删除recommend
        recommendService.removeMainIdRecommend(groupId);
        // 删除pu_recommend_success
        recommendSuccessService.removeRecommendSuccessByMainGroup(groupId);

        this.engineRuleStrategy.matchGroup(groupId, userId, time);
    }

}
