package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.FriendDeleteDAO;
import com.geetion.puputuan.model.FriendDelete;
import com.geetion.puputuan.service.FriendDeleteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class FriendDeleteServiceImpl implements FriendDeleteService {

    @Resource
    private FriendDeleteDAO friendDeleteDAO;

    @Override
    public FriendDelete getFriendDeleteById(Long id) {
        return friendDeleteDAO.selectByPrimaryKey(id);
    }

    @Override
    public FriendDelete getFriendDelete(Map param){
        return friendDeleteDAO.selectOne(param);
    }

    @Override
    public List<FriendDelete> getFriendDeleteList(Map param) {
        return friendDeleteDAO.selectParam(param);
    }

    @Override
    public PagingResult<FriendDelete> getFriendDeletePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<FriendDelete> list = getFriendDeleteList(pageEntity.getParam());
        PageInfo<FriendDelete> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addFriendDelete(FriendDelete object) {
        return friendDeleteDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateFriendDelete(FriendDelete object) {
        return friendDeleteDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFriendDelete(Long id) {
        return friendDeleteDAO.deleteByPrimaryKey(id) == 1;
    }
}
