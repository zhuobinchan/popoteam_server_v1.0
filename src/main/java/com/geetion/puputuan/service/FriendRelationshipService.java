package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.FriendRelationship;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.UserWithCount;

import java.util.List;
import java.util.Map;

public interface FriendRelationshipService {

    FriendRelationship getFriendRelationshipById(Long id);

    FriendRelationship getFriendRelationship(Map param);

    List<FriendRelationship> getFriendRelationshipList(Map param);

    PagingResult<FriendRelationship> getFriendRelationshipPage(PageEntity pageEntity);

    boolean addFriendRelationship(FriendRelationship object);

    boolean updateFriendRelationship(FriendRelationship object);

    boolean removeFriendRelationship(Long id);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public boolean addFriendRelationshipBatch(List<FriendRelationship> list);

    /**
     * 查询好友
     *
     * @param param
     * @return
     */
    public List<User> getFriendRelationshipWithUser(Map param);


    /**
     * 查询好友，根据首字母排序
     *
     * @param param
     * @return
     */
    public List<User> getUserOrderByFirstPinyin(Map param);

    /**
     * 查询好友，分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<User> getFriendRelationshipWithUserPage(PageEntity pageEntity);

    /**
     * 查询好友，根据首字母排序，分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<User> getWithUserOrderByFirstPinyinPage(PageEntity pageEntity);

    /**
     * 删除好友的关系（两条记录）
     *
     * @param param
     * @return
     */
    public boolean removeFriendRelationshipBetween(Map param);


    /**
     * 找出所有朋友的id
     *
     * @param ids
     * @return
     */
    public List<Long> getFriendIds(List<Long> ids);

    /**
     * 查询用户的好友的信息，带一些计算字段 -- 不分页
     *
     * @param param
     * @return
     */
    public List<UserWithCount> getFriendDetailList(Map param);

    /**
     * 查询用户的好友的信息，带一些计算字段 -- 分页
     *
     * @param pageEntity
     * @return
     */
    public PagingResult<UserWithCount> getFriendDetailPage(PageEntity pageEntity);

}
