package com.geetion.puputuan.model;

import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "pu_user")
public class User extends BaseModel {
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
    private String nickNameChr;

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
    private String province;
    
    @Column
    private Integer cityId;

    @Column
    private String city;

    @Column
    private Integer areaId;
    
    @Column
    private String area;

    @Column
    private Integer type;

    @Column
    private Integer device;

    @Column
    private Date createTime;

    @Column
    private Date friendUpdateTime;

    @Transient
    private UserBase userBase;

    @Transient
    private File head;

    @Transient
    private List<Job> jobList;

    @Transient
    private List<Interest> interestList;

    @Transient
    private List<Fancy> fancyList;

    @Transient
    private List<Photo> album;

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

    public String getNickNameChr() {
        return nickNameChr;
    }

    public void setNickNameChr(String nickNameChr) {
        this.nickNameChr = nickNameChr == null ? null : nickNameChr.trim();
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

    public Date getFriendUpdateTime() {
        return friendUpdateTime;
    }

    public void setFriendUpdateTime(Date friendUpdateTime) {
        this.friendUpdateTime = friendUpdateTime;
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

    public List<Photo> getAlbum() {
        return album;
    }

    public void setAlbum(List<Photo> album) {
        this.album = album;
    }

    public List<Fancy> getFancyList() {
        return fancyList;
    }

    public void setFancyList(List<Fancy> fancyList) {
        this.fancyList = fancyList;
    }
}