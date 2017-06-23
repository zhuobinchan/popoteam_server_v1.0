package com.geetion.puputuan.model;

import com.geetion.generic.districtmodule.pojo.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by dazai on 2016/7/27.
 */
public class Newjob extends BaseModel {


    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private String name;

    @Transient
    private Boolean isSelected = true;

    @Column
    private Date createTime;

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
        this.name = name;
    }

    public Boolean getHaveSelect() {
        return isSelected;
    }

    public void setHaveSelect(Boolean haveSelect) {
        this.isSelected = haveSelect;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
