package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.pojo.PhotoWithLikeCount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PhotoDAO extends BaseDAO<Photo, Long> {

    /**
     * 查询带有点赞数的相册列表
     *
     * @param param
     * @return
     */
    public List<PhotoWithLikeCount> selectParamWithLikeCount(Map param);
}