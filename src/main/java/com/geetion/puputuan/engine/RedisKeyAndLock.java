package com.geetion.puputuan.engine;

import net.sf.ehcache.util.PropertyUtil;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * redis对应的键值，锁
 * Created by guodikai on 2017/1/9.
 */
public class RedisKeyAndLock {
    private static final Logger logger = Logger.getLogger(RedisKeyAndLock.class);

    public static String BASE_USER_IDENTIFY;

    public static String BASE_INTEREST_IDENTIFY;

    public static String BASE_JOB_IDENTIFY;

    public static String BASE_FANCY_IDENTIFY;

    public static String BASE_MATCH_OPEN;

    // 缓存队伍队列的锁
    public static String LOCK_LIST_KEY ;
    // 缓存队伍信息的锁
    // eg. waiting.bean.lock.groupId.modifyTime
    public static String LOCK_BEAN_KEY ;
    // 缓存等待中队员的锁
    // eg. waiting.member.lock.groupId
    public static String LOCK_MEMBER_KEY ;
    // 缓存队伍最新修改时间的锁
    // eg. waiting.modify.lock.groupId
    public static String LOCK_MODIFY_KEY;

    // 缓存队伍队列，Key
    // key:groupWaiting:list
    // value: groupId.modifyTime ,....
    // list类型
    public static String GROUP_WAITING_LIST;
    // 缓存队伍信息bean，Key
    // key: groupWaiting:bean:groupId.modifyTime
    // hash类型
    public static String GROUP_WAITING_BEAN ;
    // 缓存等待中队员，Key
    // key: groupWaiting:member:gropuId
    // set类型，去重
    public static String GROUP_WAITING_MEMBER;
    // 缓存队伍最新修改时间，Key
    // key: groupWaiting:modify:groupId
    // string类型
    public static String GROUP_WAITING_MODIFY ;

    public static String SMS_REPEAT_SEND;

    // 锁失效时间
    public static final long LOCK_EXPIRE = 5 * 1000;

    static {
         Properties props;
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("ppt_envi.properties");
            //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            props.load(in);
            BASE_USER_IDENTIFY = props.getProperty("base.user.identify");
            BASE_INTEREST_IDENTIFY = props.getProperty("base.interest.identify");
            BASE_JOB_IDENTIFY = props.getProperty("base.job.identify");
            BASE_FANCY_IDENTIFY = props.getProperty("base.fancy.identify");

            BASE_MATCH_OPEN = props.getProperty("base.match.open");

            LOCK_LIST_KEY = props.getProperty("waiting.list.lock");
            LOCK_BEAN_KEY = props.getProperty("waiting.bean.lock");
            LOCK_MEMBER_KEY = props.getProperty("waiting.member.lock");
            LOCK_MODIFY_KEY = props.getProperty("waiting.modify.lock");

            GROUP_WAITING_LIST = props.getProperty("groupWaiting.list");
            GROUP_WAITING_BEAN = props.getProperty("groupWaiting.bean");
            GROUP_WAITING_MEMBER = props.getProperty("groupWaiting.member");
            GROUP_WAITING_MODIFY = props.getProperty("groupWaiting.modify");

            SMS_REPEAT_SEND = props.getProperty("sms.repeat.send");

        } catch (FileNotFoundException e) {
            logger.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("jdbc.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    /**
     * 取得redis中队列的key
     * @return
     */
    public static String getListKey(){
        return GROUP_WAITING_LIST;
    }

    /**
     * 根据参数返回缓存队伍信息的key
     * @param groupId
     * @param time
     * @return
     */
    public static String getBeanKey(Long groupId, Long time){
        return RedisKeyAndLock.GROUP_WAITING_BEAN + groupId + "."  + time;
    }

    /**
     * 根据参数返回等待中队员的key
     * @param groupId
     * @return
     */
    public static String getMemberKey(Long groupId){
        return RedisKeyAndLock.GROUP_WAITING_MEMBER + groupId;
    }

    /**
     * 根据参数返回队伍修改时间的key
     * @param groupId
     * @return
     */
    public static String getModifyKey(Long groupId){
        return RedisKeyAndLock.GROUP_WAITING_MODIFY + groupId;
    }
}
