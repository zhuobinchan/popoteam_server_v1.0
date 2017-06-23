package com.geetion.puputuan.engine.thread;

import com.geetion.puputuan.engine.MatchByRedisProducer;
import com.geetion.puputuan.engine.MatchProducer;
import com.geetion.puputuan.engine.NewMatchGroupProducer;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;
import com.geetion.puputuan.engine.rule.impl.AdressAndTypeStrategyEngine;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by guodikai on 2016/9/12.
 */
public class RedisMatchThread extends Thread {

    private Long groupId;
    private Long userId;
    private Long isRe;
    private Date time;
    private final Logger logger = Logger.getLogger(RedisMatchThread.class);
    /**
     * 匹配规则引擎
     * @param groupId
     * @param isRe 标识是否重跑
     */
    public RedisMatchThread(Long groupId, Long userId, Long isRe, Date time){
        this.groupId = groupId;
        this.userId = userId;
        this.isRe = isRe;
        this.time = time;
    }

    public void run(){
        EngineRuleStrategy engineRuleStrategy = new AdressAndTypeStrategyEngine();
        MatchProducer matchProducer = new MatchByRedisProducer(engineRuleStrategy);
        if(isRe == 1){
            logger.info("==============MatchThread group " + groupId + "begin run! isRe = "  + isRe + "time = " + time.getTime());
            matchProducer.reMatch(groupId, userId, time);
        }else {
            logger.info("==============MatchThread group " + groupId + "begin run!isRe = "  + isRe + "time = " + time.getTime());
            matchProducer.match(groupId, userId, time);
        }
    }

}
