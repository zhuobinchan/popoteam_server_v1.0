package com.geetion.puputuan.model.jsonModel;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Simon on 2016/8/3.
 */
@Table(name = "pu_favourite_pic")
public class UserFavouritePic extends BaseModel {

    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long albumPhotoId;

    @Column
    private Long imageId;

    @Column
    private String imageUrl;

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

    public Long getAlbumPhotoId() {
        return albumPhotoId;
    }

    public void setAlbumPhotoId(Long albumPhotoId) {
        this.albumPhotoId = albumPhotoId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
