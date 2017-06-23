package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.model.MessageHis;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.model.NoticeHis;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeHisDAO extends BaseDAO<NoticeHis, Long> {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int insertBatch(List<Notice> list);

}