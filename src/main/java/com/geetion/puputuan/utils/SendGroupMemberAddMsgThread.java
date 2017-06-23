package com.geetion.puputuan.utils;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.BarType;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodikai on 2016/11/8.
 */
public class SendGroupMemberAddMsgThread implements Runnable {

    Group group = null;
    User leader = null;
    StringBuffer sb = null;
    public SendGroupMemberAddMsgThread(Group group, User leader, StringBuffer sb) {
        this.group = group;
        this.leader = leader;
        this.sb = sb;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // 给群成员发信息
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                    leader.getNickName() + " 邀请 " + sb.substring(0, sb.length() - 1)  + " 加入队伍\n", HuanXinSendMessageType.ADD_OR_QUIT,
                    null, group.getRoomId());

        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }



    }
}