package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.model.User;

/**
 * 相册的信息，以及点赞数
 */
public class PhotoWithLikeCount extends Photo {


    //点赞数
    private Integer likeNumber;

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

}