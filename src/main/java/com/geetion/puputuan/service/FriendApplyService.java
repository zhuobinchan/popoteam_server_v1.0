package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.FriendApply;
import com.geetion.puputuan.model.User;

import java.util.List;
import java.util.Map;

public interface FriendApplyService {

    FriendApply getFriendApplyById(Long id);

    FriendApply getFriendApply(Map param);

    List<FriendApply> getFriendApplyList(Map param);

    PagingResult<FriendApply> getFriendApplyPage(PageEntity pageEntity);

    boolean addFriendApply(FriendApply object);

    boolean updateFriendApply(FriendApply object);

    boolean removeFriendApply(Long id);

}
