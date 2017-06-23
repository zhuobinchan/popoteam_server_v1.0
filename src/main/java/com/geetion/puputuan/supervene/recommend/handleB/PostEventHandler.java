package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.model.Vote;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.VoteService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16/3/31.
 */
public class PostEventHandler implements EventHandler<Recommend> {
    private RecommendService recommendService;
    private VoteService voteService;

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 计算投票得分
        recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
        voteService = SpringLoader.getInstance().getBean(VoteService.class);
        Long mainGroupId = recommend.getMainGroup().getId();
        Long matchGroupId = recommend.getMatchGroup().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("mainGroupId", matchGroupId);
        params.put("matchGroupId", mainGroupId);

        //找出匹配的队伍
        int totalScore = 0;
        com.geetion.puputuan.model.Recommend matchRecommend = recommendService.getRecommend(params);
        if (matchRecommend != null) {
            params.clear();
            params.put("recommendId", matchRecommend.getId());
            params.put("isLike", true);
            //
            List<Vote> list = voteService.getVoteList(params);
            int size = 0;
            if (list != null && !list.isEmpty()) {
                size = list.size();
            }
            totalScore = size * 45;
        }

        recommend.totalC = totalScore;
    }
}
