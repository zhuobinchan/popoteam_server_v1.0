package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.FriendRelationship;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.UserWithCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FriendRelationshipDAO extends BaseDAO<FriendRelationship, Long> {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public int insertBatch(List<FriendRelationship> list);

    /**
     * 查询好友
     *
     * @param param
     * @return
     */
    public List<User> selectParamWithUser(Map param);

    /**
     * 查询好友，根据首字母进行排序
     *
     * @param param
     * @return
     */
    public List<User> selectParamWithUserOrderByFirstPinyin(Map param);

    /**
     * 删除好友关系（互相的关系）
     *
     * @param param
     * @return
     */
    public int deleteBetween(Map param);

    /**
     * 根据团队的所有id查出一级朋友关系
     *
     * @param ids
     * @return
     */
    public List<Long> selectFriendId(@Param("ids") List<Long> ids);

    /**
     * 查询用户的好友的信息，带一些计算字段
     *
     * @param param
     * @return
     */
    public List<UserWithCount> selectFriendDetail(Map param);
}