package com.geetion.puputuan.service;

import com.geetion.puputuan.model.Notice;

import java.util.List;

public interface NoticeHisService {

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int insertBatch(List<Notice> list);
}
