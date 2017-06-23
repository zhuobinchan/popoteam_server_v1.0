package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.FriendApplyDAO;
import com.geetion.puputuan.model.FriendApply;
import com.geetion.puputuan.service.FriendApplyService;
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
public class FriendApplyServiceImpl implements FriendApplyService {

    @Resource
    private FriendApplyDAO friendApplyDAO;

    @Override
    public FriendApply getFriendApplyById(Long id) {
        return friendApplyDAO.selectByPrimaryKey(id);
    }

    @Override
    public FriendApply getFriendApply(Map param){
        return friendApplyDAO.selectOne(param);
    }

    @Override
    public List<FriendApply> getFriendApplyList(Map param) {
        return friendApplyDAO.selectParam(param);
    }

    @Override
    public PagingResult<FriendApply> getFriendApplyPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<FriendApply> list = getFriendApplyList(pageEntity.getParam());
        PageInfo<FriendApply> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addFriendApply(FriendApply object) {
        return friendApplyDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateFriendApply(FriendApply object) {
        return friendApplyDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeFriendApply(Long id) {
        return friendApplyDAO.deleteByPrimaryKey(id) == 1;
    }
}
