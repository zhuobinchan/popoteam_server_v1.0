package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class HuanXinSendMessageType {

    /**
     * 环信发送消息的type
     */


    /**
     * 邀请
     */
    public static final int INVITE = 0;
    /**
     * 修改信息
     */
    public static final int UPDATE = 2;
    /**
     * 进群或退群或队长移除群聊
     */
    public static final int ADD_OR_QUIT = 3;
    /**
     * 退出约会
     */
    public static final int QUIT_ACTIVITY = 4;
    /**
     * 队长转让
     */
    public static final int LEADER_TRANSFER = 5;
    /**
     * 开始匹配
     */
    public static final int START_MATCHING = 6;
    /**
     * 结束匹配
     */
    public static final int END_MATCHING = 7;

    /**
     * 匹配到队伍信息（带参  recommendId，heads（头像数组），userIds（用户id数组））
     */
    public static final int HAVE_MATCHING_RESULT = 8;
    /**
     * 将投票结果告诉给同组成员信息（带参）
     */
    public static final int VOTE_RESULT = 9;
    /**
     * 匹配成功信息（带参）
     */
    public static final int MATCHING_RESULT = 10;

    /**
     * 群解散（群成员少于2人）
     */
    public static final int GROUP_DISSOLUTION = 11;
    /**
     * 踢出群
     */
    public static final int GROPU_BE_REMOVE = 12;

    /**
     * 约会解散（群成员少于2人）
     */
    public static final int ACTIVITY_DISSOLUTION = 13;
    /**
     * 约会开始，系统自动发的消息类型
     */
    public static final int ACTIVITY_HELLO = 14;

    /**
     *  superlike
     */
    public static final int SUPER_LIKE = 15;

    /**
     *  superlike
     */
    public static final int BE_SUPER_LIKE = 16;

}
