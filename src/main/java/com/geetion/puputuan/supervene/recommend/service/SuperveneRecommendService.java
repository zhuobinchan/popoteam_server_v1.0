package com.geetion.puputuan.supervene.recommend.service;

import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.supervene.recommend.disruptor.DisrupterManager;
import com.geetion.puputuan.supervene.recommend.factory.MainTeamEventFactory;
import com.geetion.puputuan.supervene.recommend.factory.RecommendEventFactory;
import com.geetion.puputuan.supervene.recommend.handleA.FindMatchEventHandler;
import com.geetion.puputuan.supervene.recommend.handleB.*;
import com.geetion.puputuan.supervene.recommend.handleC.*;
import com.geetion.puputuan.supervene.recommend.handleD.RecomendInDbEventHandler;
import com.geetion.puputuan.supervene.recommend.handlerAB.MatchTeamInfoEventHandler;
import com.geetion.puputuan.supervene.recommend.model.MainTeam;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.producer.MainTeamProducerWithTranslator;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import org.apache.log4j.Logger;

import java.util.Hashtable;

/**
 * Created by mac on 16/3/30.
 */
public class SuperveneRecommendService {
    private Logger logger = Logger.getLogger(SuperveneRecommendService.class);
    public static Hashtable<Long, Long> GroupCount = new Hashtable<>();
    private FindMatchEventHandler findMatchEventHandler = new FindMatchEventHandler();
    private MatchTeamInfoEventHandler matchTeamInfoEventHandler = new MatchTeamInfoEventHandler();
    private MainInterestEventHandler mainInterestEventHandler = new MainInterestEventHandler();
    private MatchInterestEventHandler matchInterestEventHandler = new MatchInterestEventHandler();
    private PostEventHandler postEventHandler = new PostEventHandler();
    private CountInterestEventHandler countInterestEventHandler = new CountInterestEventHandler();
    private MainJobEventHandler mainJobEventHandler = new MainJobEventHandler();
    private MatchJobEventHandler matchJobEventHandler = new MatchJobEventHandler();
    private CountJobEventHandler countJobEventHandler = new CountJobEventHandler();
    private MainLikeEventHandler mainLikeEventHandler = new MainLikeEventHandler();
    private MatchLikeEventHandler matchLikeEventHandler = new MatchLikeEventHandler();
    private CountLikeEventHandler countLikeEventHandler = new CountLikeEventHandler();
    private CountConstellatoryEventHandler countConstellatoryEventHandler = new CountConstellatoryEventHandler();
    private MainSignEventHandler mainSignEventHandler = new MainSignEventHandler();
    private MatchSignEventHandler matchSignEventHandler = new MatchSignEventHandler();
    private CountSignEventHandler countSignEventHandler = new CountSignEventHandler();
    private FriendRelationEventHandler friendRelationEventHandler = new FriendRelationEventHandler();
    private CountRelationEventHandler countRelationEventHandler = new CountRelationEventHandler();

    private RecomendInDbEventHandler recomendInDbEventHandler = new RecomendInDbEventHandler();
    private TeamNumberEventHandler teamNumberEventHandler = new TeamNumberEventHandler();

    private RecommendEventFactory recommendEventFactory = new RecommendEventFactory();

    private MainTeamEventFactory mainTeamEventFactory = new MainTeamEventFactory();

    private BlockingWaitStrategy blockingWaitStrategy = new BlockingWaitStrategy();
    private BlockingWaitStrategy blockingWaitStrategy2 = new BlockingWaitStrategy();
    //初始化
    private DisrupterManager<MainTeam> mainTeamDisrupterManager = new DisrupterManager<>();
    //计算用
    private DisrupterManager<Recommend> disrupterManager = new DisrupterManager<>();

    private MainTeamProducerWithTranslator mainTeamProducerWithTranslator = new MainTeamProducerWithTranslator();

    private RingBuffer<MainTeam> mainTeamRingBuffer;

    private RingBuffer<Recommend> ringBuffer;

    public static long beginTime;

    private RecommendService recommendService;
    //男队区域
    public static Hashtable<Long, Hashtable<Long, Boolean>> areaManTeamMap = new Hashtable<>();
    //女队区域
    public static Hashtable<Long, Hashtable<Long, Boolean>> areaFemaleTeamMap = new Hashtable<>();

    /**
     * 初始化内存池, 将所有匹配中的队伍推进内存空间
     */
    public void initPutIntoMemory() {

    }

