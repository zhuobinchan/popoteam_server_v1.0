package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class GroupTypeAndStatus {

    /**
     * 团队群的type的定义
     */

    //男性群
    public static int GROUP_MALE = 0;
    //女性群
    public static int GROUP_FEMALE = 1;
    //全部
    public static int GROUP_ALL = 2;
    //混合群
    public static int GROUP_MIX = 3;
    /**
     * 团队群的status的定义
     */

    //创建中
    public static int GROUP_CREATE = 0;
    //匹配中
    public static int GROUP_MATCHING = 1;
    //组队中、约会中（匹配成功）
    public static int GROUP_TEAM = 2;
    //解散
    public static int GROUP_DISSOLUTION = 3;
    //结束
    public static int GROUP_FINISH = 4;


}
