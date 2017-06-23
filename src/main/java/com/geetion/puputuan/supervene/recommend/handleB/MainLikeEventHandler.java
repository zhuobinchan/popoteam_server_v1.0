package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.PhotoLikeService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by mac on 16/4/18.
 */
public class MainLikeEventHandler implements EventHandler<Recommend> {
    private PhotoLikeService photoLikeService;
    private Logger logger = Logger.getLogger(MainLikeEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出main对里面所有like(match团中的人员)
        photoLikeService = SpringLoader.getInstance().getBean(PhotoLikeService.class);
        Map<String, Object> params = new HashMap<>();
        List<Long> matchIds = new ArrayList<>();
        for (User user : recommend.getMatchUser()) {
            matchIds.add(user.getUserId());
        }
        params.put("ids", matchIds);
        for (UserRecommend user : recommend.getMainUser()) {
            params.put("user_id", user.getUserId());
            List<HashMap<String, Long>> userMap = photoLikeService.getUserPhotoLikeIn(params);
            user.setLikeList(userMap);
        }
//        logger.info("找出main对里面所有like(match团中的人员) ");
    }
}
