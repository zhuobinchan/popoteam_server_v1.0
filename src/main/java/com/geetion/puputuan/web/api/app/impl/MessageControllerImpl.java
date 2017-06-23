package com.geetion.puputuan.web.api.app.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.MessageType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Announcement;
import com.geetion.puputuan.model.Message;
import com.geetion.puputuan.service.AnnouncementService;
import com.geetion.puputuan.service.MessageService;
import com.geetion.puputuan.utils.EmojiCharacterUtil;
import com.geetion.puputuan.web.api.app.MessageController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.persistence.TableGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class MessageControllerImpl extends BaseController implements MessageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private MessageService messageService;
    @Resource
    private AnnouncementService announcementService;


    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        logger.info("MessageControllerImpl search begin...");
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("userId", shiroService.getLoginUserBase().getId());
            //查询对用户可见的消息（这里是针对公告类型的消息）
            param.put("status", MessageType.CAN_READ);
            switch (methodType) {
                case 1:
                    List<Message> list = messageService.getMessageList(param);
                    for (Message mes : list){
                        mes.setContent(EmojiCharacterUtil.emojiRecovery2(mes.getContent()));
                    }
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Message> pagingForKeyword = messageService.getMessagePage(pageEntity);
                    for (Message mes : pagingForKeyword.getResultList()){
                        mes.setContent(EmojiCharacterUtil.emojiRecovery2(mes.getContent()));
                        mes.setParam(EmojiCharacterUtil.emojiRecovery2(mes.getParam()));
                    }
                    if(null != pagingForKeyword){
                        if (pagingForKeyword.getCurrentPage() == pageEntity.getPage()){
                            resultMap.put("list", pagingForKeyword.getResultList());
                            resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                            resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                            resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                        }
                    }
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
        logger.info("MessageControllerImpl setRead begin...");
        if (checkParaNULL(id)) {
            Message message = new Message();
            message.setId(id);
            message.setIsRead(true);
            boolean result = messageService.updateMessage(message);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object unReadNum() {
        logger.info("MessageControllerImpl unReadNum begin...");
        Message message = new Message();
        message.setIsRead(false);
        message.setUserId(shiroService.getLoginUserBase().getId());

        //计算用户有几条未读的信息
        int unReadNum = messageService.countMessageNum(message);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("unReadNum", unReadNum);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchAnnouncement(Long id) {
        logger.info("MessageControllerImpl searchAnnouncement begin...");
        if (checkParaNULL(id)) {
            Map<String, Object> resultMap = new HashMap<>();
            Announcement announcement = announcementService.getAnnouncementById(id);
            resultMap.put("object", announcement);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delMsg(Long id) {
        logger.info("MessageControllerImpl delMsg begin...");
        if(checkParaNULL(id)) {
            this.messageService.delMsg(id);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delMulMsg(List<Long> ids) {
        logger.info("MessageControllerImpl delMulMsg begin...");
        if(checkParaNULL(ids)) {
            this.messageService.delMulMsg(ids);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object delAllMsg(Long userId) {
        logger.info("MessageControllerImpl delAllMsg begin...");
        if(checkParaNULL(userId)){
            this.messageService.delAllMsg(userId);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


}
