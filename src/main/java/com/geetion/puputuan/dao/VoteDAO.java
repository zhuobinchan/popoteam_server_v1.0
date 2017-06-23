package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Vote;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteDAO extends BaseDAO<Vote, Long> {

}