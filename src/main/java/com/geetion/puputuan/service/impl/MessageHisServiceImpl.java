package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.dao.MessageHisDAO;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.service.MessageHisService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class MessageHisServiceImpl implements MessageHisService {

    @Resource
    private MessageHisDAO messageHisDAO;


    @Override
    public int insertBatch(List<Message> list) {
        /**
         * 数据量大时，需要考虑分批提交
         */
        return messageHisDAO.insertBatch(list);
    }
}
