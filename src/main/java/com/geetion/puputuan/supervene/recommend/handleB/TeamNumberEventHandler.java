package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.service.AnalyseMatch;
import com.lmax.disruptor.EventHandler;

/**
 * Created by mac on 16/3/31.
 */
public class TeamNumberEventHandler implements EventHandler<Recommend> {
    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 根据人数修改评分
        int mainNumber = recommend.getMainUser().size();
        int matchNumber = recommend.getMatchUser().size();
        String tag = mainNumber + "" + matchNumber;
        float weight = AnalyseMatch.getNumberMatch(tag);
        recommend.weight = weight;
//        System.out.println("根据人数修改评分" + "人数:" + tag + " 比例:" + weight);
    }
}
