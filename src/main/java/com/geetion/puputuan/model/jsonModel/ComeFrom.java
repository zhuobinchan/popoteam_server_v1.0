package com.geetion.puputuan.model.jsonModel;

import com.geetion.generic.districtmodule.pojo.base.BaseModel;

/**
 * Created by dazai on 2016/7/27.
 */
public class ComeFrom extends BaseModel {


    private String province;

    private Integer provinceId;

    private String city;

    private Integer cityId;

    private String area;

    private Integer areaId;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }


}
