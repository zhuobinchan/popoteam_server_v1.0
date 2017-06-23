package com.geetion.puputuan.engine;

import com.geetion.puputuan.engine.thread.RedisMatchThread;
import com.geetion.puputuan.redis.Lock;
import com.geetion.puputuan.redis.RedisBasedDistributedLock;
import com.geetion.puputuan.utils.RedisUtil;
import com.geetion.puputuan.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by guodikai on 2017/1/9.
 */
public class ScanWaitingGroupInRedis {
    private final Logger logger = Logger.getLogger(ScanWaitingGroupInRedis.class);

    private static final int GROUP_RUN_MATCH_EXPIRE = 1;
    @Resource
    private RedisUtil redisUtil;


    public void scanWaitingList() throws IOException {
        logger.info("=================== ScanWaitingGroupInRedis.scanWaitingList begin ===========================");
        excute();
    }

    public void excute(){
        RedisTemplate<Serializable, Serializable> redisTemplate = redisUtil.getRedisTemplate();
        ThreadPoolTaskExecutor poolTaskExecutor = SpringContextUtil.getBean("matchThreadPool");

        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            @Transactional
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] listKey = serializer.serialize(RedisKeyAndLock.GROUP_WAITING_LIST);
                int activeCount = poolTaskExecutor.getActiveCount();
                int usableCount = poolTaskExecutor.getCorePoolSize() - activeCount;

                Long len = connection.lLen(listKey);
                logger.info("======================= ScanWaitingGroupInRedis waiting list "+ len +"==================");

                // 缓存队列为空时，直接不处理
                if(0 == len){
                    return true;
                }

                // 通过比较连接池可用连接，以及缓存队列中的队伍数量，生成对应的线程
                if (len < usableCount) {
                    usableCount = len.intValue();
                }
                String waitingGroup ;
                byte[] beanKey;
                byte[] modifyKey;
                byte[] memberKey;
                Long groupId;
                Long userId;
                Long isReMatch;
                Long time;
                Long lastTime = 0L;
                byte[] groupIdByte = serializer.serialize("groupId");
                byte[] userIdByte = serializer.serialize("userId");
                byte[] isReMatchByte = serializer.serialize("isReMatch");
                byte[] timeByte = serializer.serialize("time");
                byte[] lastTimeByte = serializer.serialize("lastTime");
                String modifyTime;

                for (int i = 0; i < usableCount; i++) {

                    waitingGroup = serializer.deserialize(connection.lPop(listKey));
                    beanKey = serializer.serialize(RedisKeyAndLock.GROUP_WAITING_BEAN + waitingGroup);
                    logger.info("======================= ScanWaitingGroupInRedis lpop value " + waitingGroup + "=====================");

                    Map<byte[], byte[]> waitingGroupBean = connection.hGetAll(beanKey);
                    logger.info("======================= ScanWaitingGroupInRedis hmget value " + waitingGroupBean + "=====================");

                    if (null == waitingGroupBean || waitingGroupBean.size() == 0){
                        continue;
                    }

                    groupId = Long.valueOf(serializer.deserialize(waitingGroupBean.get(groupIdByte)));
                    userId = Long.valueOf(serializer.deserialize(waitingGroupBean.get(userIdByte)));
                    isReMatch = Long.valueOf(serializer.deserialize(waitingGroupBean.get(isReMatchByte)));
                    time = Long.valueOf(serializer.deserialize(waitingGroupBean.get(timeByte)));
                    if(null != waitingGroupBean.get(lastTimeByte)){
                        lastTime = Long.valueOf(serializer.deserialize(waitingGroupBean.get(lastTimeByte)));
                    }


                    // 如果bean的lastTime为0，直接执行匹配
                    // 否则判断间隔时间，大于1小时，执行匹配，小于1小时，放回list

                    if (!lastTime.equals(0L) && !checkIfOverTime(lastTime, new Date().getTime())){
                        logger.info("=======================  current group " + waitingGroup + " is not over 1 hour=====================");
                        connection.rPush(serializer.serialize(RedisKeyAndLock.GROUP_WAITING_LIST), serializer.serialize(waitingGroup));
                        continue;
                    }

                    modifyKey = serializer.serialize(RedisKeyAndLock.getModifyKey(groupId));
                    modifyTime = serializer.deserialize(connection.get(modifyKey));

                    // 当time、modifyTime为空，可能为赃数据，直接不处理，将bean删除
                    // 当前bean不是最新的数据，直接删除
                    if(null == time || null == modifyTime || (!time.toString().equals(modifyTime))){
                        logger.info("===================== ScanWaitingGroupInRedis not the newest group ===================");
                        connection.del(beanKey);
                        continue;
                    }

                    logger.info("======================= ScanWaitingGroupInRedis modifyTime " + modifyTime + "=====================");
                    // 启动匹配规则线程=
                    poolTaskExecutor.execute(new RedisMatchThread(groupId,
                            userId,
                            isReMatch,
                            new Date(time)));
                }
                return true;
            }
        });
        logger.info("=================== ScanWaitingGroupInRedis.scanWaitingList stop ===========================");
    }

    public boolean checkIfOverTime(Long begin, Long end){
        long betweenDays = (long)Math.abs((end - begin) / (1000 * 60 ) + 0.5);

        return betweenDays >= GROUP_RUN_MATCH_EXPIRE;
    }

}
