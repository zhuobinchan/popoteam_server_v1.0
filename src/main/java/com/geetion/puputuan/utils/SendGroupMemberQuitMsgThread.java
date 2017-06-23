package com.geetion.puputuan.utils;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.BarType;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.ActivityService;
import com.geetion.puputuan.service.BarService;
import com.geetion.puputuan.service.CommonService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;
import org.apache.log4j.Logger;

/**
 * Created by guodikai on 2016/11/8.
 */
public class SendGroupMemberQuitMsgThread implements Runnable {

    Group group = null;
//    User user = null;
    String nickName = null;
    public SendGroupMemberQuitMsgThread(Group group, String nickName) {
        this.group = group;
        this.nickName = nickName;
    }


    @Override
    public void run() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 通知其他群聊中其他成员
        HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
        huanXinMessageExtras.setRoomId(group.getRoomId());
        try {
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                    nickName + " 离开队伍\n",
                    HuanXinSendMessageType.ADD_OR_QUIT, huanXinMessageExtras, group.getRoomId());
        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }

    }
}