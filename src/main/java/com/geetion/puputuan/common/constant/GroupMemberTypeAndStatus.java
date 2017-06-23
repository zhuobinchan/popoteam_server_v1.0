package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class GroupMemberTypeAndStatus {

    /**
     * 队友群的type的定义
     */

    //队长
    public static int GROUP_MEMBER_TYPE_LEADER = 0;
    //队员
    public static int GROUP_MEMBER_TYPE_PLAYER = 1;

    /**
     * 队友群的status的定义
     */

    // 邀请
    public static int GROUP_MEMBER_STATUS_INVITE = 0;
    // 已进入
    public static int GROUP_MEMBER_STATUS_ACCESS = 1;
    // 已退出
    public static int GROUP_MEMBER_STATUS_QUIT = 2;
    // 被踢出群
    public static int GROUP_MEMBER_STATUS_REMOVE = 3;
    // 投票中
    public static int GROUP_MEMBER_STATUS_VOTE = 4;
}
