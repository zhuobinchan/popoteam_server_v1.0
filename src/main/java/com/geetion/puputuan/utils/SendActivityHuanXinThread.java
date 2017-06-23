package com.geetion.puputuan.utils;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.BarType;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.ActivityService;
import com.geetion.puputuan.service.BarService;
import com.geetion.puputuan.service.CommonService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import org.apache.log4j.Logger;

/**
 * Created by guodikai on 2016/11/8.
 */
public class SendActivityHuanXinThread implements Runnable {

    Activity activity = null;
    String roomId = "";
    int isSuperLike = 0;
    Group mainGroup = null;
    Group matchGroup = null;
    String[] alias ;
    private CommonService commonService = SpringLoader.getInstance().getBean(CommonService.class);
    private ActivityService activityService = SpringLoader.getInstance().getBean(ActivityService.class);
    private BarService barService = SpringLoader.getInstance().getBean(BarService.class);
    private final Logger logger = Logger.getLogger(SendActivityHuanXinThread.class);

    public SendActivityHuanXinThread(Activity activity,String[] alias, String roomId, int isSuperLike, Group mainGroup, Group matchGroup) {
        this.activity = activity;
        this.alias = alias;
        this.roomId = roomId;
        this.isSuperLike = isSuperLike;
        this.mainGroup = mainGroup;
        this.matchGroup = matchGroup;

    }


    @Override
    public void run() {
        sendActivityHuanXin();
//        for(int i = 0; i < 10; i++ ){
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Activity activityById = activityService.getActivityById(activity.getId());
//            if(null != activityById){
//                sendActivityHuanXin();
//                return;
//            }
//        }

    }

    private void sendActivityHuanXin(){
        try {
            //发送环信消息 将约会推送给两个队伍
            HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
            huanXinMessageExtras.setActivityId(activity.getId());
            huanXinMessageExtras.setRoomId(roomId);
            if (isSuperLike == 1) {
                // 给队员推送消息
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        "恭喜您成功SuperLike其他队伍", HuanXinSendMessageType.SUPER_LIKE, huanXinMessageExtras,
                        commonService.getGroupMemberAccountOrToken(mainGroup.getId(), true, CommonService.USER_ACCOUNT));

                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        "您的队伍被其他队伍SuperLike", HuanXinSendMessageType.BE_SUPER_LIKE, huanXinMessageExtras,
                        commonService.getGroupMemberAccountOrToken(matchGroup.getId(), true, CommonService.USER_ACCOUNT));
            } else {
                // 给队员推送消息
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        "您的队伍已经成功匹配到其他队伍", HuanXinSendMessageType.MATCHING_RESULT, huanXinMessageExtras, alias);
            }

            if (mainGroup.getBarId() == BarType.ID_MAD_HOUSE || matchGroup.getBarId() == BarType.ID_MAD_HOUSE) {
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        "约会：MAD HOUSE\n\n" + "活动结束后约会将自动解散\n", HuanXinSendMessageType.ACTIVITY_HELLO, huanXinMessageExtras, roomId);
            } else {
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        "约会：" + getActivityMsg(mainGroup, matchGroup) + "\n\n" + "24小时后约会将自动解散\n", HuanXinSendMessageType.ACTIVITY_HELLO, huanXinMessageExtras, roomId);
            }

        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }
    }
    private String getActivityMsg(Group mainGroup, Group matchGroup){
        StringBuffer sb = new StringBuffer();
        Bar bar = barService.getBarById(mainGroup.getBarId());

        //bar id 600开头表示是特殊活动

        if (mainGroup.getBarId().equals(matchGroup.getBarId())){
            if (null != bar && bar.getStatus() ==1){
                //取数据库配置的约会提示消息
                return bar.getName();
            }else{
                //如果未配置，则默认提示消息
                return "我们去溜达！";
            }
        }else {
            sb.append("我们去溜达！");
        }

        return sb.toString();
    }
}