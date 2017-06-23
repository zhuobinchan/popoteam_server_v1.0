package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Consultation;

import java.util.List;
import java.util.Map;

public interface ConsultationService {

    Consultation getConsultationById(Long id);

    Consultation getConsultation(Map param);

    List<Consultation> getConsultationList(Map param);

    PagingResult<Consultation> getConsultationPage(PageEntity pageEntity);

    boolean addConsultation(Consultation object);

    boolean updateConsultation(Consultation object);

    boolean removeConsultation(Long id);
}
