package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.VoteLike;

import java.util.List;
import java.util.Map;

public interface VoteLikeService {

    VoteLike getVoteLikeById(Long id);

    VoteLike getVoteLike(Map param);

    List<VoteLike> getVoteLikeList(Map param);

    PagingResult<VoteLike> getVoteLikePage(PageEntity pageEntity);

    boolean addVoteLike(VoteLike object);

    boolean updateVoteLike(VoteLike object);

    boolean removeVoteLike(Long id);
}
