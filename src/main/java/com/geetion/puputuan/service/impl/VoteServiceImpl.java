package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.VoteDAO;
import com.geetion.puputuan.model.Vote;
import com.geetion.puputuan.service.VoteService;
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
public class VoteServiceImpl implements VoteService {

    @Resource
    private VoteDAO voteDAO;

    @Override
    public Vote getVoteById(Long id) {
        return voteDAO.selectByPrimaryKey(id);
    }

    @Override
    public Vote getVote(Map param){
        return voteDAO.selectOne(param);
    }

    @Override
    public List<Vote> getVoteList(Map param) {
        return voteDAO.selectParam(param);
    }

    @Override
    public PagingResult<Vote> getVotePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Vote> list = getVoteList(pageEntity.getParam());
        PageInfo<Vote> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addVote(Vote object) {
        return voteDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateVote(Vote object) {
        return voteDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeVote(Long id) {
        return voteDAO.deleteByPrimaryKey(id) == 1;
    }
}
