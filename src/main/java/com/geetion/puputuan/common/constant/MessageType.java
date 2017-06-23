package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class MessageType {

    /**
     * 消息的type的定义
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
     * 警告信息
     */
    public static final int BE_WARN = 3;


    /**
     * 消息的状态，是否可见 pu_message 表的 status 的定义
     */
    /**
     * 对用户可见
     */
    public static final int CAN_READ = 0;
    /**
     * 对用户不可见
     */
    public static final int CANNOT_READ = 1;


}
