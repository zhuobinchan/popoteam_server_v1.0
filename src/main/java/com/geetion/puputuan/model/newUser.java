package com.geetion.puputuan.model;

import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.pojo.base.BaseModel;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.userbase.pojo.UserBase;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by dazai on 2016/7/27.
 */
public class newUser extends BaseModel {

    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long userId;

    @Column
    private String phone;

    @Column
    private String wechatId;

    @Column
    private String nickName;

    @Column
    private String identify;

    @Column
    private String sex;

    @Column
    private String sign;

    @Column
    private Date birthday;

    @Column
    private Long headId;

    @Column
    private String constellation;

    @Column
    private Integer provinceId;

    @Column
    private Integer cityId;

    @Column
    private Integer areaId;

    @Column
    private Integer type;

    @Column
    private Integer device;

    @Column
    private Date createTime;

    @Transient
    private UserBase userBase;

    @Transient
    private District province;

    @Transient
    private District city;

    @Transient
    private District area;

    @Transient
    private File head;

    @Transient
    private List<Job> jobList;

    @Transient
    private List<Interest> interestList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation == null ? null : constellation.trim();
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDevice() {
        return device;
    }

    public void setDevice(Integer device) {
        this.device = device;
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public void setUserBase(UserBase userBase) {
        this.userBase = userBase;
    }

    public District getProvince() {
        return province;
    }

    public void setProvince(District province) {
        this.province = province;
    }

    public District getCity() {
        return city;
    }

    public void setCity(District city) {
        this.city = city;
    }

    public District getArea() {
        return area;
    }

    public void setArea(District area) {
        this.area = area;
    }

    public File getHead() {
        return head;
    }

    public void setHead(File head) {
        this.head = head;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interest> interestList) {
        this.interestList = interestList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }
















}
