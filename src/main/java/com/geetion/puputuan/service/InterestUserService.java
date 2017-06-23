package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Interest;
import com.geetion.puputuan.model.InterestUser;

import java.util.List;
import java.util.Map;

public interface InterestUserService {

    InterestUser getInterestUserById(Long id);

    InterestUser getInterestUser(Map param);

    List<InterestUser> getInterestUserList(Map param);

    PagingResult<InterestUser> getInterestUserPage(PageEntity pageEntity);

    boolean addInterestUser(InterestUser object);

    boolean updateInterestUser(InterestUser object);

    boolean removeInterestUser(Long id);


    /**
     * 批量添加
     *
     * @param param
     * @return
     */
    public boolean addInterestUserBatch(Map param);


    /**
     * 查询用户的兴趣
     *
     * @param param
     * @return
     */
    public List<Interest> getInterestByParam(Map param);


    /**
     * 根据ids找出所有的interestIds
     *
     * @param ids
     * @return
     */
    public List<Long> getInterestIdsByUserIds(List<Long> ids);

    /**
     * 根据用户id，删除所有的相关的 interest
     *
     * @param userId
     * @return
     */
    public boolean removeInterestUserByUserId(Long userId);

}
