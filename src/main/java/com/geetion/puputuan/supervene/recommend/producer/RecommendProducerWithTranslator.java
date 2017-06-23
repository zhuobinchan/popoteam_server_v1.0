package com.geetion.puputuan.supervene.recommend.producer;

import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.supervene.recommend.model.MainTeam;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.model.UserRecommend;
import com.lmax.disruptor.EventTranslatorThreeArg;

import java.util.List;

/**
 * Created by mac on 16/3/30.
 */
public class RecommendProducerWithTranslator implements EventTranslatorThreeArg<Recommend, MainTeam, Long[], List<User>> {

    @Override
    public void translateTo(Recommend recommend, long l, MainTeam mainTeam, Long[] match, List<User> users) {
        Group mainGroup = new Group();
        mainGroup.setId(mainTeam.getMainID());
        recommend.setMainGroup(mainGroup);
        recommend.setAreaId(mainTeam.getAreaID());
        Group matchGroup = new Group();
        matchGroup.setId(match[0]);
        recommend.setMatchGroup(matchGroup);
        recommend.setType(mainTeam.getType());
        recommend.setMatchSize(match[1]);

        for (User user : users) {
            UserRecommend userRecommend = new UserRecommend(user);
            recommend.getMainUser().add(userRecommend);
        }
    }
}