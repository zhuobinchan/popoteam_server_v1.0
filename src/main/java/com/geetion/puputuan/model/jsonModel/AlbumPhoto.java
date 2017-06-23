package com.geetion.puputuan.model.jsonModel;

/**
 * Created by admin on 2016/7/29.
 */
public class AlbumPhoto {

    private long albumPhotoId;

    private long imageId;

    private String imageUrl;

    private boolean isAvatar;

    public long getAlbumPhotoId() {
        return albumPhotoId;
    }

    public void setAlbumPhotoId(long albumPhotoId) {
        this.albumPhotoId = albumPhotoId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getIsAvatar() {
        return isAvatar;
    }

    public void setIsAvatar(boolean isAvatar) {
        this.isAvatar = isAvatar;
    }
}
