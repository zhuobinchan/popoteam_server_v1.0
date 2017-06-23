package com.geetion.puputuan.service;


import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.UserBlackList;

import java.util.List;
import java.util.Map;

public interface UserBlackListService {
    boolean checkIfInBL(Long userId);

    UserBlackList getUserBlackListById(Long id);

    UserBlackList getUserBlackList(Map param);

    List<UserBlackList> getUserBlackListList(Map param);

    PagingResult<UserBlackList> getUserBlackListPage(PageEntity pageEntity);

    boolean updateUserBlackList(UserBlackList userBlackList);

    boolean removeUserBlackList(Long id);

    /**
     * 批量添加 黑名单
     * map里放入identities list数组 或者 放入 phone 数组 批量添加
     * @param map
     * @return
     */
    boolean addUserBlackLists(Map map);

    /**
     * 批量删除 黑名单
     * map里放入identities list数组 或者 放入 phone 数组 批量添加
     * @param map
     * @return
     */
    boolean removeBlackLists(Map map);
}
