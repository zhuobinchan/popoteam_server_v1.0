package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.NoticeHisDAO;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.service.NoticeHisService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class NoticeHisServiceImpl implements NoticeHisService {

    @Resource
    private NoticeHisDAO noticeHisDAO;



    @Override
    public int insertBatch(List<Notice> list) {
        return noticeHisDAO.insertBatch(list);
    }
}
