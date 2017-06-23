package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.RecommendDAO;
import com.geetion.puputuan.model.Recommend;
import com.geetion.puputuan.service.RecommendService;
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
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private RecommendDAO recommendDAO;

    @Override
    public Recommend getRecommendById(Long id) {
        return recommendDAO.selectByPrimaryKey(id);
    }

    @Override
    public Recommend getRecommend(Map param) {
        return recommendDAO.selectOne(param);
    }

    @Override
    public List<Recommend> getRecommendList(Map param) {
        return recommendDAO.selectParam(param);
    }

    @Override
    public PagingResult<Recommend> getRecommendPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Recommend> list = getRecommendList(pageEntity.getParam());
        PageInfo<Recommend> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addRecommend(Recommend object) {
        return recommendDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateRecommend(Recommend object) {
        return recommendDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeRecommend(Long id) {
        return recommendDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public int removeMainIdRecommend(Long id) {
        return recommendDAO.deleteByMainId(id);
    }

    @Override
    public int removeMainIdMatchIdRecommend(Long mainId, Long matchId) {
        Recommend recommend = new Recommend();
        recommend.setMainGroupId(mainId);
        recommend.setMatchGroupId(matchId);
        return recommendDAO.delete(recommend);
    }

    @Override
    public List<Recommend> getRecommendListByScore(Map param) {
        return recommendDAO.selectParamByScore(param);
    }

    @Override
    public boolean addRecommendBatch(List<Recommend> recommends) {
        return recommendDAO.insertList(recommends) == recommends.size();
    }

    @Override
    public int sumGroupRecommend(Long groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        return recommendDAO.sumGroupRecommend(params);
    }
}
