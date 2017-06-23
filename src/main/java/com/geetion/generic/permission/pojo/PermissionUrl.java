package com.geetion.generic.permission.pojo;

import com.geetion.generic.permission.pojo.base.BaseModel;

import javax.persistence.*;
import java.util.Date;
@Table(name = "geetion_permission_url")
public class PermissionUrl extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long permissionId;

    @Column
    private String url;

    @Column
    private Date createTime;

    private Permission permission;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "GeetionPermissionUrl{" +
                "id=" + id +
                ", permissionId=" + permissionId +
                ", url='" + url + '\'' +
                ", createTime=" + createTime +
                ", geetionPermission=" + permission +
                '}';
    }
}