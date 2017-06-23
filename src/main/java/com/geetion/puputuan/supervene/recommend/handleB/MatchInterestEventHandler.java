package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.InterestUserService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/3/31.
 */
public class MatchInterestEventHandler implements EventHandler<Recommend> {

    private InterestUserService interestUserService;
    private Logger logger = Logger.getLogger(MainInterestEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出Match团队里面所有成员的所有兴趣爱好
        interestUserService = SpringLoader.getInstance().getBean(InterestUserService.class);
        List<Long> matchIds = new ArrayList<>();
        for (User user : recommend.getMatchUser()) {
            matchIds.add(user.getUserId());
        }
        List<Long> mainInterestIds = interestUserService.getInterestIdsByUserIds(matchIds);
        recommend.mainInterest.addAll(mainInterestIds);

//        logger.info("找出Match团队里面所有的兴趣标签 " + mainInterestIds.size());
    }

}
