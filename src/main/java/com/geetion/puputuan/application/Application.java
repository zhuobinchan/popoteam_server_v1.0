package com.geetion.puputuan.application;


import com.geetion.generic.serverfile.pojo.OssAccessToken;
import com.geetion.puputuan.common.constant.DefaultSettingValue;
import com.geetion.puputuan.engine.RedisKeyAndLock;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.service.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mac on 15/2/28.
 */
@Component("puputuanApplication")
public class Application {

    private OssAccessToken ossAccessToken;

    @Resource
    private UserService userService;
    @Resource
    private InterestService interestService;
    @Resource
    private JobService jobService;
    @Resource
    private  FancyService fancyService;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private AppSettingService appSettingService;
    @Resource
    private MatchWeightService matchWeightService;

    @Resource(name = "redisTemplate")
    protected RedisTemplate<Serializable, Serializable> redisTemplate;
    //用户的identity
    private Long BASE_USER_IDENTIFY = null;
    //兴趣的identity
    private Long BASE_INTERSET_IDENTIFY = null;
    //职业的identity
    private Long BASE_JOB_IDENTIFY = null;
    //喜爱夜蒲标签的identity
    private Long BASE_FANCY_IDENTIFY = null;


    //后台消息发送处理线程池
    public static ExecutorService messageThreadPool = Executors.newFixedThreadPool(100);

    public OssAccessToken getOssAccessToken() {
        return ossAccessToken;
    }

    public void setOssAccessToken(OssAccessToken ossAccessToken) {
        this.ossAccessToken = ossAccessToken;
    }


    /**
     * 初始化APP设置和匹配权重等
     */
    public void initAppSetting() {

        AppSetting appSetting = appSettingService.getAppSetting(null);
        if (appSetting == null) {
            appSetting = new AppSetting();
            appSetting.setAdvertisementPages(DefaultSettingValue.advertisementPages);
            appSetting.setCarouselInterval(DefaultSettingValue.carouselInterval);
            appSetting.setActivityNewInterval(DefaultSettingValue.activityNewInterval);
            appSetting.setActivityPositionInterval(DefaultSettingValue.activityPositionInterval);
            appSetting.setActivityPositionRadius(DefaultSettingValue.activityPositionRadius);
            //添加APP设置的默认值
            boolean result = appSettingService.addAppSetting(appSetting);
            System.out.println("App设置不存在，已" + (result ? "添加成功" : "添加失败"));
        } else {
            System.out.println("App设置已存在");
        }

        MatchWeight matchWeight = matchWeightService.getMatchWeight(null);
        if (matchWeight == null) {
            matchWeight = new MatchWeight();
            matchWeight.setConstellation(DefaultSettingValue.constellation);
            matchWeight.setDataSign(DefaultSettingValue.dataSign);
            matchWeight.setInterestionJob(DefaultSettingValue.interestionJob);
            matchWeight.setMutualLike(DefaultSettingValue.mutualLike);
            matchWeight.setRelationship(DefaultSettingValue.relationship);
            matchWeight.setVoteResult(DefaultSettingValue.voteResult);
            //添加APP设置的默认值
            boolean result = matchWeightService.addMatchWeight(matchWeight);
            System.out.println("匹配权重不存在，已" + (result ? "添加成功" : "添加失败"));
        } else {
            System.out.println("匹配权重已存在");
        }

    }

