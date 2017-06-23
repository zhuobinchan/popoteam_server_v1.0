package com.geetion.puputuan.engine.thread;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by guodikai on 2016/10/19.
 */
public class ShareCacheVar {

    // 记录等待进入线程池的匹配队伍 <HashMap<String, Long>>
    public static ConcurrentLinkedQueue groupWaitingQueue = new ConcurrentLinkedQueue();
    // 记录匹配队伍为空，需要重新匹配的队伍
    public static ConcurrentLinkedQueue groupNoMatchWaitingQueue = new ConcurrentLinkedQueue();
    // 当某个队伍已经在匹配中，队伍成员触发匹配时，记录队伍成员 <Long groupId, List<Long> memberList>
    public static ConcurrentHashMap memberWaitingMap = new ConcurrentHashMap();
    // 用于记录队伍修改时间 <Long groupId, Date modifyDate>
    public static ConcurrentHashMap groupModifyMap = new ConcurrentHashMap();

    // 用于控制是否使用新的推荐机制
    public static boolean ifUseNew = true;

    private final Logger logger = Logger.getLogger(ShareCacheVar.class);

}
