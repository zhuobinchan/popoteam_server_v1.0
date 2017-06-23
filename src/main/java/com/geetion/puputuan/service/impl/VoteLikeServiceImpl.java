package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.VoteLikeDAO;
import com.geetion.puputuan.model.VoteLike;
import com.geetion.puputuan.service.VoteLikeService;
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
public class VoteLikeServiceImpl implements VoteLikeService {

    @Resource
    private VoteLikeDAO voteLikeDAO;

    @Override
    public VoteLike getVoteLikeById(Long id) {
        return voteLikeDAO.selectByPrimaryKey(id);
    }

    @Override
    public VoteLike getVoteLike(Map param){
        return voteLikeDAO.selectOne(param);
    }

    @Override
    public List<VoteLike> getVoteLikeList(Map param) {
        return voteLikeDAO.selectParam(param);
    }

    @Override
    public PagingResult<VoteLike> getVoteLikePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<VoteLike> list = getVoteLikeList(pageEntity.getParam());
        PageInfo<VoteLike> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addVoteLike(VoteLike object) {
        return voteLikeDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateVoteLike(VoteLike object) {
        return voteLikeDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeVoteLike(Long id) {
        return voteLikeDAO.deleteByPrimaryKey(id) == 1;
    }
}
