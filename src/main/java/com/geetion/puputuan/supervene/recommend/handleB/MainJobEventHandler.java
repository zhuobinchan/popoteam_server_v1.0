package com.geetion.puputuan.supervene.recommend.handleB;

import com.geetion.puputuan.model.User;
import com.geetion.puputuan.service.JobUserService;
import com.geetion.puputuan.supervene.recommend.model.Recommend;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import com.lmax.disruptor.EventHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/3/31.
 */
public class MainJobEventHandler implements EventHandler<Recommend> {
    private JobUserService jobUserService;
    private Logger logger = Logger.getLogger(MainJobEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出main团队的所有工作
        jobUserService = SpringLoader.getInstance().getBean(JobUserService.class);
        List<Long> mainIds = new ArrayList<>();
        for (User user : recommend.getMainUser()) {
            mainIds.add(user.getUserId());
        }
        List<Long> mainJobs = jobUserService.getJobIdsByUserIds(mainIds);
        recommend.mainJob.addAll(mainJobs);
//        logger.info("找出Main团队里面所有的工作标签 " + mainJobs.size());
    }
}
