package com.task;

import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.service.MessageHisService;
import com.geetion.puputuan.service.MessageService;
import com.geetion.puputuan.service.NoticeHisService;
import com.geetion.puputuan.service.NoticeService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 后台消息定时任务--每两周执行一次
 * Created by eric on 2016/9/2.
 */
@Component
public class MsgHisJob {
    @Resource
    private MessageService messageService;
    @Resource
    private MessageHisService messageHisService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private NoticeHisService noticeHisService;

    private Date date = new Date();
    private Timestamp begin;


    private final  static Logger logger = Logger.getLogger(MsgHisJob.class);

    /**
     *将用户消息转移到历史表
     */

    public void executeMsgJob(){
        logger.info("executeMsgJob running...");
        this.init();

        logger.info("executeMsgJob get message");
        //取得消息某个时间段的消息
        List<Message> messages = this.messageService.selectMsgByDate(begin);
        if(messages.size() > 0)
        {
            logger.info("executeMsgJob has messages");

            logger.info("executeMsgJob insert message_his");
            //将消息移到历史表
            messageHisService.insertBatch(messages);

            logger.info("executeMsgJob delete message");
            this.delMsg(messages);
        }
    }


    /**
     * 将公告信息转移到历史表
     */

    public void executeNoticeJob(){
        logger.info("executeNoticeJob running...");
        this.init();

        logger.info("executeMsgJob get notice");
        //取得消息某个时间段的消息
        List<Notice> notices = this.noticeService.selectNoticeByDate(begin);
        if(notices.size() > 0)
        {
            logger.info("executeNoticeJob has notice");

            logger.info("executeNoticeJob insert notice_his");
            //将消息移到历史表
            noticeHisService.insertBatch(notices);

            logger.info("executeNoticeJob delete notice");
            this.delNotice(notices);
        }
    }


    /**
     * 删除消息
     * @param notices
     */
    public void delNotice(List<Notice> notices){

        List<Long> list = new ArrayList<>();
        for(Notice n : notices){
            list.add(n.getId());
        }
        noticeService.delMulNotice(list);
    }

    /**
     * 删除消息
     * @param messages
     */
    public void delMsg(List<Message> messages){

        List<Long> list = new ArrayList<>();
        for(Message m : messages){
            list.add(m.getId());
        }
        messageService.delMulMsg(list);
    }



    /**
     * 初始化时间段，取两周时间的开始跟结束时间
     */
    private void init(){
        logger.info("executeMsgJob init...");
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //取两周前的时间
        Date beginDate = this.getDate(14);
        this.begin = Timestamp.valueOf(format.format(beginDate));
        logger.info("beginTime: " + this.begin);
    }

    /**
     * 取得指定*天前的时间
     * @param num
     * @return
     */
    private Date getDate(int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -num);
        return calendar.getTime();
    }



}
