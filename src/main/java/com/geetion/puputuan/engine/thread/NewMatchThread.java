package com.geetion.puputuan.engine.thread;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.common.constant.RecommendType;
import com.geetion.puputuan.engine.MatchGroupProducer;
import com.geetion.puputuan.engine.NewMatchGroupProducer;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.rule.impl.AdressAndTypeStrategyEngine;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.model.UserRecommend;
import com.geetion.puputuan.service.UserRecommendService;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guodikai on 2016/9/12.
 */
public class NewMatchThread extends Thread {

    private Long groupId;
    private Long userId;
    private Long isRe;
    private Date time;
    private final Logger logger = Logger.getLogger(NewMatchThread.class);
    /**
     * 匹配规则引擎
     * @param groupId
     * @param isRe 标识是否重跑
     */
    public NewMatchThread(Long groupId, Long userId, Long isRe, Date time){
        this.groupId = groupId;
        this.userId = userId;
        this.isRe = isRe;
        this.time = time;
    }

    public void run(){
        EngineRuleStrategy engineRuleStrategy = new AdressAndTypeStrategyEngine();
        NewMatchGroupProducer matchGroupProducer = new NewMatchGroupProducer(engineRuleStrategy);
        if(isRe == 1){
            logger.info("==============MatchThread group " + groupId + "begin run! isRe = "  + isRe);
            matchGroupProducer.reMatch(groupId, userId, time);
        }else {
            logger.info("==============MatchThread group " + groupId + "begin run!isRe = "  + isRe);
            matchGroupProducer.match(groupId, userId, time);
        }
    }

}
