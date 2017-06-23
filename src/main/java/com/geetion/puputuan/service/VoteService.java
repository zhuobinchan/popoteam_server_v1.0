package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Vote;

import java.util.List;
import java.util.Map;

public interface VoteService {

    Vote getVoteById(Long id);

    Vote getVote(Map param);

    List<Vote> getVoteList(Map param);

    PagingResult<Vote> getVotePage(PageEntity pageEntity);

    boolean addVote(Vote object);

    boolean updateVote(Vote object);

    boolean removeVote(Long id);
}
