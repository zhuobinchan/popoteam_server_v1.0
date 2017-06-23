package com.geetion.puputuan.service;

import com.geetion.puputuan.model.UserLikeGroup;

import java.util.List;
import java.util.Map;

public interface UserLikeGroupService {

    List<UserLikeGroup> getUserLikeGroupList(Map params);

    boolean addUserLikeGroupLike(List<UserLikeGroup> list);

    void setUserLikeGroupToInvalid(Long groupId);
}
