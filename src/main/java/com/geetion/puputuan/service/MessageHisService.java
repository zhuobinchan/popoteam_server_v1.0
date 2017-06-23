package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.model.MessageHis;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface MessageHisService {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int insertBatch(List<Message> list);
}
