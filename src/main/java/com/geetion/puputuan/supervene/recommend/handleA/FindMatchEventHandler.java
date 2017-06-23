package com.geetion.puputuan.supervene.recommend.handleA;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.GroupMemberService;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.supervene.recommend.model.MainTeam;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.producer.RecommendProducerWithTranslator;
import com.geetion.puputuan.supervene.recommend.service.SuperveneRecommendService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by mac on 16/3/31.
 */
public class FindMatchEventHandler implements EventHandler<MainTeam> {
    private RingBuffer<Recommend> ringBuffer;
    private RecommendProducerWithTranslator recommendProducerWithTranslator = new RecommendProducerWithTranslator();
    private GroupService groupService;
    private GroupMemberService groupMemberService;
    private Logger logger = Logger.getLogger(FindMatchEventHandler.class);

    public void setRingBuffer(RingBuffer<Recommend> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void onEvent(MainTeam mainTeam, long l, boolean b) throws Exception {
        //TODO 找出所有Main的团队的Match团队ID
        List<Group> matchGroupList = findMatchGroup(mainTeam);
        //TODO 找出MainTeam里面所有人员信息
        List<User> userList = findMainGroupInfo(mainTeam.getMainID());
        //计算器,用于记录每个mainteam计算到的步伐是否最后一个
        SuperveneRecommendService.GroupCount.put(mainTeam.getMainID(), 0l);

        //TODO 首先找出同区域在计算的队伍
        Hashtable<Long, Boolean> teamMap;
//        System.out.println("主队男女" + mainTeam.getType());
        if (mainTeam.getType() == GroupTypeAndStatus.GROUP_MALE) {
            teamMap = SuperveneRecommendService.areaFemaleTeamMap.get(mainTeam.getAreaID());
            if (teamMap != null) {
                if (SuperveneRecommendService.areaFemaleTeamMap.get(mainTeam.getCityID()) != null)
                    teamMap.putAll(SuperveneRecommendService.areaFemaleTeamMap.get(mainTeam.getCityID()));
            } else {
                teamMap = SuperveneRecommendService.areaFemaleTeamMap.get(mainTeam.getCityID());
            }
        } else {
            teamMap = SuperveneRecommendService.areaManTeamMap.get(mainTeam.getAreaID());
        }
        if (teamMap != null) {
            logger.info("找出内存性别树 -- " + teamMap.size());
            Set<Long> keys = teamMap.keySet();
            for (Long key : keys) {
                Boolean status = teamMap.get(key);
                if (key != mainTeam.getMainID()) {
                    List<User> otherTeamList = findMainGroupInfo(key);
                    //发送到下一个子任务计算
                    logger.info("团队id:" + mainTeam.getMainID() + " 子任务匹配团队Id :" + key);
                    //TODO 如果status已经结束了
                    long totalSize = 1l;
                    if (!status) {
                        totalSize = -1;
                    }
                    Long[] match = new Long[]{mainTeam.getMainID(), totalSize};
                    MainTeam otherTeam = new MainTeam();
                    otherTeam.setAreaID(mainTeam.getAreaID());
                    otherTeam.setMainID(key);
                    logger.info("内存推出计算队伍 " + otherTeam.getMainID() + " match队伍 " + match[0]);
                    ringBuffer.publishEvent(recommendProducerWithTranslator, otherTeam, match, otherTeamList);
                }
            }
        }

        //内存推送完毕, 主队推进内存
        if (mainTeam.getType() == GroupTypeAndStatus.GROUP_MALE) {
            teamMap = SuperveneRecommendService.areaManTeamMap.get(mainTeam.getAreaID());
        } else {
            teamMap = SuperveneRecommendService.areaFemaleTeamMap.get(mainTeam.getAreaID());
        }
        if (teamMap == null) {
            teamMap = new Hashtable<>();
        }
        //记录进入匹配队伍的所有团队以及计算状态
        if (matchGroupList != null && !matchGroupList.isEmpty()) {
            teamMap.put(mainTeam.getMainID(), false);
        } else {
            teamMap.put(mainTeam.getMainID(), true);
        }
        if (mainTeam.getType() == GroupTypeAndStatus.GROUP_MALE) {
//            System.out.println("进入内存器 -- menTree" + mainTeam.getMainID());
            SuperveneRecommendService.areaManTeamMap.put(mainTeam.getAreaID(), teamMap);
        } else {
//            System.out.println("进入内存器 -- femaleTree" + mainTeam.getMainID());
            SuperveneRecommendService.areaFemaleTeamMap.put(mainTeam.getAreaID(), teamMap);
        }
        for (Group matchGroup : matchGroupList) {
            long matchId = matchGroup.getId();
            Long[] match = {matchId, Long.valueOf(matchGroupList.size())};
//            System.out.println("主队计算器 " + mainTeam.getMainID() + " match队伍 " + match[0]);
            ringBuffer.publishEvent(recommendProducerWithTranslator, mainTeam, match, userList);
        }

    }

    /**
     * 找出mainTeam里面所有匹配的matchTeamId
     *
     * @param mainTeam
     * @return
     * @throws Exception
     */
    public List<Group> findMatchGroup(MainTeam mainTeam) throws Exception {
        groupService = SpringLoader.getInstance().getBean(GroupService.class);
        long mainId = mainTeam.getMainID();
        Group group = groupService.getGroupById(mainId);
        if (group.getAreaId() != null)
            mainTeam.setAreaID(group.getAreaId());
        else
            mainTeam.setAreaID(group.getCityId());
        mainTeam.setType(group.getType());
        mainTeam.setCityID(group.getCityId());
        if (group == null) {
            throw new Exception("团队ID不存在当前数据库 / mainTeam ID doesn't exist in DB .");
        }
        Map<String, Object> params = new HashMap<>();
        //找出地区匹配的所有队伍
        int mainType = group.getType();
        if (group.getAreaId() != null)
            params.put("areaId", group.getAreaId());
        else
            params.put("cityId", group.getCityId());
        if (mainType == GroupTypeAndStatus.GROUP_FEMALE) {
            params.put("type", GroupTypeAndStatus.GROUP_MALE);
        } else {
            params.put("type", GroupTypeAndStatus.GROUP_FEMALE);
        }
        params.put("status", 1);
        List<Group> matchGroupList = groupService.getGroupList(params);
//        logger.info("团队id:" + mainTeam.getMainID() + "  step one 通过地区匹配结果有:" + matchGroupList.size() + "团队符合要求");
        return matchGroupList;
    }

    /**
     * 找出teamId团队里面所有人员信息
     *
     * @param teamId
     */
    public List<User> findMainGroupInfo(long teamId) {
        groupMemberService = SpringLoader.getInstance().getBean(GroupMemberService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", teamId);
        //找出团队的所有用户以及用户的所有详细信息
        List<GroupMember> list = groupMemberService.getGroupMemberList(params);
        if (!list.isEmpty()) {
            List<User> mainUserList = new ArrayList<>();
            StringBuffer loggerString = new StringBuffer("\n");
            for (GroupMember groupMember : list) {
                User user = groupMember.getUser();
                mainUserList.add(user);
//                loggerString.append("mainTeam用户" + mainId + "/" + user.getId() + " 详细资料 \n");
            }
//            logger.info(loggerString);
            return mainUserList;
        } else {
            logger.error("main team have no member!!!");
            return null;
        }
    }
}
