package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class DefaultSettingValue {

    /**
     * 客户端设置，后台约会设置，权重设置的各种默认值
     */

    /**
     * APP设置 -- 广告图页数上限
     */
    public static final int advertisementPages = 5;
    /**
     * APP设置 -- 轮播间隔时间 -- 单位秒
     */
    public static final float carouselInterval = 3;
    /**
     * 约会判断设置 -- 定位时间间隔 -- 单位分钟
     */
    public static final int activityPositionInterval = 15;
    /**
     * 约会判断设置 -- 新约会间隔时间 -- 单位小时
     */
    public static final int activityNewInterval = 15;
    /**
     * 约会判断设置 -- 判断半径 -- 单位米
     */
    public static final int activityPositionRadius = 5000;


    /**
     * 匹配权重 -- 相互点赞 -- 单位是百分比
     */
    public static final int mutualLike = 30;
    /**
     * 匹配权重 -- 关系度 -- 单位是百分比
     */
    public static final int relationship = 30;
    /**
     * 匹配权重 -- 已投票结果 -- 单位是百分比
     */
    public static final int voteResult = 30;
    /**
     * 匹配权重 -- 星座 -- 单位是百分比
     */
    public static final int constellation = 30;
    /**
     * 匹配权重 -- 职业兴趣 -- 单位是百分比
     */
    public static final int interestionJob = 30;
    /**
     * 匹配权重 -- 约会签到 -- 单位是百分比
     */
    public static final int dataSign = 30;


}
