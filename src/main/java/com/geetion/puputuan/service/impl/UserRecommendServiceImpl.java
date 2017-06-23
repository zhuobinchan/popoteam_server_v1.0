package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.constant.RecommendType;
import com.geetion.puputuan.dao.UserRecommendDAO;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.model.UserRecommend;
import com.geetion.puputuan.service.UserRecommendService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserRecommendServiceImpl implements UserRecommendService {

    @Resource
    private UserRecommendDAO userRecommendDAO;


    @Override
    public List<UserRecommend> getUserRecommendByUserId(Long userId, Integer status) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("status",status);
        return userRecommendDAO.selectParam(param);
    }

    @Override
    public boolean addUserRecommendBatch(List<UserRecommend> userRecommendList) {
        return userRecommendDAO.insertList(userRecommendList) == userRecommendList.size();
    }

    @Override
    public boolean removeUserRecommendBatch(List<Recommend> recommendList) {
        return userRecommendDAO.deleteUserRecommendBatch(recommendList) == recommendList.size();
    }

    @Override
    public void removeUserRecommendByGroupId(Map params) {
        // 调用存储过程清理相关数据
         userRecommendDAO.deleteUserRecommendByGroupId(params);
    }

    @Override
    public void sendUserRecommendMsgHuanXin(List<Recommend> recommends, List<UserRecommend> userRecommends) {

    }

    @Override
    public List<UserRecommend> getUserRecommendByParams(Map params) {
        return userRecommendDAO.selectUserRecommendByParams(params);
    }

    @Override
    public void updateUserRecommend(UserRecommend userRecommend) {
        userRecommendDAO.update(userRecommend);
    }

    @Override
    public void setUserRecommendSend(List<UserRecommend> userRecommendList) {
        for (UserRecommend ur : userRecommendList){
            ur.setStatus(RecommendType.HAVE_SEND);
            userRecommendDAO.update(ur);
        }
    }

}
