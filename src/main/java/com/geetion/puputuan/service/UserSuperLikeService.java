package com.geetion.puputuan.service;

import com.geetion.puputuan.model.SysDic;
import com.geetion.puputuan.model.UserSuperLike;

import java.util.List;
import java.util.Map;

public interface UserSuperLikeService {

    List<UserSuperLike> getTodaySuperLike(Long userId);

    boolean addUserSuperLike(UserSuperLike object);

    List<UserSuperLike> getSuperLikeByParam(Map param);

    void deleteUserSuperLike(Map param);
}
