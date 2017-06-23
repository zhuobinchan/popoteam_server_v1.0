package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.UserSuperLike;
import com.geetion.puputuan.model.UserSuperLikeConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSuperLikeConfigDAO extends BaseDAO<UserSuperLikeConfig, Long> {

}