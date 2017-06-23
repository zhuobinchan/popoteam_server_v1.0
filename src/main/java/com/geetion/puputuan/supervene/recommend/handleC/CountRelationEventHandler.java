package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mac on 16/3/31.
 */
public class CountRelationEventHandler implements EventHandler<Recommend> {
    private Logger logger = Logger.getLogger(CountRelationEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算关系得分
        List<Long> mainIds = new ArrayList<>();
        HashMap<Long, Long> mainLevel = new HashMap<>();
        for (UserRecommend userRecommend : recommend.getMainUser()) {
            mainIds.add(userRecommend.getUserId());
            //默认朋友关系为深度2
            mainLevel.put(userRecommend.getUserId(), 2l);
        }
        //一级朋友关系判断
        for (Long friendId : recommend.getSecondFriend()) {
            boolean isContains = mainIds.contains(friendId);
            if (isContains) {
                mainLevel.put(friendId, 1l);
            }
        }

        //直接朋友关系判断
        for (Long friendId : recommend.getFirstFriend()) {
            boolean isContains = mainIds.contains(friendId);
            if (isContains) {
                mainLevel.put(friendId, 0l);
            }
        }

        //计算深度得分
        int friendScore = 0;
        for (UserRecommend userRecommend : recommend.getMainUser()) {
            long level = mainLevel.get(userRecommend.getUserId());
            if (level == 0) {
                friendScore += 0 * 5;
            } else if (level == 1) {
                friendScore += 4 * 5;
            } else {
                friendScore += 9 * 5;
            }
        }
        recommend.totalB = friendScore;
//        System.out.println("计算关系得分 " + friendScore);
    }
}
