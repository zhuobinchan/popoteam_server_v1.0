package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.PhotoLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PhotoLikeService {

    PhotoLike getPhotoLikeById(Long id);

    PhotoLike getPhotoLike(Map param);

    List<PhotoLike> getPhotoLikeList(Map param);

    int countPhotoLikeByPhotoId(Long id);

    boolean userIsLikePhoto(Map param);

    PagingResult<PhotoLike> getPhotoLikePage(PageEntity pageEntity);

    boolean addPhotoLike(PhotoLike object);

    boolean updatePhotoLike(PhotoLike object);

    boolean removePhotoLike(Long id);

    public List<HashMap<String, Long>> getUserPhotoLikeIn(Map param);

    public List<HashMap<String, Long>> getEachOtherPhotoLike(Map param);

}
