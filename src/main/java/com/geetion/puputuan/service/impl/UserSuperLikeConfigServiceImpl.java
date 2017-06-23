package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.UserSuperLikeConfigDAO;
import com.geetion.puputuan.model.UserSuperLikeConfig;
import com.geetion.puputuan.service.UserSuperLikeConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class UserSuperLikeConfigServiceImpl implements UserSuperLikeConfigService {

    @Resource
    private UserSuperLikeConfigDAO userSuperLikeConfigDAO;

    @Override
    public UserSuperLikeConfig getUserSuperLikeConfig(Long userId) {

        Map<String, Object> params = new HashMap<>();

        params.put("userId", userId);

        List<UserSuperLikeConfig> userSuperLikeConfigs = userSuperLikeConfigDAO.selectParam(params);

        return userSuperLikeConfigs.size() > 0 ? userSuperLikeConfigs.get(0) : null;
    }

    @Override
    public UserSuperLikeConfig getUserSuperLikeConfigById(Long id) {
        return userSuperLikeConfigDAO.selectByPrimaryKey(id);
    }

    @Override
    public UserSuperLikeConfig getUserSuperLikeConfigByParam(Map param) {
        return userSuperLikeConfigDAO.selectOne(param);
    }

    @Override
    public List<UserSuperLikeConfig> getUserSuperLikeConfigList(Map param) {
        return userSuperLikeConfigDAO.selectParam(param);
    }

    @Override
    public PagingResult<UserSuperLikeConfig> getUserSuperLikeConfigPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserSuperLikeConfig> list = getUserSuperLikeConfigList(pageEntity.getParam());
        PageInfo<UserSuperLikeConfig> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean updateUserSuperLikeConfig(UserSuperLikeConfig userSuperLikeConfig){return userSuperLikeConfigDAO.updateByPrimaryKeySelective(userSuperLikeConfig) == 1;
    }

    @Override
    public boolean removeUserSuperLikeConfig(Long id) {
        return userSuperLikeConfigDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addUserSuperLikeConfig(UserSuperLikeConfig userSuperLikeConfig) {
        return userSuperLikeConfigDAO.insertSelective(userSuperLikeConfig)==1;
    }
}
