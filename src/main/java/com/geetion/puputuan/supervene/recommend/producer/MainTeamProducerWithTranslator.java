package com.geetion.puputuan.supervene.recommend.producer;

import com.geetion.puputuan.supervene.recommend.model.MainTeam;
import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * Created by mac on 16/3/30.
 */
public class MainTeamProducerWithTranslator implements EventTranslatorOneArg<MainTeam, Long> {
    @Override
    public void translateTo(MainTeam mainTeam, long l, Long aLong) {
        mainTeam.setMainID(aLong);
    }
}