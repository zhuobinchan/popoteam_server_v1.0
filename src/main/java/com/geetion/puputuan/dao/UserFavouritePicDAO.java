package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.jsonModel.UserFavouritePic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Simon on 2016/8/4.
 */
@Repository
public interface UserFavouritePicDAO extends BaseDAO<UserFavouritePic, Long> {

    int insertBatch(Map param);

    int updateBatch(Map param);

    int deleteByUserId(@Param("userId") Long userId);
}
