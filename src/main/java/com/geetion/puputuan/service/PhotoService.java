package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.pojo.PhotoWithLikeCount;

import java.util.List;
import java.util.Map;

public interface PhotoService {

    Photo getPhotoById(Long id);

    Photo getPhoto(Map param);

    List<Photo> getPhotoList(Map param);

    PagingResult<Photo> getPhotoPage(PageEntity pageEntity);

    boolean addPhoto(Photo object);

    boolean updatePhoto(Photo object);

    boolean removePhoto(Long id);


    /**
     * 查询带有点赞数的相册列表 --不分页
     *
     * @param param
     * @return
     */
    public List<PhotoWithLikeCount> getPhotoWithLikeCountList(Map param);

    /**
     * 查询带有点赞数的相册列表 -- 分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<PhotoWithLikeCount> getPhotoWithLikeCountPage(PageEntity pageEntity);

}
