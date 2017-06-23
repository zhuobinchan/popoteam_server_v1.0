package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "pu_app_setting")
public class AppSetting extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    //APP设置 -- 广告图页数上限
    @Column
    private Integer advertisementPages;

    //APP设置 -- 轮播间隔时间 -- 单位秒
    @Column
    private Float carouselInterval;

    //约会判断设置 -- 定位时间间隔 -- 单位分钟
    @Column
    private Integer activityPositionInterval;

    //约会判断设置 -- 新约会间隔时间 -- 单位小时
    @Column
    private Integer activityNewInterval;

    //约会判断设置 -- 判断半径 -- 单位米
    @Column
    private Integer activityPositionRadius;

    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdvertisementPages() {
        return advertisementPages;
    }

    public void setAdvertisementPages(Integer advertisementPages) {
        this.advertisementPages = advertisementPages;
    }

    public Float getCarouselInterval() {
        return carouselInterval;
    }

    public void setCarouselInterval(Float carouselInterval) {
        this.carouselInterval = carouselInterval;
    }

    public Integer getActivityPositionInterval() {
        return activityPositionInterval;
    }

    public void setActivityPositionInterval(Integer activityPositionInterval) {
        this.activityPositionInterval = activityPositionInterval;
    }

    public Integer getActivityNewInterval() {
        return activityNewInterval;
    }

    public void setActivityNewInterval(Integer activityNewInterval) {
        this.activityNewInterval = activityNewInterval;
    }

    public Integer getActivityPositionRadius() {
        return activityPositionRadius;
    }

    public void setActivityPositionRadius(Integer activityPositionRadius) {
        this.activityPositionRadius = activityPositionRadius;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}