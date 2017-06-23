package com.geetion.puputuan.supervene.recommend.handleC;

import com.geetion.puputuan.service.PhotoLikeService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16/4/18.
 */
public class CountLikeEventHandler implements EventHandler<Recommend> {

    private PhotoLikeService photoLikeService;
    private Logger logger = Logger.getLogger(CountLikeEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算JOB得分
        int singleScore = 0;
        int eachOtherScore = 0;
        photoLikeService = SpringLoader.getInstance().getBean(PhotoLikeService.class);
        Map<String, Object> params = new HashMap<>();
        List<Long> matchIds = new ArrayList<>();
        List<Long> mainIds = new ArrayList<>();
        //循环遍历主队每个用户的单项点赞数
        for (UserRecommend user : recommend.getMatchUser()) {
            matchIds.add(user.getUserId());
            for (HashMap<String, Long> likemap : user.getLikeList()) {
                long likeNumber = likemap.get("number");
                if (likeNumber > 10) {
                    likeNumber = 10;
                }
                singleScore += likeNumber * 1;
            }
        }
        params.put("matchIds", matchIds);

        for (UserRecommend user : recommend.getMainUser()) {
            mainIds.add(user.getUserId());
        }
        params.put("mainIds", mainIds);
        List<HashMap<String, Long>> eachOtherLikes = photoLikeService.getEachOtherPhotoLike(params);
        for (HashMap<String, Long> map : eachOtherLikes) {
            Long mainId = map.get("lUser");
            Long matchId = map.get("pUser");
//            logger.info("非朋友互相点赞" + mainId.longValue() + " -- " + matchId);
        }
        eachOtherScore = eachOtherLikes.size() * 5;
//        System.out.println("计算Like得分 单相点赞 :" + singleScore + " -- 相向点赞 :" + eachOtherScore);
        recommend.totalA = eachOtherScore + singleScore;
    }
}
