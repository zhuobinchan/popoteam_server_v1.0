package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.MessageDAO;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageDAO messageDAO;
    @Override
    public Message getMessageById(Long id) {
        return messageDAO.selectByPrimaryKey(id);
    }

    @Override
    public Message getMessage(Map param) {
        return messageDAO.selectOne(param);
    }

    @Override
    public List<Message> getMessageList(Map param) {
        return messageDAO.selectParam(param);
    }

    @Override
    public PagingResult<Message> getMessagePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Message> list = getMessageList(pageEntity.getParam());
        PageInfo<Message> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addMessage(Message object) {
        return messageDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateMessage(Message object) {
        return messageDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removeMessage(Long id) {
        return messageDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean addMessageBatch(List<Message> list) {
        return messageDAO.insertBatch(list) == list.size();
    }

    @Override
    public int countMessageNum(Message message) {
        return messageDAO.countNum(message);
    }

    @Override
    public boolean updateMessageByParam(Message message) {
        return messageDAO.updateByParam(message) > 0;
    }

    @Override
    public boolean delMsg(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return this.delMulMsg(ids);
    }

    @Override
    public boolean delMulMsg(List<Long> ids) {

        return messageDAO.delMsgBatch(ids) == ids.size();
    }

    @Override
    public boolean delAllMsg(Long userId) {
        messageDAO.delMsgByUserId(userId);
        return true;
    }

    @Override
    public List<Message> selectMsgByDate(Timestamp begin) {
        return messageDAO.selectMsgByDate(begin);
    }

}
