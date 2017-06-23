package com.geetion.puputuan.model;

import com.geetion.generic.serverfile.model.File;
import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pu_photo")
public class Photo extends BaseModel {
    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long imageId;

    @Column
    private String content;

    @Column
    private Integer type;

//    @Column
//    private Boolean isDelete;

    @Column
    private Boolean isAvatar;

    @Column
    private Date createTime;

    // TODO add by simon at 2016/08/04
//    @Column
//    private Boolean isFavourite;

    @Transient
    private File image;

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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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

//    public Boolean getIsDelete() {
//        return isDelete;
//    }
//
//    public void setIsDelete(Boolean isDelete) {
//        this.isDelete = isDelete;
//    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Boolean getIsAvatar() {
        return isAvatar;
    }

    public void setIsAvatar(Boolean isAvatar) {
        this.isAvatar = isAvatar;
    }
}