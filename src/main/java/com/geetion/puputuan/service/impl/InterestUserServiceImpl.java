package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.InterestUserDAO;
import com.geetion.puputuan.model.Interest;
import com.geetion.puputuan.model.InterestUser;
import com.geetion.puputuan.service.InterestUserService;
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
public class InterestUserServiceImpl implements InterestUserService {

    @Resource
    private InterestUserDAO interestUserDAO;

    @Override
    public InterestUser getInterestUserById(Long id) {
        return interestUserDAO.selectByPrimaryKey(id);
    }

    @Override
    public InterestUser getInterestUser(Map param) {
        return interestUserDAO.selectOne(param);
    }

    @Override
    public List<InterestUser> getInterestUserList(Map param) {
        return interestUserDAO.selectParam(param);
    }

    @Override
    public PagingResult<InterestUser> getInterestUserPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<InterestUser> list = getInterestUserList(pageEntity.getParam());
        PageInfo<InterestUser> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addInterestUser(InterestUser object) {
        return interestUserDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateInterestUser(InterestUser object) {
        return interestUserDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeInterestUser(Long id) {
        return interestUserDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addInterestUserBatch(Map param) {
        return interestUserDAO.insertBatch(param) > 0;
    }

    @Override
    public List<Interest> getInterestByParam(Map param) {
        return interestUserDAO.selectWithInterestByParam(param);
    }

    @Override
    public List<Long> getInterestIdsByUserIds(List<Long> ids) {
        return interestUserDAO.selectInterestIds(ids);
    }

    @Override
    public boolean removeInterestUserByUserId(Long userId) {
        return interestUserDAO.deleteByUserId(userId) > 0;
    }
}
