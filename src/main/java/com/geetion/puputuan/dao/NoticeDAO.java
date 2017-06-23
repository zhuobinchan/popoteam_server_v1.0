package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Notice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NoticeDAO extends BaseDAO<Notice, Long> {
    /**
     * 查询数量
     *
     * @param notice
     * @return
     */
    int countNum(Notice notice);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int insertBatch(List<Notice> list);

    /**
     * 根据不同的参数更新
     *
     * @param notice
     * @return
     */
    int updateByParam(Notice notice);

    /**
     * 批量删除消息
     * @param ids
     * @return
     */
    int delNoticeBatch(@Param("ids") List<Long> ids);

    /**
     * 根据用户Id删除消息
     * @param userId
     * @return
     */
    int delNoticeByUserId(@Param("userId") Long userId);

    /**
     *
     * @param begin
     * @return
     */
    List<Notice> selectNoticeByDate(@Param("begin") Timestamp begin);
}