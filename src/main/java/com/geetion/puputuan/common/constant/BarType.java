package com.geetion.puputuan.common.constant;

/**
 * Created by quanjianan on 2017/3/6.
 */
public class BarType {

    /**
     * 活动操作类型  pu_bar表 action_type 的定义
     */

    /**
     * 无特殊操作
     */
    public static final int ACTION_TYPE_DEFAULT = 0;

    /**
     * 组队后在队伍聊天会话工具栏及设置页面添加有赞商城购物入口
     */
    public static final int ACTION_TYPE_YOUZAN_IN_GROUP = 1;


    /**
     * 活动id  pu_bar表 id 的定义.后台根据特定id做特殊操作,譬如修改推送文本内容等.
     */

    /**
     * 丛林Mad House活动,需要在组队成功或更新队伍类型成功时修改推送文本内容,指导用户找到购票入口
     */
    public static final int ID_MAD_HOUSE = 60003;
}
