package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.model.UserRecommend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRecommendDAO extends BaseDAO<UserRecommend, Long> {

    int deleteUserRecommendBatch( List<Recommend> list);

    void deleteUserRecommendByGroupId(Map params);

    List<UserRecommend> selectUserRecommendByParams(Map params);

    int update(UserRecommend userRecommend);

    /**
     * 将用户约会消息设置为已推送
     * @param userRecommendList
     * @return
     */
    int updateBatch(List<UserRecommend> userRecommendList);
}