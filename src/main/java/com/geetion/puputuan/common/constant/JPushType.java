package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class JPushType {

    /**
     * JPush推送消息的type的定义
     */

    /**
     * 公告，系统通知
     */
    public static final int ANNOUNCEMENT = 0;
    /**
     * 好友申请（带好友id）
     */
    public static final int FRIEND_APPLY = 1;
    /**
     * 好友申请结果
     */
    public static final int FRIEND_APPLY_RESULT = 2;
    /**
     * 邀请xx进群消息（带群组id）
     */
    public static final int INVITE_GROUP = 3;
    /**
     * 群相关消息（xx解散群消息）
     */
    public static final int GROUP_RELATED = 4;
    /**
     * 开始匹配
     */
    public static final int START_MATCHING = 5;
    /**
     * 结束匹配
     */
    public static final int END_MATCHING = 6;

    /**
     * 匹配到队伍信息（带参  recommendId，heads（头像数组），userIds（用户id数组））
     */
    public static final int HAVE_MATCHING_RESULT = 7;
    /**
     * 投票结果信息（带参）
     */
    public static final int VOTE_RESULT = 8;
    /**
     * 匹配成功信息（带参）
     */
    public static final int MATCHING_RESULT = 9;
    /**
     * 约会通知信息（xx退群消息，xx解散群消息，xx修改群信息，约会完成消息）
     */
    public static final int ACTIVITY_RELATED = 10;
    /**
     * 约会完成之后的评价信息
     */
    public static final int ACTIVITY_EVALUATE = 11;
    /**
     * 互相评价成功之后的成为好友信息
     */
    public static final int ACTIVITY_EVALUATE_FRIEND = 12;
    /**
     * 群解散（群成员少于2人）
     */
    public static final int GROUP_DISSOLUTION = 13;
    /**
     * 约会通知信息（约会完成消息）
     */
    public static final int ACTIVITY_FINISH = 14;
    /**
     * 系统警告信息
     */
    public static final int WARN = 15;
    /**
     * 解除好友关系（带好友identify）
     */
    public static final int FRIEND_DELETE = 16;
}
