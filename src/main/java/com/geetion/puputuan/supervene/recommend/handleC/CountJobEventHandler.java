package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.lmax.disruptor.EventHandler;

/**
 * Created by mac on 16/3/31.
 */
public class CountJobEventHandler implements EventHandler<Recommend> {
    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算JOB得分
        int totalScore = 0;
        for (Long mainJobId : recommend.getMainJob()) {
            for (Long matchJobId : recommend.getMatchJob()) {
                if (mainJobId.longValue() == matchJobId.longValue()) {
                    totalScore += 4.5;
                }
            }
        }
        recommend.totalF2 = totalScore;
//        System.out.println("计算JOB得分:" + totalScore);
    }
}
