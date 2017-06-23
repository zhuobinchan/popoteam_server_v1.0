package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Message;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface MessageService {

    Message getMessageById(Long id);

    Message getMessage(Map param);

    List<Message> getMessageList(Map param);

    PagingResult<Message> getMessagePage(PageEntity pageEntity);

    boolean addMessage(Message object);

    boolean updateMessage(Message object);

    boolean removeMessage(Long id);


    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public boolean addMessageBatch(List<Message> list);

    /**
     * 批量添加
     *
     * @param message
     * @return
     */
    public int countMessageNum(Message message);

    /**
     * 根据不同的参数更新 Message
     *
     * @param message
     * @return
     */
    public boolean updateMessageByParam(Message message);

    /**
     * 删除单条消息
     * @param id
     * @return
     */
    boolean delMsg(Long id);

    /**
     * 删除多条消息
     * @param ids
     * @return
     */
    boolean delMulMsg(List<Long> ids);

    /**
     * 删除用户名下所以消息
     * @param userId
     * @return
     */
    boolean delAllMsg(Long userId);

    /**
     * 取某时间段的信息
     * @param begin
     * @return
     */
    List<Message> selectMsgByDate(Timestamp begin);

}
