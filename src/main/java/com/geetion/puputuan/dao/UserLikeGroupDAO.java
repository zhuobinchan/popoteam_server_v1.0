package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.UserLikeGroup;
import com.geetion.puputuan.model.UserSuperLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserLikeGroupDAO extends BaseDAO<UserLikeGroup, Long> {
    List<UserLikeGroup> selectParam(Map params);
    int updateToInvalid(Map params);
}