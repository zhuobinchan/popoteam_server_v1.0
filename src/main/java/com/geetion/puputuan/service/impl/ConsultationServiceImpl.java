package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.ConsultationDAO;
import com.geetion.puputuan.model.Consultation;
import com.geetion.puputuan.service.ConsultationService;
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
public class ConsultationServiceImpl implements ConsultationService {

    @Resource
    private ConsultationDAO consultationDAO;

    @Override
    public Consultation getConsultationById(Long id) {
        return consultationDAO.selectByPrimaryKey(id);
    }

    @Override
    public Consultation getConsultation(Map param){
        return consultationDAO.selectOne(param);
    }

    @Override
    public List<Consultation> getConsultationList(Map param) {
        return consultationDAO.selectParam(param);
    }

    @Override
    public PagingResult<Consultation> getConsultationPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Consultation> list = getConsultationList(pageEntity.getParam());
        PageInfo<Consultation> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addConsultation(Consultation object) {
        return consultationDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateConsultation(Consultation object) {
        return consultationDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeConsultation(Long id) {
        return consultationDAO.deleteByPrimaryKey(id) == 1;
    }
}
