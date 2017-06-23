package com.geetion.puputuan.utils;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodikai on 2016/11/8.
 */
public class SendGroupCreateMsgThread implements Runnable {

    private final Logger logger = Logger.getLogger(SendGroupCreateMsgThread.class);

    Group group = null;
    User leader = null;
    Bar bar = null;
    List<User> userList = null;
    List<User> blackList = null;
    public SendGroupCreateMsgThread(Group group, User leader, Bar bar, List<User> userList, List<User> blackList) {
        this.group = group;
        this.leader = leader;
        this.bar = bar;
        this.userList = userList;
        this.blackList = blackList;
    }


    @Override
    public void run() {

        if (userList.size() == 0) {
            logger.info("SendGroupCreateMsgThread get no user to send message.");
            return;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //要加入的群聊的成员
        List<String> memberList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer bl = new StringBuffer();
        for (User user : userList) {
            //获得每一个用户的账户作为环信的用户名
            memberList.add(user.getUserBase().getAccount() + "");
            sb.append(EmojiCharacterUtil.emojiRecovery2(user.getNickName()) + "、");
        }

        for (User user : blackList) {
            bl.append(EmojiCharacterUtil.emojiRecovery2(user.getNickName()) + "、");
        }

        HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
        huanXinMessageExtras.setAccount(leader.getUserBase().getAccount());
        huanXinMessageExtras.setHeadId(leader.getHeadId());
        huanXinMessageExtras.setUserId(leader.getUserId());
        huanXinMessageExtras.setNickName(leader.getNickName());
        huanXinMessageExtras.setGroupId(group.getId());
        huanXinMessageExtras.setRoomId(group.getRoomId());
        try {

            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                    leader.getNickName() + " 邀请您加入群", HuanXinSendMessageType.INVITE, huanXinMessageExtras, memberList.toArray(new String[memberList.size()]));

            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                    "约会：" + bar.getName() + " (" + group.getArea() + ")\n", HuanXinSendMessageType.ADD_OR_QUIT,
                    null, group.getRoomId());

            //丛林Mad House活动需要添加新消息指导用户找到购票入口
//            if (bar.getActionType() == BarType.ACTION_TYPE_YOUZAN_IN_GROUP) {
//                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
//                        "您可以在下方工具栏点击+号进入商城购买 MAD HOUSE 活动门票\n", HuanXinSendMessageType.ADD_OR_QUIT,
//                        null, group.getRoomId());
//            }

            if(sb.length() > 0){
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        leader.getNickName() + "  邀请  " + sb.substring(0, sb.length() - 1) + "  加入队伍\n", HuanXinSendMessageType.ADD_OR_QUIT,
                        null, group.getRoomId());
            }

            if (bl.length() > 0){
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        "  由于  " + bl.substring(0, bl.length() - 1) + " 已被管理员拉入黑名单，无法加入队伍 \n", HuanXinSendMessageType.ADD_OR_QUIT,
                        null, group.getRoomId());
            }


        } catch (HuanXinMessageException e) {
            e.printStackTrace();
        }



    }
}