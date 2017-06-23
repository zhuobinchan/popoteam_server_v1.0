package com.geetion.generic.sms.pojo;

import java.io.Serializable;
import java.util.Date;

public class SmsCode implements Serializable {

    private Long id;

    private String account;

    private String code;

    private Date createTime;

    private Date updateTime;

    private Long validMillisecond;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getValidMillisecond() {
        return validMillisecond;
    }

    public void setValidMillisecond(Long validMillisecond) {
        this.validMillisecond = validMillisecond;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}