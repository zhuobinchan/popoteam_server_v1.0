package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.lmax.disruptor.EventHandler;

/**
 * Created by mac on 16/3/31.
 */
public class CountInterestEventHandler implements EventHandler<Recommend> {
    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算兴趣得分
        int totalScore = 0;
        for (Long mainInsterestId : recommend.getMainInterest()) {
            for (Long matchInsterestId : recommend.getMatchInterest()) {
                if (mainInsterestId.longValue() == matchInsterestId.longValue()) {
                    totalScore += 4.5;
                }
            }
        }
        recommend.totalF = totalScore;
//        System.out.println("计算兴趣得分:" + totalScore);
    }
}
