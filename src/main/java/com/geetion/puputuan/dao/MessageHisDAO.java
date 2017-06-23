package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.model.MessageHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MessageHisDAO extends BaseDAO<MessageHis, Long> {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    public int insertBatch(List<Message> list);

}