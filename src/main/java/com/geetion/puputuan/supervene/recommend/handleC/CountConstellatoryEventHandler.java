package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.common.constant.SexType;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.geetion.puputuan.supervene.recommend.service.AnalyseMatch;
import com.lmax.disruptor.EventHandler;

/**
 * Created by mac on 16/3/31.
 */
public class CountConstellatoryEventHandler implements EventHandler<Recommend> {

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算星座得分
        int totalScore = 0;
        for (UserRecommend mainUser : recommend.getMainUser()) {
            String mainConstellatory = mainUser.getConstellation();
            for (UserRecommend matchUser : recommend.getMatchUser()) {
                String matchConstellatory = matchUser.getConstellation();
                if (matchUser.getSex().equals(SexType.MALE)) {
                    if (AnalyseMatch.getConstellatoryMatch(mainConstellatory, matchConstellatory)) {
                        totalScore = totalScore + 9;
                    }
                } else {
                    if (AnalyseMatch.getConstellatoryMatch(matchConstellatory, mainConstellatory)) {
                        totalScore = totalScore + 9;
                    }
                }
            }
        }
        recommend.totalD = totalScore;
//        System.out.println("计算星座得分:" + totalScore);
    }
}
