package com.geetion.puputuan.engine;

import com.geetion.puputuan.engine.thread.NewMatchThread;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;
import java.util.Map;

/**
 * Created by guodikai on 2016/10/19.
 */
public class ScanWaitingMatchGroup {
    private final Logger logger = Logger.getLogger(ScanWaitingMatchGroup.class);

    // 扫描等待进行执行的进程
    public void scanWaitingList(){
        logger.info("==================ScanWaitingMatchGroup scanWaitingList begin");
        logger.info("==================ScanWaitingMatchGroup ShareCacheVar.groupWaitingQueue" + ShareCacheVar.groupWaitingQueue);
        ThreadPoolTaskExecutor poolTaskExecutor = SpringContextUtil.getBean("matchThreadPool");
        int activeCount = poolTaskExecutor.getActiveCount();
        int usableCount = poolTaskExecutor.getCorePoolSize() - activeCount;
        for (int i = 0; i < usableCount; i++){
            Map<String, Object> group = (Map<String, Object>) ShareCacheVar.groupWaitingQueue.poll();
            if(null == group){
                return;
            }
            if(group.get("isValid").equals(1L)){
                poolTaskExecutor.execute(new NewMatchThread((Long)group.get("groupId"), (Long)group.get("userId"), (Long)group.get("isReMatch"), (Date)group.get("time")));
                logger.info("======================ScanWaitingMatchGroup poolTaskExecutor execute "  + group.get("groupId"));
            }

        }
    }

    // 扫描等待进行执行的进程，主要是匹配队伍为空，才会进入该队列
    public void scanNoMatchGroupWaitingList(){
        logger.info("===================ScanWaitingMatchGroup scanNoMatchGroupWaitingList begin");
        logger.info("===================ScanWaitingMatchGroup scanNoMatchGroupWaitingList" + ShareCacheVar.groupNoMatchWaitingQueue);
        ThreadPoolTaskExecutor poolTaskExecutor = SpringContextUtil.getBean("matchThreadPool");
        int activeCount = poolTaskExecutor.getActiveCount();
        int usableCount = poolTaskExecutor.getCorePoolSize() - activeCount;
        for (int i = 0; i < usableCount; i++){
            Map<String, Object> group = (Map<String, Object>) ShareCacheVar.groupNoMatchWaitingQueue.poll();
            if(null == group){
                return;
            }
            poolTaskExecutor.execute(new NewMatchThread((Long)group.get("groupId"), (Long)group.get("userId"), (Long)group.get("isReMatch"), (Date)group.get("time")));
            logger.info("===============scanNoMatchGroupWaitingList poolTaskExecutor execute " + group.get("groupId"));
        }
    }
}
