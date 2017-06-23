package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.lmax.disruptor.EventHandler;

/**
 * Created by mac on 16/3/31.
 */
public class CountSignEventHandler implements EventHandler<Recommend> {
    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算签到区间得分
        int signScore = 0;
        for (UserRecommend userRecommend : recommend.getMainUser()) {
            int mainSign = userRecommend.getSignInfo();
            for (UserRecommend matchUser : recommend.getMatchUser()) {
                if (mainSign == matchUser.getSignInfo()) {
                    signScore += 9;
                }
            }
        }
        recommend.totalE = signScore;
//        System.out.println("计算签到区间得分" + signScore);
    }
}
