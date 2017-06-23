package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface MessageDAO extends BaseDAO<Message, Long> {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public int insertBatch(List<Message> list);

    /**
     * 查询数量
     *
     * @param message
     * @return
     */
    public int countNum(Message message);

    /**
     * 根据不同的参数更新
     *
     * @param message
     * @return
     */
    public int updateByParam(Message message);

    /**
     * 批量删除消息
     * @param ids
     * @return
     */
    public int delMsgBatch(@Param("ids") List<Long> ids);

    /**
     * 根据用户Id删除消息
     * @param userId
     * @return
     */
    public int delMsgByUserId(@Param("userId") Long userId);

    /**
     *
     * @param begin
     * @return
     */
    public List<Message> selectMsgByDate(@Param("begin") Timestamp begin);
}