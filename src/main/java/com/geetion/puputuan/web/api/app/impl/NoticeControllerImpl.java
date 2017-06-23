package com.geetion.puputuan.web.api.app.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.MessageType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.service.AnnouncementService;
import com.geetion.puputuan.service.NoticeService;
import com.geetion.puputuan.web.api.app.NoticeController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class NoticeControllerImpl extends BaseController implements NoticeController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private AnnouncementService announcementService;


    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        logger.info("NoticeControllerImpl search begin...");
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("userId", shiroService.getLoginUserBase().getId());
            //查询对用户可见的消息（这里是针对公告类型的消息）
            param.put("status", MessageType.CAN_READ);
            switch (methodType) {
                case 1:
                    List<Notice> list = noticeService.getNoticeList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Notice> pagingForKeyword = noticeService.getNoticePage(pageEntity);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object setRead(Long id) {
        logger.info("NoticeControllerImpl setRead begin...");
        if (checkParaNULL(id)) {
            Notice notice = new Notice();
            notice.setId(id);
            notice.setIsRead(true);
            boolean result = noticeService.updateNotice(notice);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object unReadNum() {
        Notice notice = new Notice();
        notice.setIsRead(false);
        notice.setUserId(shiroService.getLoginUserBase().getId());
        notice.setStatus(MessageType.CAN_READ);

        //计算用户有几条未读的信息
        int unReadNum = noticeService.countNoticeNum(notice);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("unReadNum", unReadNum);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delNotice(Long id) {
        logger.info("NoticeControllerImpl delNotice begin...");
        if(checkParaNULL(id)) {
            this.noticeService.delNotice(id);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delMulNotice(List<Long> ids) {
        logger.info("NoticeControllerImpl delMulNotice begin...");
        if(checkParaNULL(ids)) {
            this.noticeService.delMulNotice(ids);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delAllNotice(Long userId) {
        logger.info("NoticeControllerImpl delAllNotice begin...");
        if(checkParaNULL(userId)){
            this.noticeService.delAllNotice(userId);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


}
