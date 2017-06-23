package com.geetion.puputuan.engine.thread;

import com.easemob.server.example.constant.HuanXinConstant;
//import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.common.constant.RecommendType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.engine.MatchGroupProducer;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.rule.impl.AdressAndTypeStrategyEngine;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.model.UserRecommend;
import com.geetion.puputuan.service.UserRecommendService;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guodikai on 2016/9/12.
 */
public class MatchThread extends Thread {

    private Long groupId;
    private Long userId;
    private boolean isRe;

    // 记录当前在进入匹配的队伍
    public static ConcurrentHashMap matchThreadMap = new ConcurrentHashMap();

    private final Logger logger = Logger.getLogger(MatchThread.class);
    /**
     * 匹配规则引擎
     * @param groupId
     * @param isRe 标识是否重跑
     */
    public MatchThread(Long groupId, Long userId, boolean isRe){
        this.groupId = groupId;
        this.userId = userId;
        this.isRe = isRe;
    }

    public void run(){
        logger.info("MatchThread group " + groupId + "begin run!");
        EngineRuleStrategy engineRuleStrategy = new AdressAndTypeStrategyEngine();
        MatchGroupProducer matchGroupProducer = new MatchGroupProducer(engineRuleStrategy);
        if(!MatchThread.matchThreadMap.containsKey(groupId)){
            logger.info("MatchThread matchThreadMap containsKey " + groupId);
            MatchThread.matchThreadMap.put(groupId,Thread.currentThread().getId());
            runMatch(matchGroupProducer);
        }else{
            while (true){
                synchronized(this){
                    // 判断队伍的线程是否已经结束
                    if(!MatchThread.matchThreadMap.containsKey(groupId)){
                        logger.info("MatchThread matchThreadMap not containsKey " + groupId);
                        UserRecommendService userRecommendService = SpringLoader.getInstance().getBean(UserRecommendService.class);
                        UserService userService = SpringLoader.getInstance().getBean(UserService.class);
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", userId);
                        params.put("status", RecommendType.NOT_SEND);

                        //取得未推送的匹配队伍
                        List<UserRecommend> userRecommendList = userRecommendService.getUserRecommendByParams(params);
                        if(userRecommendList != null && userRecommendList.size() > 0){
                            logger.info("the group "+ groupId + " 's userRecommendList not null" );
                            User user = userService.getByUserId(userId);
                            try {
                                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                                        "恭喜，匹配到新的队伍", HuanXinSendMessageType.HAVE_MATCHING_RESULT, null,
                                        user.getUserBase().getAccount());
                            } catch (HuanXinMessageException e) {
                                e.printStackTrace();
                            }
                        }else{
                            logger.info("Thread " + Thread.currentThread().getName() +"is waiting");
                            runMatch(matchGroupProducer);
                        }
                        logger.info("MatchThread run end! groupId = " + groupId);
                        return;
                    }
                }

                // 等待
                try {
                    logger.info("Thread " + Thread.currentThread().getName() +"is waiting");
                    logger.info("GroupId " + groupId +"is waiting");
                    logger.info("MatchThread matchThreadMap" + MatchThread.matchThreadMap.toString());
                    Thread.sleep(5 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void runMatch(MatchGroupProducer matchGroupProducer){
        if(isRe){
            matchGroupProducer.reMatch(groupId, userId);
        }else {
            matchGroupProducer.match(groupId, userId);
        }
    }

}