    public Long getBASE_USER_IDENTIFY() {
        // 加锁，处理并发问题
//        synchronized (BASE_USER_IDENTIFY){
//            if (BASE_USER_IDENTIFY == null) {
//                setBASE_USER_IDENTIFY();
//            } else {
//                BASE_USER_IDENTIFY++;
//            }
//            return BASE_USER_IDENTIFY;
//        }

        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] key = redisTemplate.getStringSerializer().serialize(
                        RedisKeyAndLock.BASE_USER_IDENTIFY);
                Long identify;
                if (connection.exists(key)) {

                    byte[] value = connection.get(key);
                    identify = Long.valueOf(redisTemplate.getStringSerializer()
                            .deserialize(value));
                    connection.incr(key);
                    identify ++;
                }else {

                    User lastUser = userService.getUser(null);
                    if (lastUser != null) {
                        identify = Long.valueOf(lastUser.getIdentify()) + 1;
                    } else {
                        identify = 600000L;
                    }
                    connection.setNX(key, redisTemplate.getStringSerializer().serialize(String.valueOf(identify)));
                }
                return identify;
            }
        });

    }

    public void setBASE_USER_IDENTIFY() {
        //查询数据库最后一个identity，有则加1，无则使用默认的 100000L
        User lastUser = userService.getUser(null);
        Long identify ;
        if (lastUser != null) {
//            BASE_USER_IDENTIFY = Long.valueOf(lastUser.getIdentify()) + 1;
            identify = Long.valueOf(lastUser.getIdentify()) + 1;
        } else {
//            BASE_USER_IDENTIFY = 600000L;
            identify = 600000L;
        }


    }

    public Long getBASE_INTERSET_IDENTIFY() {
//        if (BASE_INTERSET_IDENTIFY == null) {
//            setBASE_INTERSET_IDENTIFY();
//        } else {
//            BASE_INTERSET_IDENTIFY++;
//        }
//        return BASE_INTERSET_IDENTIFY;

        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize(
                        RedisKeyAndLock.BASE_INTEREST_IDENTIFY);
                Long identify;
                if (connection.exists(key)) {

                    byte[] value = connection.get(key);
                    identify = Long.valueOf(redisTemplate.getStringSerializer()
                            .deserialize(value));
                    connection.incr(key);
                    identify ++;
                }else {

                    Interest lastInterest = interestService.getInterest(null);
                    if (lastInterest != null) {
                        identify = Long.valueOf(lastInterest.getIdentify()) + 1;
                    } else {
                        identify = 1L;
                    }
                    connection.setNX(key, redisTemplate.getStringSerializer().serialize(String.valueOf(identify)));
                }
                return identify;
            }
        });
    }

    public void setBASE_INTERSET_IDENTIFY() {
        //查询数据库最后一个identity，有则加1，无则使用默认的 1L
        Interest lastInterest = interestService.getInterest(null);
        if (lastInterest != null) {
            BASE_INTERSET_IDENTIFY = lastInterest.getIdentify() + 1;
        } else {
            BASE_INTERSET_IDENTIFY = 1L;
        }
    }

    public Long getBASE_JOB_IDENTIFY() {
//        if (BASE_JOB_IDENTIFY == null) {
//            setBASE_JOB_IDENTIFY();
//        } else {
//            BASE_JOB_IDENTIFY++;
//        }
//        return BASE_JOB_IDENTIFY;
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] key = redisTemplate.getStringSerializer().serialize(
                        RedisKeyAndLock.BASE_JOB_IDENTIFY);
                Long identify;
                if (connection.exists(key)) {

                    byte[] value = connection.get(key);
                    identify = Long.valueOf(redisTemplate.getStringSerializer()
                            .deserialize(value));
                    connection.incr(key);
                    identify ++;
                }else {

                    Job lastJOb = jobService.getJob(null);
                    if (lastJOb != null) {
                        identify = Long.valueOf(lastJOb.getIdentify()) + 1;
                    } else {
                        identify = 1L;
                    }
                    connection.setNX(key, redisTemplate.getStringSerializer().serialize(String.valueOf(identify)));
                }
                return identify;
            }
        });
    }

    public void setBASE_JOB_IDENTIFY() {
        //查询数据库最后一个identity，有则加1，无则使用默认的 1L
        Job lastJOb = jobService.getJob(null);
        if (lastJOb != null) {
            BASE_JOB_IDENTIFY = lastJOb.getIdentify() + 1;
        } else {
            BASE_JOB_IDENTIFY = 1L;
        }
    }

    public Long getBASE_FANCY_IDENTIFY() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {

                byte[] key = redisTemplate.getStringSerializer().serialize(
                        RedisKeyAndLock.BASE_FANCY_IDENTIFY);
                Long identify;
                if (connection.exists(key)) {

                    byte[] value = connection.get(key);
                    identify = Long.valueOf(redisTemplate.getStringSerializer()
                            .deserialize(value));
                    connection.incr(key);
                    identify ++;
                }else {

                    Fancy lastFancy = fancyService.getFancy(null);
                    if (lastFancy != null) {
                        identify = Long.valueOf(lastFancy.getIdentify()) + 1;
                    } else {
                        identify = 1L;
                    }
                    connection.setNX(key, redisTemplate.getStringSerializer().serialize(String.valueOf(identify)));
                }
                return identify;
            }
        });
    }

    /**
     * 初始化环境配置
     */
    public void initEnviConf(){
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("ppt_envi.properties");
            prop.load(fis);
            System.out.println("\nThe sms.check.open property: " +
                    prop.getProperty("sms.check.open").trim());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMatcherOpen(){
        SysDic matcherOpenRule = sysDicService.getSysDicByKey("MATCHER_OPEN_RULE");
        String[] rules = matcherOpenRule.getValue().split(",");

        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize(RedisKeyAndLock.BASE_MATCH_OPEN);
                connection.del(key);
                for(int i = 0 ; i < rules.length; i++){
                    connection.rPush(key, redisTemplate.getStringSerializer().serialize(rules[i]));
                }
                return 0L;
            }
        });
    }
}
