package com.geetion.puputuan.redis;

import com.geetion.puputuan.utils.RedisUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by guodikai on 2017/1/9.
 */
public class RedisBasedDistributedLock extends AbstractLock {

    private final Logger logger = Logger.getLogger(RedisBasedDistributedLock.class);
    // redis操作工具
    private RedisUtil redisUtil;

    // 锁的名字
    protected String lockKey;

    // 锁的有效时长(毫秒)
    protected long lockExpires;

    public RedisBasedDistributedLock(RedisUtil redisUtil, String lockKey, long lockExpires) throws IOException {
        this.redisUtil = redisUtil;
        this.lockKey = lockKey;
        this.lockExpires = lockExpires;
    }

    // 阻塞式获取锁的实现
    protected boolean lock(boolean useTimeout, long time, TimeUnit unit, boolean interrupt) throws InterruptedException{
        logger.debug("=============== RedisBasedDistributedLock lock =======================");
        if (interrupt) {
            checkInterruption();
        }

        // 超时控制 的时间可以从本地获取, 因为这个和锁超时没有关系, 只是一段时间区间的控制
        long start = localTimeMillis();
        long timeout = unit.toMillis(time); // if !useTimeout, then it's useless

        while (useTimeout ? isTimeout(start, timeout) : true) {
            if (interrupt) {
                checkInterruption();
            }

            long lockExpireTime = serverTimeMillis() + lockExpires + 1;//锁超时时间
            String stringOfLockExpireTime = String.valueOf(lockExpireTime);

            if (redisUtil.setnx(lockKey, stringOfLockExpireTime)) { // 获取到锁
                logger.debug("=============== RedisBasedDistributedLock lock setnx true, get the lock =======================");
                // TODO 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            String value = redisUtil.get(lockKey);
            if (value != null && isTimeExpired(value)) { // lock is expired
                // 假设多个线程(非单jvm)同时走到这里
                String oldValue = redisUtil.getset(lockKey, stringOfLockExpireTime); // getset is atomic
                logger.debug("=============== RedisBasedDistributedLock lock old value " + oldValue +" =======================");
                // 但是走到这里时每个线程拿到的oldValue肯定不可能一样(因为getset是原子性的)
                // 加入拿到的oldValue依然是expired的，那么就说明拿到锁了
                if (oldValue != null && isTimeExpired(oldValue)) {
                    // TODO 成功获取到锁, 设置相关标识
                    logger.debug("=============== RedisBasedDistributedLock lock setnx true, get the lock =======================");
                    locked = true;
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else {
                // TODO lock is not expired, enter next loop retrying

                logger.debug("=============== RedisBasedDistributedLock lock , don't get the lock =======================");
            }
        }
        return false;
    }

    public boolean tryLock() {
        logger.debug("=============== RedisBasedDistributedLock tryLock =======================");
        long lockExpireTime = serverTimeMillis() + lockExpires + 1;//锁超时时间
        String stringOfLockExpireTime = String.valueOf(lockExpireTime);

        if (redisUtil.setnx(lockKey, stringOfLockExpireTime)) { // 获取到锁
            // TODO 成功获取到锁, 设置相关标识
            logger.debug("=============== RedisBasedDistributedLock tryLock, get the lock =======================");
            locked = true;
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }

        String value = redisUtil.get(lockKey);
        if (value != null && isTimeExpired(value)) { // lock is expired
            // 假设多个线程(非单jvm)同时走到这里
            String oldValue = redisUtil.getset(lockKey, stringOfLockExpireTime); // getset is atomic
            logger.debug("=============== RedisBasedDistributedLock tryLock, the old value " + oldValue + " =======================");
            // 但是走到这里时每个线程拿到的oldValue肯定不可能一样(因为getset是原子性的)
            // 假如拿到的oldValue依然是expired的，那么就说明拿到锁了
            if (oldValue != null && isTimeExpired(oldValue)) {
                // TODO 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
        } else {
            // TODO lock is not expired, enter next loop retrying
            logger.debug("=============== RedisBasedDistributedLock tryLock , don't get the lock =======================");
        }

        return false;
    }

    /**
     * Queries if this lock is held by any thread.
     *
     * @return {@code true} if any thread holds this lock and
     *         {@code false} otherwise
     */
    public boolean isLocked() {
        logger.debug("=============== RedisBasedDistributedLock isLocked =======================");
        if (locked) {
            logger.debug("=============== RedisBasedDistributedLock isLocked true =======================");
            return true;
        } else {
            String value = redisUtil.get(lockKey);
            logger.debug("=============== RedisBasedDistributedLock isLocked false, lockKey is " + value + " =======================");
            // TODO 这里其实是有问题的, 想:当get方法返回value后, 假设这个value已经是过期的了,
            // 而就在这瞬间, 另一个节点set了value, 这时锁是被别的线程(节点持有), 而接下来的判断
            // 是检测不出这种情况的.不过这个问题应该不会导致其它的问题出现, 因为这个方法的目的本来就
            // 不是同步控制, 它只是一种锁状态的报告.
            return !isTimeExpired(value);
        }
    }

    @Override
    protected void unlock0() {
        logger.debug("=============== RedisBasedDistributedLock unlock0 =======================");
        // TODO 判断锁是否过期
        String value = redisUtil.get(lockKey);
        if (!isTimeExpired(value)) {
            doUnlock();
        }
    }


    public void release() {
        logger.debug("=============== RedisBasedDistributedLock release =======================");
        doUnlock();
    }

    private void doUnlock() {
        logger.debug("=============== RedisBasedDistributedLock doUnlock =======================");

        redisUtil.del(lockKey);
    }

    private void checkInterruption() throws InterruptedException {
        logger.debug("=============== RedisBasedDistributedLock checkInterruption =======================");

        if(Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private boolean isTimeExpired(String value) {
        logger.debug("=============== RedisBasedDistributedLock isTimeExpired =======================");
        // 这里拿服务器的时间来比较
        return Long.parseLong(value) < serverTimeMillis();
    }

    private long serverTimeMillis(){
        logger.debug("=============== RedisBasedDistributedLock serverTimeMillis =======================");
        return  System.currentTimeMillis();
    }

    private boolean isTimeout(long start, long timeout) {
        logger.debug("=============== RedisBasedDistributedLock isTimeout =======================");

        // 这里拿本地的时间来比较
        return start + timeout > System.currentTimeMillis();
    }

    private long localTimeMillis() {
        return System.currentTimeMillis();
    }
}
