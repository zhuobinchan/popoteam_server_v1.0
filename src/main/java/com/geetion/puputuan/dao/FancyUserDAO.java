package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Fancy;
import com.geetion.puputuan.model.FancyUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FancyUserDAO extends BaseDAO<FancyUser, Long> {
    public int deleteByUserId(@Param("userId") Long userId);
}