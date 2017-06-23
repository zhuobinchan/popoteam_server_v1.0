package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.SysDic;
import com.geetion.puputuan.model.UserSuperLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSuperLikeDAO extends BaseDAO<UserSuperLike, Long> {
    List<UserSuperLike> selectTodaySuperLike(Long userId);

    void deleteSuperLike(Map param);
}