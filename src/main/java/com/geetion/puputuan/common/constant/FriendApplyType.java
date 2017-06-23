package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class FriendApplyType {

    /**
     * 好友申请的type的定义 pu_friend_apply 表
     */

    /**
     * 发送中
     */
    public static final int SENDING = 0;
    /**
     * 同意
     */
    public static final int AGREE = 1;
    /**
     * 拒绝
     */
    public static final int REJECT = 2;


    /**
     * 添加好友成功
     */
    public static final int APPLY_SUCCESS = 0;

    /**
     * 添加好友申请
     */
    public static final int FRIEND_APPLY = 1;

    /**
     * 重复添加好友
     */
    public static final int DUPLICATE_APPLY = 2;

    /**
     * 处理异常
     */
    public static final int EXCEPTION = 3;
}
