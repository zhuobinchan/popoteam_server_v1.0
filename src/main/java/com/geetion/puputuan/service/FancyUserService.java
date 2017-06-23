package com.geetion.puputuan.service;

import com.geetion.puputuan.model.FancyUser;

public interface FancyUserService {

    boolean addFancyUser(FancyUser fancyUser);

    /**
     * 根据用户id，删除所有的相关的 fancy
     *
     * @param userId
     * @return
     */
    boolean removeFancyUserByUserId(Long userId);

}
