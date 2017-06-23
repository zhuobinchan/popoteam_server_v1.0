package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.FriendRelationshipService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/3/31.
 */
public class FriendRelationEventHandler implements EventHandler<Recommend> {
    private FriendRelationshipService friendRelationshipService;
    private Logger logger = Logger.getLogger(FriendRelationEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出main,match团队关系度
        friendRelationshipService = SpringLoader.getInstance().getBean(FriendRelationshipService.class);
        List<Long> matchIds = new ArrayList<>();
        for (User user : recommend.getMatchUser()) {
            matchIds.add(user.getUserId());
        }
        //TODO 找出一级朋友关系网
        List<Long> firstFriend = friendRelationshipService.getFriendIds(matchIds);
        // logger.info("match队伍[" + recommend.getMatchGroup().getId() + "]一级朋友关系:" + firstFriend.size());
        recommend.getFirstFriend().addAll(firstFriend);

        //TODO 找出二级朋友关系网
        List<Long> secondFriend = friendRelationshipService.getFriendIds(firstFriend);
        // logger.info("match队伍[" + recommend.getMatchGroup().getId() + "]二级朋友关系:" + secondFriend.size());
        recommend.getFirstFriend().addAll(secondFriend);
    }
}