    /**
     * 清空寄存器某一个teamId
     *
     * @param areaId
     * @param teamId
     * @param teamType
     */
    public void cleanMemorystatus(Long areaId, Long teamId, int teamType) {
        Hashtable<Long, Boolean> teamMap = null;
        if (teamType == GroupTypeAndStatus.GROUP_FEMALE) {
            teamMap = areaFemaleTeamMap.get(areaId);
        } else {
            teamMap = areaManTeamMap.get(areaId);
        }
        if (teamMap != null) {
            teamMap.remove(teamId);
        }
        GroupCount.remove(teamId);
        logger.info("清楚内存器 : id" + teamId);

    }

    /**
     * 进行推荐算法
     *
     * @param mainTeamID
     */
    public void doRecommend(Long mainTeamID) {
        try {
            //每次先清空数据库所有mainTeamID对应的队伍
            recommendService = SpringLoader.getInstance().getBean(RecommendService.class);
            recommendService.removeMainIdRecommend(mainTeamID);

            beginTime = System.currentTimeMillis();
            //构造第一个分发器
            mainTeamDisrupterManager.setWaitStrategy(blockingWaitStrategy)
                    .setFactory(mainTeamEventFactory)
                    .build();
            //构造第二个分发器
            disrupterManager.setFactory(recommendEventFactory)
                    .setWaitStrategy(blockingWaitStrategy2)
                    .build();
            buildHandlerSequence(disrupterManager);
            ringBuffer = disrupterManager.startEvent();
            buildMatchHandler(mainTeamDisrupterManager, ringBuffer);
            mainTeamRingBuffer = mainTeamDisrupterManager.startEvent();
            mainTeamRingBuffer.publishEvent(mainTeamProducerWithTranslator, mainTeamID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buildMatchHandler(DisrupterManager disrupterManager, RingBuffer<Recommend> ringBuffer) throws Exception {
        findMatchEventHandler.setRingBuffer(ringBuffer);
        //找出所有匹配的队伍  match Team
        disrupterManager.handleEventsWith(findMatchEventHandler);
    }

    private void buildHandlerSequence(DisrupterManager disrupterManager) throws Exception {

        //找出匹配队伍用户信息
        EventHandlerGroup stepABGroup = disrupterManager.handleEventsWith(matchTeamInfoEventHandler);

        //找出兴趣爱好
        EventHandlerGroup stepInterestGroup = stepABGroup.handleEventsWith(mainInterestEventHandler, matchInterestEventHandler);

        //计算投票得分
        EventHandlerGroup countPostGroup = stepABGroup.handleEventsWith(postEventHandler);

        //人数权重选择
        EventHandlerGroup countNumberEventGroup = stepABGroup.handleEventsWith(teamNumberEventHandler);

        //计算兴趣爱好得分
        EventHandlerGroup countIntrerstGroup = stepInterestGroup.handleEventsWith(countInterestEventHandler);

        //找出工作
        EventHandlerGroup stepJobGroup = stepABGroup.handleEventsWith(mainJobEventHandler, matchJobEventHandler);
        //计算工作得分
        EventHandlerGroup countJobGroup = stepJobGroup.handleEventsWith(countJobEventHandler);

        //计算星座得分
        EventHandlerGroup countConstellatoryGroup = stepABGroup.handleEventsWith(countConstellatoryEventHandler);

        //找出签到
        EventHandlerGroup stepSignGroup = stepABGroup.handleEventsWith(mainSignEventHandler, matchSignEventHandler);
        //计算签到得分
        EventHandlerGroup countSignGroup = stepSignGroup.handleEventsWith(countSignEventHandler);

        //找出关系
        EventHandlerGroup stepRelationGroup = stepABGroup.handleEventsWith(friendRelationEventHandler);
        //计算关系得分
        EventHandlerGroup countRelationGroup = stepRelationGroup.handleEventsWith(countRelationEventHandler);

        //找出like
        EventHandlerGroup stepLikeGroup = stepABGroup.handleEventsWith(mainLikeEventHandler, matchLikeEventHandler);
        //计算like得分
        EventHandlerGroup countLikeGroup = stepLikeGroup.handleEventsWith(countLikeEventHandler);

        //保存到数据库
        EventHandlerGroup finalGroup = countIntrerstGroup.and(countJobGroup)
                .and(countConstellatoryGroup)
                .and(countNumberEventGroup)
                .and(countLikeGroup)
                .and(countSignGroup)
                .and(countPostGroup)
                .and(countRelationGroup);

        finalGroup.then(recomendInDbEventHandler);
    }
}
