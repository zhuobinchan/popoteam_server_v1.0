package com.geetion.puputuan.engine.handlernew;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import com.geetion.puputuan.engine.handler.impl.ResetUserRecommendMatcher;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.redis.Lock;
import com.geetion.puputuan.redis.RedisBasedDistributedLock;
import com.geetion.puputuan.service.CommonService;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by guodikai on 2017/1/9.
 */
public abstract class RedisMatcher extends BaseMatcher implements Matcher {
    private final Logger logger = Logger.getLogger(RedisMatcher.class);

    private Matcher matcher;

    /**
     * 匹配队伍
     */
    public abstract boolean matchGroup(Long groupId, Long userId, Date time);

    /**
     * 执行下一个处理者
     * @param groupId
     * @return
     */
    public boolean nextMatchGroup(Long groupId, Long userId, Date time){
        if (this.getMatcher() != null){
            return this.getMatcher().matchGroup(groupId, userId,time);
        }
        return false;
    }
    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    /**
     * 判断当前执行的线程队伍信息是否是最新的
     * @param mainGroup
     * @return
     */
    public boolean ifNewestGroup(Group mainGroup, Date time){

        String s = runMatchUtil.getWithLock(RedisKeyAndLock.GROUP_WAITING_MODIFY + mainGroup.getId(), RedisKeyAndLock.LOCK_MODIFY_KEY + mainGroup.getId());
        // 判断队伍信息是否有发生过修改
        if(!String.valueOf(time.getTime()).equals(s)){
            logger.info("================MatchByRedisProducer.match begin, current is not the newest group ==========================");
            runMatchUtil.cleanRedisBean(mainGroup.getId() + "." + time.getTime());
            return false;
        }
        return true;
    }

    /**
     * 发送环信消息
     * 如果匹配算法匹配到新队伍，需要检查memberWaitingMap，判断是否需要给其他队员发送推送消息
     * 如果用户重新用户推荐信息表，则只给当前用户发消息
     * @param mainGroup
     * @param userId
     * @param ifRest
     */
    protected void sendRecommendMsgHuanxin(Group mainGroup, Long userId, Date time, boolean ifRest){
        logger.info("RedisMatcher mainGroup = " + mainGroup.getId() + ", userId = " + userId);
        logger.info("RedisMatcher thread name = " + Thread.currentThread().getName());
        List<GroupMember> groupMemberList = commonService.getGroupMember(mainGroup.getId());

        String[] token = new String[groupMemberList.size()];

        try {
            Lock lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_MEMBER_KEY + mainGroup.getId(), RedisKeyAndLock.LOCK_EXPIRE);
            boolean stop = false;
            boolean ifCleanBean = true;
            Set<String> waitingMember = new HashSet<>();
            List<Long> toSendMember = new ArrayList<>();
            String nextMember = null;
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    logger.info("======================= sendRecommendMsgHuanxin lock success =========================");
                    try {
                        if (ifRest){
                            // 当重置用户数据时，只给当前用户发送推送消息
                            toSendMember.add(userId);
                            // 从member缓存中将当前用户去除
                            redisUtil.srem(RedisKeyAndLock.getMemberKey(mainGroup.getId()), userId.toString());
                            // 随机取得member中一个成员
                            nextMember = redisUtil.srand(RedisKeyAndLock.getMemberKey(mainGroup.getId()));
                        }else{
                            waitingMember = redisUtil.sget(RedisKeyAndLock.GROUP_WAITING_MEMBER + mainGroup.getId());
                            for(String m : waitingMember){
                                logger.info("RedisMatcher sendRecommendMsgHuanxin waitingMember " + m);
                                redisUtil.srem(RedisKeyAndLock.getMemberKey(mainGroup.getId()), m);
                                toSendMember.add(Long.valueOf(m));
                            }
                        }

                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }

            if (ifRest && null != nextMember){
                logger.info("RedisMatcher nexMember " + nextMember);
                // 将bean中的userId改成新的成员
                redisUtil.hset(RedisKeyAndLock.getBeanKey(mainGroup.getId(), time.getTime()),"userId", nextMember);
                // 重新将队伍放入队列中
                runMatchUtil.rpushWithLock(RedisKeyAndLock.GROUP_WAITING_LIST, mainGroup.getId() + "." + time.getTime());

                ifCleanBean = false;
            }

            token = commonService.getUserAccountOrToken(CommonService.USER_ACCOUNT, toSendMember.toArray(new Long[toSendMember.size()]));

            logger.info("RedisMatcher sendRecommendMsgHuanxin tokens " + token.length);
            logger.info("RedisMatcher sendRecommendMsgHuanxin ifReset " + ifRest);
            logger.info("======================================================================");

            //发送环信消息 将推荐结果发送给当前队伍
            HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
            huanXinMessageExtras.setIfReset(ifRest);
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                    "恭喜，匹配到新的队伍", HuanXinSendMessageType.HAVE_MATCHING_RESULT, huanXinMessageExtras,
                    token);


            // 当重置数据，且等待队列中有其他成员时，才不需要清除bean
            if(ifCleanBean){
                runMatchUtil.cleanRedisBean(mainGroup.getId() + "." + time.getTime());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果队伍的recommend_sex为null，则返回异性队伍
     * 如果不为空，则直接返回对应类型
     * @param group
     * @return
     */
    protected Integer getMatchGroupType(Group group){
        if (null == group.getRecommendSex()){
            return group.getType() == GroupTypeAndStatus.GROUP_MALE ? GroupTypeAndStatus.GROUP_FEMALE : GroupTypeAndStatus.GROUP_MALE;
        } else {
            // 如果类型为混全类型，则不需要传type值
            return GroupTypeAndStatus.GROUP_ALL == group.getRecommendSex() ? null : group.getRecommendSex();
        }
    }




}
