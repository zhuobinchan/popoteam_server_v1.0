package com.geetion.puputuan.engine;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Interest;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.RecommendSuccessService;
import com.geetion.puputuan.service.UserRecommendService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.geetion.puputuan.utils.RedisUtil;
import com.geetion.puputuan.utils.RunMatchUtil;
import com.geetion.puputuan.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * Created by guodikai on 2017/1/9.
 */
public class MatchByRedisProducer implements MatchProducer {
    private final Logger logger = Logger.getLogger(MatchByRedisProducer.class);


    private RedisUtil redisUtil;
    private RunMatchUtil runMatchUtil;
    private EngineRuleStrategy engineRuleStrategy;

    private GroupService groupService = SpringLoader.getInstance().getBean(GroupService.class);

    public MatchByRedisProducer(EngineRuleStrategy engineRuleStrategy) {
        this.engineRuleStrategy = engineRuleStrategy;
    }

    {
        redisUtil = SpringLoader.getInstance().getBean(RedisUtil.class);
        runMatchUtil = SpringLoader.getInstance().getBean(RunMatchUtil.class);
    }

    @Override
    @Transactional(timeout = 10000)
    public void match(Long groupId, Long userId, Date time) {
        logger.info("================MatchByRedisProducer.match begin, groupId: " + groupId + "================================");

        Group mainGroup = groupService.getGroupById(groupId);

        try{
            String s = redisUtil.get(RedisKeyAndLock.GROUP_WAITING_MODIFY + groupId);

            // 判断队伍信息是否有发生过修改
            if(!String.valueOf(time.getTime()).equals(s)){
                logger.info("================MatchByRedisProducer.match begin, " + groupId + " is not the newest group ==========================");
                runMatchUtil.cleanRedisBean(groupId + "." + time.getTime());
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            // 当出现读取redis超时时，由于队列中的数据已经推出，所以需要将相应的bean值去除，保证数据一致性
            runMatchUtil.cleanRedisBean(groupId + "." + time.getTime());
        }


        //　队伍已经不存在，不进行匹配，直接返回
        if(mainGroup.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
            logger.info("================MatchByRedisProducer.match begin, " + groupId + " has dissolved ==========================");
            runMatchUtil.cleanRedis(groupId);
            return ;
        }

        if(this.engineRuleStrategy.matchGroup(groupId, userId, time)){
            logger.info("================MatchByRedisProducer.match begin, " + groupId + " match the new group ==========================");
            return;
        }else{

            logger.info("================MatchByRedisProducer.match begin, " + groupId + " don't match the new group ==========================");

            //如果匹配失败，将当前队伍重新放入队列中，等待再次执行
            Group group = groupService.getGroupById(groupId);
            if(group.getStatus().equals(GroupTypeAndStatus.GROUP_DISSOLUTION)){
                runMatchUtil.cleanRedis(groupId);
                return;
            }

            RedisTemplate<Serializable, Serializable> redisTemplate = redisUtil.getRedisTemplate();
            redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                @Transactional
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    String groupWaitingListId = group.getId() + "."  + time.getTime();
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

                    byte[] listKey = serializer.serialize(RedisKeyAndLock.GROUP_WAITING_LIST);
                    byte[] beanKey = serializer.serialize(RedisKeyAndLock.getBeanKey(groupId, time.getTime()));
                    byte[] value = serializer.serialize(groupWaitingListId);
                    byte[] lastTime = serializer.serialize(String.valueOf(new Date().getTime()));

                    connection.rPush(listKey, value);
                    // 更新队伍的执行时间
                    connection.hSet(beanKey, serializer.serialize("lastTime"), lastTime);

                    return true;
                }});

        }
    }

    @Override
    @Transactional(timeout = 10000)
    public synchronized void reMatch(Long groupId, Long userId, Date time) {
        logger.info("================MatchByRedisProducer.reMatch begin, groupId: " + groupId + " userId: " + userId + " time: " + time.getTime());

        String s = runMatchUtil.getWithLock(RedisKeyAndLock.GROUP_WAITING_MODIFY + groupId, RedisKeyAndLock.LOCK_MODIFY_KEY + groupId);
        // 判断队伍信息是否有发生过修改
        if(!String.valueOf(time.getTime()).equals(s)){
            logger.info("================MatchByRedisProducer.reMatch begin, current is not the newest group ==========================");
            logger.info("================cleanRedisBean key ; "+groupId + "." + time.getTime() + "=============================");
            runMatchUtil.cleanRedisBean(groupId + "." + time.getTime());
            return;
        }
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
