package com.geetion.puputuan.supervene.recommend.handlerAB;

import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.GroupMemberService;
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
 * Created by mac on 16/3/31.
 */
public class MatchTeamInfoEventHandler implements EventHandler<Recommend> {
    private GroupMemberService groupMemberService;
    private Logger logger = Logger.getLogger(MatchTeamInfoEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出MatchTeam里面所有人员信息
        groupMemberService = SpringLoader.getInstance().getBean(GroupMemberService.class);
        long groupId = recommend.getMatchGroup().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        //找出团队的所有用户以及用户的所有详细信息
        List<GroupMember> list = groupMemberService.getGroupMemberList(params);
        if (list.isEmpty()) {
            throw new Exception("matchId :" + groupId + " 没有团员 / group do not have members");
        }
        List<UserRecommend> matchUserList = new ArrayList<>();
        StringBuffer loggerString = new StringBuffer();
        for (GroupMember groupMember : list) {
            User user = groupMember.getUser();
            matchUserList.add(new UserRecommend(user));
            loggerString.append("\n matchTeam用户" + groupId + "/" + user.getId() + " 详细资料 ");
        }
        recommend.setMatchUser(matchUserList);
//        logger.info(loggerString);
    }


}