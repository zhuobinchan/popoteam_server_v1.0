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
public class MatchJobEventHandler implements EventHandler<Recommend> {
    private JobUserService jobUserService;
    private Logger logger = Logger.getLogger(MatchJobEventHandler.class);

    @Override
    public void onEvent(Recommend recommend, long l, boolean b) throws Exception {
        //TODO 找出main团队的所有工作
        jobUserService = SpringLoader.getInstance().getBean(JobUserService.class);
        List<Long> matchIds = new ArrayList<>();
        for (User user : recommend.getMainUser()) {
            matchIds.add(user.getUserId());
        }
        List<Long> matchJobs = jobUserService.getJobIdsByUserIds(matchIds);
        recommend.matchJob.addAll(matchJobs);
//        logger.info("找出Match团队里面所有的工作标签 " + matchJobs.size());
    }
}
