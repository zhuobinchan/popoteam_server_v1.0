package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Job;
import com.geetion.puputuan.model.UserBlackList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserBlackListDAO extends BaseDAO<UserBlackList, Long> {
    int insertUserBlackLists(Map map);
    int deleteUserBlackLists(Map map);
}