package com.geetion.puputuan.supervene.recommend.factory;

import com.geetion.puputuan.supervene.recommend.model.MainTeam;
import com.lmax.disruptor.EventFactory;

/**
 * Created by mac on 16/3/30.
 */
public class MainTeamEventFactory implements EventFactory<MainTeam> {
    public MainTeam newInstance() {
        //TODO make a recommed include mainGroup and matchGroup
        MainTeam mainTeam = new MainTeam();
        return mainTeam;
    }
}