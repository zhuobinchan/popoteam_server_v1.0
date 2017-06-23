package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.service.SignService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by mac on 16/3/31.
 */
public class MainSignEventHandler implements EventHandler<Recommend> {

    private SignService signService;
    private Logger logger = Logger.getLogger(MainSignEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出main团队的所有签到信息
        signService = SpringLoader.getInstance().getBean(SignService.class);
        for (UserRecommend user : recommend.getMainUser()) {
            Map<String, Object> mapSign = signService.getMaxArea(user.getUserId());
            if (mapSign != null) {
                Object maxArea = mapSign.get("timeArea");
                if (maxArea != null) {
                    user.setSignInfo(((Double) maxArea).intValue());
                }
            }
        }
//        logger.info("找出main团队的所有签到信息");
    }
}
