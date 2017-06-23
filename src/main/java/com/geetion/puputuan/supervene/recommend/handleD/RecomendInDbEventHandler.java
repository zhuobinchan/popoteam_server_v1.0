package com.geetion.puputuan.supervene.recommend.handleD;

import com.geetion.generic.serverfile.utils.HttpUtil2;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.model.RecommendHistory;
import com.geetion.puputuan.service.RecommendHistoryService;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.service.SuperveneRecommendService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by mac on 16/3/31.
 */
public class RecomendInDbEventHandler implements EventHandler<Recommend> {
    private RecommendService recommendService;
    private RecommendHistoryService recommendHistoryService;

    @Override
    public void onEvent(Recommend recommend, long l, boolean isLast) throws Exception {
        //TODO 得分如数据库
        long totalScore = recommend.totalA + recommend.totalB + recommend.totalC + recommend.totalD + recommend.totalE + recommend.totalF + recommend.totalF2;
        recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
        recommendHistoryService = SpringLoader.getInstance().getBean(RecommendHistoryService.class);

//        System.out.println("保存据库" + totalScore + ", 人数权重比 " + recommend.weight);
//        System.out.println("总耗时:" + (System.currentTimeMillis() - SuperveneRecommendService.beginTime) + " ,最后一步" + isLast + " long " + l);
        Long count = 0l;
        if (recommend.getMatchSize() != -1) {
            count = SuperveneRecommendService.GroupCount.get(recommend.getMainGroup().getId());
            count = count == null ? 1 : count + 1;
            SuperveneRecommendService.GroupCount.put(recommend.getMainGroup().getId(), count);
        }
        com.geetion.puputuan.model.Recommend recommendRecord = new com.geetion.puputuan.model.Recommend();
        RecommendHistory recommendHistory = new RecommendHistory();

        recommendRecord.setMainGroupId(recommend.getMainGroup().getId());
        recommendRecord.setMatchGroupId(recommend.getMatchGroup().getId());
        recommendService.removeMainIdMatchIdRecommend(recommend.getMainGroup().getId(), recommend.getMatchGroup().getId());
        recommendRecord.setScoreA((int) recommend.getTotalA());
        recommendRecord.setScoreB((int) recommend.getTotalB());
        recommendRecord.setScoreC((int) recommend.getTotalC());
        recommendRecord.setScoreD((int) recommend.getTotalD());
        recommendRecord.setScoreE((int) recommend.getTotalE());
        recommendRecord.setScoreF((int) (recommend.getTotalF() + recommend.getTotalF2()));
        recommendRecord.setWeight((double) recommend.getWeight());
        recommendService.addRecommend(recommendRecord);
        recommendHistory.setMainGroupId(recommend.getMainGroup().getId());
        recommendHistory.setMatchGroupId(recommend.getMatchGroup().getId());
        recommendHistory.setScoreA((int) recommend.getTotalA());
        recommendHistory.setScoreB((int) recommend.getTotalB());
        recommendHistory.setScoreC((int) recommend.getTotalC());
        recommendHistory.setScoreD((int) recommend.getTotalD());
        recommendHistory.setScoreE((int) recommend.getTotalE());
        recommendHistory.setScoreF((int) (recommend.getTotalF() + recommend.getTotalF2()));
        recommendHistory.setWeight((double) recommend.getWeight());
        recommendHistory.setRecommendId(recommendRecord.getId());
        recommendHistoryService.addRecommendHistory(recommendHistory);

        //清除堆栈是否有队伍在
        Hashtable<Long, Boolean> teamMap = null;
        if (recommend.getType() == GroupTypeAndStatus.GROUP_MALE) {
            teamMap = SuperveneRecommendService.areaManTeamMap.get(recommend.areaId);
        } else {
            teamMap = SuperveneRecommendService.areaFemaleTeamMap.get(recommend.areaId);
        }
        Boolean status = teamMap.get(recommend.getMainGroup().getId());
        if (status != null)
            synchronized (status) {
                if (count == recommend.getMatchSize()) {
                    //TODO go api notification
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json;charset=UTF-8");
                    String url = "http://localhost:8080/app/group/pushRecommend?groupId=" + recommend.getMainGroup().getId();
//                    String url = "http://120.76.26.114:8080/app/group/pushRecommend?groupId=" + recommend.getMainGroup().getId();
                    String result = HttpUtil2.get(url, headers);
//                    System.out.println("\n http://120.76.26.114:8080 匹配的队伍，推送出去 " + "\n");
                    //清除计算器
                    SuperveneRecommendService.GroupCount.remove(recommend.getMainGroup().getId());
                    //清除状态记录
                    teamMap.put(recommend.getMainGroup().getId(), true);
                }
            }
    }
}
