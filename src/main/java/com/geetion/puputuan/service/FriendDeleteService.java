package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.FriendDelete;

import java.util.List;
import java.util.Map;

public interface FriendDeleteService {

    FriendDelete getFriendDeleteById(Long id);

    FriendDelete getFriendDelete(Map param);

    List<FriendDelete> getFriendDeleteList(Map param);

    PagingResult<FriendDelete> getFriendDeletePage(PageEntity pageEntity);

    boolean addFriendDelete(FriendDelete object);

    boolean updateFriendDelete(FriendDelete object);

    boolean removeFriendDelete(Long id);
}
