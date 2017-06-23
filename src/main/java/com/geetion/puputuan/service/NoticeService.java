package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Notice;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface NoticeService {

    Notice getNoticeById(Long id);

    PagingResult<Notice> getNoticePage(PageEntity pageEntity);
    /**
     *更新对象
     * @param object
     * @return
     */
    boolean updateNotice(Notice object);

    /**
     * 按条件统计数量
     * @param notice
     * @return
     */
    int countNoticeNum(Notice notice);

    List<Notice> getNoticeList(Map param);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    boolean addNoticeBatch(List<Notice> list);

    /**
     * 根据不同的参数更新 notice
     *
     * @param notice
     * @return
     */
    boolean updateNoticeByParam(Notice notice);

    /**
     * 删除单条消息
     * @param id
     * @return
     */
    boolean delNotice(Long id);

    /**
     * 删除多条消息
     * @param ids
     * @return
     */
    boolean delMulNotice(List<Long> ids);

    /**
     * 删除用户名下所以消息
     * @param userId
     * @return
     */
    boolean delAllNotice(Long userId);

    /**
     * 取某时间段的信息
     * @param begin
     * @return
     */
    List<Notice> selectNoticeByDate(Timestamp begin);
}
