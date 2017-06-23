package com.geetion.puputuan.service;

import com.geetion.puputuan.model.jsonModel.UserFavouritePic;

import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2016/8/4.
 */
public interface UserFavouritePicService {

    UserFavouritePic getFavouritePicById(Long id);

    UserFavouritePic getFavouritePic(Map param);

    List<UserFavouritePic> getFavouritePicList(Map param);

    boolean addFavouritePic(UserFavouritePic object);

    boolean updateFavouritePic(UserFavouritePic object);

    boolean removeFavouritePicById(Long id);

    boolean removeFavouritePicByUserId(Long userId);

    boolean addFavouritePicBatch(Map param);

    boolean updateFavouritePicBatch(Map param);
}
