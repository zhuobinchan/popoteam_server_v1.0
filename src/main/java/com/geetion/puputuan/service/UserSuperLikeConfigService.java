package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.UserSuperLike;
import com.geetion.puputuan.model.UserSuperLikeConfig;

import java.util.List;
import java.util.Map;

public interface UserSuperLikeConfigService {

    UserSuperLikeConfig getUserSuperLikeConfig(Long userId);

    UserSuperLikeConfig getUserSuperLikeConfigById(Long id);

    UserSuperLikeConfig getUserSuperLikeConfigByParam(Map param);

    List<UserSuperLikeConfig> getUserSuperLikeConfigList(Map param);

    PagingResult<UserSuperLikeConfig> getUserSuperLikeConfigPage(PageEntity pageEntity);

    boolean updateUserSuperLikeConfig(UserSuperLikeConfig userSuperLikeConfig);

    boolean removeUserSuperLikeConfig(Long id);

    boolean addUserSuperLikeConfig(UserSuperLikeConfig userSuperLikeConfig);

}
