package com.geetion.puputuan.model;

import com.geetion.puputuan.model.base.BaseModel;
import com.geetion.puputuan.utils.EmojiCharacterUtil;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pu_group")
public class Group extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private String name;

    @Column
    private String token;

    @Column
    private Long locationId;

    @Column
    private Integer provinceId;

    @Column
    private Integer cityId;

    @Column
    private Integer areaId;

    @Column
    private Long barId;

    @Column
    private String roomId;

    @Column
    private Integer status;

    @Column
    private Integer type;

    @Column
    private Date createTime;

    @Column
    private String province;

    @Column
    private String city;

    @Column
    private String area;

    @Column
    private Date modifyTime;
    @Column
    private String runTime;
    @Column
    private Integer recommendSex;
    @Transient
    private Location location;

    @Transient
    private Bar bar;

//    @Transient
//    private District province;
//
//    @Transient
//    private District city;
//
//    @Transient
//    private District area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getBarId() {
        return barId;
    }

    public void setBarId(Long barId) {
        this.barId = barId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public Integer getRecommendSex() {
        return recommendSex;
    }

    public void setRecommendSex(Integer recommendSex) {
        this.recommendSex = recommendSex;
    }
}