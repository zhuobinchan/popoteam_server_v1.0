package com.geetion.puputuan.engine;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.thread.MatchThread;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.RecommendSuccessService;
import com.geetion.puputuan.service.UserRecommendService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guodikai on 2016/9/9.
 */
public class MatchGroupProducer {
    private EngineRuleStrategy engineRuleStrategy;

    public MatchGroupProducer(EngineRuleStrategy engineRuleStrategy) {
        this.engineRuleStrategy = engineRuleStrategy;
    }
    private final Logger logger = Logger.getLogger(MatchGroupProducer.class);
    private GroupService groupService = SpringLoader.getInstance().getBean(GroupService.class);
    /**
     * 匹配队伍
     * @param groupId
     */
    public void match(Long groupId, Long userId){

        logger.debug("================MatchGroupProducer.match begin, groupId: " + groupId);
        // 如果匹配不到队伍，这定时执行匹配算法
        while(true){
            Group mainGroup = groupService.getGroupById(groupId);
            if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
                //　队伍已经不存在，不进行匹配，直接返回
                MatchThread.matchThreadMap.remove(groupId);
                return ;
            }

            if(this.engineRuleStrategy.matchGroup(groupId, userId, null)){
                logger.info("================MatchGroupProducer.match return true");
                logger.info("================MatchGroupProducer.match matchThreadMap :" + MatchThread.matchThreadMap.toString());
                MatchThread.matchThreadMap.remove(groupId);
                logger.info("================MatchGroupProducer.match matchThreadMap :" + MatchThread.matchThreadMap.toString());
                return;
            }

            try {
                logger.debug("================MatchGroupProducer.match sleep");
                // 当前线程待1分钟
                Thread.sleep(60 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重新匹配
     * @param groupId
     */
    public void reMatch(Long groupId, Long userId){

        RecommendService recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
        RecommendSuccessService recommendSuccessService =  SpringLoader.getInstance().getBean(RecommendSuccessService.class);
        UserRecommendService userRecommendService =  SpringLoader.getInstance().getBean(UserRecommendService.class);

        Map<String, Object> recommonParams = new HashMap<>();
        recommonParams.put("mainGroupId", groupId);
        List<Recommend> recommendList = recommendService.getRecommendList(recommonParams);

        // 删除user_recommend
        if(null != recommendList){
            userRecommendService.removeUserRecommendBatch(recommendList);
        }

        // 删除recommend
        recommendService.removeMainIdRecommend(groupId);
        // 删除pu_recommend_success
        recommendSuccessService.removeRecommendSuccessByMainGroup(groupId);

        this.engineRuleStrategy.matchGroup(groupId, userId, null);
    }

}
