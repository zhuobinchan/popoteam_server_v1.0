package com.geetion.puputuan.service;

import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.model.UserRecommend;

import java.util.List;
import java.util.Map;

public interface UserRecommendService {

    List<UserRecommend> getUserRecommendByUserId(Long id, Integer status);

    boolean addUserRecommendBatch(List<UserRecommend> userRecommendList);

    boolean removeUserRecommendBatch(List<Recommend> recommendList);

    void removeUserRecommendByGroupId(Map params);

    void sendUserRecommendMsgHuanXin(List<Recommend> recommends, List<UserRecommend> userRecommends);

    List<UserRecommend> getUserRecommendByParams(Map params);

    void updateUserRecommend(UserRecommend userRecommend);


    void setUserRecommendSend(List<UserRecommend> userRecommendList);
}
