package com.geetion.puputuan.engine.rule.impl;

import com.geetion.puputuan.engine.handlernew.impl.*;
import com.geetion.puputuan.engine.handlernew.RedisMatcher;
import com.geetion.puputuan.engine.rule.EngineRuleStrategy;

import java.util.Date;


/**
 * 根据地区、类型进行匹配队伍
 */
public class AdressAndTypeStrategyEngine implements EngineRuleStrategy {
    @Override
    public boolean matchGroup(Long groupId, Long userId, Date time) {
        return match(groupId, userId, time);
    }

    @Override
    public boolean rematchGroup(Long groupId, Long userId, Date time) {

        return matchGroup(groupId, userId, time);
    }

    private boolean match(Long groupId, Long userId, Date time){
        //匹配算法
        // 同地区同类型 > 同地区不同类型 > 同市同类型 > 同市不同类型
//        RedisMatcher matcherS = new SameTypeMatcher();
        RedisMatcher matcherA = new SameAreaSameTypeMatcher();
        RedisMatcher matcherB = new SameAreaDifTypeMatcher();
        RedisMatcher matcherC = new SameCitySameTypeMatcher();
        RedisMatcher matcherD = new SameCityDifTypeMatcher();
        RedisMatcher matcherE = new SameProvinceDifTypeMatcher();
        RedisMatcher matcherF = new NationWideMatcher();
        RedisMatcher matcherG = new ResetUserRecommendMatcher();

//        matcherS.setMatcher(matcherA);
        matcherA.setMatcher(matcherB);
        matcherB.setMatcher(matcherC);
        matcherC.setMatcher(matcherD);
        matcherD.setMatcher(matcherE);
        matcherE.setMatcher(matcherF);
        matcherF.setMatcher(matcherG);

        //执行匹配算法
//        return matcherS.matchGroup(groupId, userId, time);
        return matcherA.matchGroup(groupId, userId, time);
    }
}
