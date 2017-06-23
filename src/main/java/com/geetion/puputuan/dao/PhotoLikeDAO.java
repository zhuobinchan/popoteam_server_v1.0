package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.PhotoLike;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface PhotoLikeDAO extends BaseDAO<PhotoLike, Long> {
    public List<HashMap<String, Long>> selectUserPhotoLikeIn(Map params);

    //统计用计算互为点赞的队伍
    public List<HashMap<String, Long>> selectEachOtherPhotoLike(Map params);

    int countPhotoByPhotoId(Long id);

    int userIsLke(Map params);
}