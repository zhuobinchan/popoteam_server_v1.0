package com.geetion.puputuan.utils;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.redis.Lock;
import com.geetion.puputuan.redis.RedisBasedDistributedLock;
import com.geetion.puputuan.service.GroupService;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 操作匹配算法redis缓存的工具类
 * Created by guodikai on 2017/1/10.
 */
@Component
public class RunMatchUtil {
    private final Logger logger = Logger.getLogger(RunMatchUtil.class);
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private GroupService groupService;
    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;



    /**
     * 添加队伍对应的缓存数据
     * 当已经有对应的bean值存在，直接将当前用户添加至member缓存中，否则在list中新增队伍，新建bean
     * 当modifyTime已经存在时，进行更新，否则新增
     * @param group
     * @param isReMatch
     */
    private java.util.concurrent.locks.Lock lock = new ReentrantLock();
    public void addToRedis(Group group, boolean isReMatch, List<Long> waitToAdd, List<Long> waitToRemove){
        logger.info("====================== RunMatchUtil addToRedis =====================");
        // 添加同步锁，避免多个连接池操作redis时，出现数据混乱
        lock.lock();
        try {

            RedisTemplate<Serializable, Serializable> redisTemplate = redisUtil.getRedisTemplate();

            redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection)
                        throws DataAccessException {

                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] beanKey = serializer.serialize(RedisKeyAndLock.getBeanKey(group.getId(), Long.valueOf(group.getRunTime())));
                    byte[] memberKey = serializer.serialize(RedisKeyAndLock.getMemberKey(group.getId()));
                    byte[] modifyKey = serializer.serialize(RedisKeyAndLock.getModifyKey(group.getId()));
                    byte[] userId = serializer.serialize(shiroService.getLoginUserBase().getId().toString());
                    byte[] modifyTime = serializer.serialize(String.valueOf(group.getRunTime()));
//                    byte[] createTime = serializer.serialize(String.valueOf(new Date().getTime()));
                    // 用于当队伍匹配不到新队伍时，每隔一小时再重新跑一次匹配算法
                    byte[] lastTime = serializer.serialize(String.valueOf(0));

                    // 先将缓存中的修改时间更新
                    connection.set(modifyKey, modifyTime);

                    // 测试过程中，有时会返回null，由于无法判断是否有bean存在，所以直接返回，然后再等app端触发
//                    if(null == connection.exists(beanKey)){
//                        return true;
//                    }

                    // 判断缓存中是否有对应的bean存在
                    // 如果存在时，将当前成员放至等待队列中，并更新bean的创建时间
                    if(null != connection.exists(beanKey) && true == connection.exists(beanKey)){
                        logger.info("=============== RunMatchUtil addToRedis bean exists ==========================");
                        connection.sAdd(memberKey, userId);
                        if(isReMatch){
                            connection.hSet(beanKey, serializer.serialize("lastTime"), lastTime);
                        }
                        return true;
                    }

                    logger.info("=============== RunMatchUtil addToRedis bean not exists ==========================");
                    // 将当前队伍放至队伍中
                    String groupWaitingListId = group.getId() + "."  + group.getRunTime();
                    connection.rPush(serializer.serialize(RedisKeyAndLock.GROUP_WAITING_LIST), serializer.serialize(groupWaitingListId));

                    Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
                    hash.put(serializer.serialize("groupId"), serializer.serialize(group.getId().toString()));
                    hash.put(serializer.serialize("userId"), userId);
                    hash.put(serializer.serialize("isReMatch"), serializer.serialize(isReMatch ? "1" : "0"));
                    hash.put(serializer.serialize("time"), modifyTime);
                    hash.put(serializer.serialize("lastTime"), lastTime);
//                    hash.put(serializer.serialize("tryTimes"), serializer.serialize("0"));
                    connection.hMSet(beanKey, hash);
                    connection.sAdd(memberKey, userId);

                    if (null != waitToAdd && waitToAdd.size() > 0){
                         for (Long u : waitToAdd){
                             connection.sAdd(memberKey, serializer.serialize(u.toString()));
                         }
                    }

                    if (null != waitToRemove && waitToRemove.size() > 0){
                        for (Long u : waitToRemove){
                            connection.sRem(memberKey, serializer.serialize(u.toString()));
                        }
                    }

                    return true;
                }
            });
        }catch (Exception e){
            System.out.println("=================================addToRedis error " + group.getId() + "==============================");
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    /**
     * 清除队伍缓存信息
     *
     */
    public void cleanRedis(Long groupId){

        try{

            RedisTemplate<Serializable, Serializable> redisTemplate = redisUtil.getRedisTemplate();

            redisTemplate.execute(new RedisCallback<Boolean>() {

                @Override
                @Transactional
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] memberKey = serializer.serialize(RedisKeyAndLock.getMemberKey(groupId));
                    byte[] modifyKey = serializer.serialize(RedisKeyAndLock.getModifyKey(groupId));
                    // 通过通配符，取得所有相关key，然后进行删除
                    Set<byte[]> keys = connection.keys(serializer.serialize(RedisKeyAndLock.GROUP_WAITING_BEAN + groupId + ".*"));
                    for (byte[] b : keys){
                        connection.del(b);
                    }
                    connection.del(memberKey, modifyKey);
                    return true;
                }
            });
        }catch (Exception e){
            System.out.println("=================================cleanRedis error "+ groupId +" ==========================================");
            logger.error(e.getMessage());
            e.printStackTrace();
        }


    }



    /**
     * 清除reids中缓存的队伍
     * @param groupId
     * @return 返回被清除的队伍
     */
    private List<String> cleanRedisList(Long groupId){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_LIST_KEY, RedisKeyAndLock.LOCK_EXPIRE);
            List<String> add = new ArrayList<>();
            List<String> remove = new ArrayList<>();
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        Long len = redisUtil.llen(RedisKeyAndLock.getListKey());
                        for(int i = 0; i < len; i++){
                            String value = redisUtil.lpop(RedisKeyAndLock.getListKey());
                            if (value.startsWith(String.valueOf(groupId))){
                                remove.add(value);
                            }else {
                                add.add(value);
                            }
                        }
                        for(String s : add){
                            redisUtil.rpush(RedisKeyAndLock.getListKey(), s);
                        }
                        stop = true;

                    } finally {
                        lock.unlock();
                    }
                }
            }
            return remove;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据key值，清除对应的redis中的bean。
     * @param key
     */
    public void cleanRedisBean(String key){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_BEAN_KEY + key, RedisKeyAndLock.LOCK_EXPIRE);
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {

                        redisUtil.hdel(RedisKeyAndLock.GROUP_WAITING_BEAN + key);
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除redis中指定id队伍的等待成员
     * @param groupId
     */
    public void cleanRedisMember(Long groupId){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_MEMBER_KEY + groupId, RedisKeyAndLock.LOCK_EXPIRE);
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        redisUtil.hdel(RedisKeyAndLock.getMemberKey(groupId));
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除redis中指定id队伍的等待成员
     * @param groupId
     */
    public void cleanRedisModify(Long groupId){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_MODIFY_KEY + groupId, RedisKeyAndLock.LOCK_EXPIRE);
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        redisUtil.del(RedisKeyAndLock.getModifyKey(groupId));
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加对应的value到list缓存中，带锁
     * @param key
     * @param strs
     */
    public void rpushWithLock(String key ,String...strs){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_LIST_KEY, RedisKeyAndLock.LOCK_EXPIRE);
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        redisUtil.rpush(key, strs);
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWithLock(String key, String lk){

        Lock lock = null;
        String value = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, lk, RedisKeyAndLock.LOCK_EXPIRE);
            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        value = redisUtil.get(key);
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断Redis的group_waiting_bean中是否存在key
     * @param group
     * @param key
     * @return
     */
    private boolean checkIfInWaitingBean(Group group, String key){
        Lock lock = null;
        try {
            boolean stop = false;
            lock = new RedisBasedDistributedLock(redisUtil, RedisKeyAndLock.LOCK_BEAN_KEY + group.getId() + "."  + group.getModifyTime().getTime(),
                    RedisKeyAndLock.LOCK_EXPIRE);

            while (!stop){
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    try {
                        Map<Object, Object> hmget = redisUtil.hmget(key);
                        if (null != hmget && hmget.size() > 0){
                            return true;
                        }
                        stop = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 取redis服务器系统时间，如果取redis时间出错，默认时间为当前系统时间
     * @return
     */
    public String getSysTime(){
        try{

            RedisTemplate<Serializable, Serializable> redisTemplate = redisUtil.getRedisTemplate();

            return redisTemplate.execute(new RedisCallback<String>() {

                @Override
                @Transactional
                public String doInRedis(RedisConnection connection) throws DataAccessException {

                    return connection.time().toString();
                }
            });
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return String.valueOf(System.currentTimeMillis());
    }


}
