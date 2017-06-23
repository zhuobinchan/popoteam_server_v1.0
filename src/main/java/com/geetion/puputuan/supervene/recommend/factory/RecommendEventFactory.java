package com.geetion.puputuan.supervene.recommend.factory;

import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.lmax.disruptor.EventFactory;

/**
 * Created by mac on 16/3/30.
 */
public class RecommendEventFactory implements EventFactory<Recommend> {
    public Recommend newInstance() {
        //TODO make a recommed include mainGroup and matchGroup
        Recommend recommend = new Recommend();
        return recommend;
    }
}