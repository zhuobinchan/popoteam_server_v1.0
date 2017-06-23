package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.NoticeDAO;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.service.NoticeService;
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
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeDAO noticeDAO;


    @Override
    public Notice getNoticeById(Long id) {
        return noticeDAO.selectByPrimaryKey(id);
    }

    @Override
    public PagingResult<Notice> getNoticePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Notice> list = getNoticeList(pageEntity.getParam());
        PageInfo<Notice> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean updateNotice(Notice object) {
        return noticeDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public int countNoticeNum(Notice notice) {
        return noticeDAO.countNum(notice);
    }

    @Override
    public List<Notice> getNoticeList(Map param) {
        return noticeDAO.selectParam(param);
    }

    @Override
    public boolean addNoticeBatch(List<Notice> list) {
        return noticeDAO.insertBatch(list) == list.size();
    }

    @Override
    public boolean updateNoticeByParam(Notice notice) {
        return noticeDAO.updateByParam(notice) > 0;
    }

    @Override
    public boolean delNotice(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return this.delMulNotice(ids);
    }

    @Override
    public boolean delMulNotice(List<Long> ids) {
        return noticeDAO.delNoticeBatch(ids) == ids.size();
    }

    @Override
    public boolean delAllNotice(Long userId) {
        noticeDAO.delNoticeByUserId(userId);
        return true;
    }

    @Override
    public List<Notice> selectNoticeByDate(Timestamp begin) {
        return noticeDAO.selectNoticeByDate(begin);
    }
}
