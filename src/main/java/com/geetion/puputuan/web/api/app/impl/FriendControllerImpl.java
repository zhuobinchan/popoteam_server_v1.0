package com.geetion.puputuan.web.api.app.impl;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.model.jsonModel.JSONUserBase;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.app.FriendController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class FriendControllerImpl extends BaseController implements FriendController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private FriendDeleteService friendDeleteService;
    @Resource
    private MessageService messageService;
    @Resource
    private TransactionHelper transactionHelper;

    @Resource
    private OssFileUtils ossFileUtils;


    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object,
                         Boolean orderByFirstLetter) {
        logger.info("FriendControllerImpl search begin...");
        if (checkParaNULL(methodType, orderByFirstLetter, object)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            List<JSONUserBase> jsonList = null;


//            param.putAll(pojoToMap(object));
            //如果identify不传，则为查询我的好友
            if (object.getIdentify() == null && object.getPhone() == null) {
                param.put("userId", shiroService.getLoginUserBase().getId());
            } else {
                param.put("identify", object.getIdentify());
                User user = userService.getUser(param);

                //如果找不到好友,则返回用户不存在错误
                if (null == user) {
                    return sendResult(ResultCode.CODE_719.code, ResultCode.CODE_719.msg, resultMap);
                } else {
                    param.clear();
                    param.put("userId", user.getId());
                }
            }

            switch (methodType) {
                case 1:
                    List<User> list = null;
                    if (orderByFirstLetter) {
                        //根据首字母分页
                        list = friendRelationshipService.getUserOrderByFirstPinyin(param);
                    } else {
                        //根据添加时间分页
                        list = friendRelationshipService.getFriendRelationshipWithUser(param);
                    }
                    //获取用户头像
                    ossFileUtils.getUserHeadList(list,null);

                    jsonList = convertDBListToJSONList(list);
                    resultMap.put("list", jsonList);
                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<User> pagingForKeyword = null;
                    if (orderByFirstLetter) {
                        //根据首字母分页
                        pagingForKeyword = friendRelationshipService.getWithUserOrderByFirstPinyinPage(pageEntity);
                    } else {
                        //根据添加时间分页
                        pagingForKeyword = friendRelationshipService.getFriendRelationshipWithUserPage(pageEntity);
                    }
                    //获取图片
                    ossFileUtils.getUserHeadPage(pagingForKeyword,null);

                    jsonList = convertDBListToJSONList(pagingForKeyword.getResultList());
                    resultMap.put("list", jsonList);
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
    public Object applyFriend(String identify) {
        logger.info("FriendControllerImpl applyFriend begin...");
        if (checkParaNULL(identify)) {
            Map<String, Object> param = new HashMap<>();
            param.put("identify", identify);
            User friend = userService.getUser(param);

            //添加的好友不存在
            if (null == friend) {
                return sendResult(ResultCode.CODE_721.code, ResultCode.CODE_721.msg, null);
            }

            try {
                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());

                FriendApply friendApply = addOrUpdateApplyRecord(user, friend);

                if (friendApply == null) {
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }

                int result = checkIfApplySuccess(user, friend, friendApply);

                switch (result) {
                    case FriendApplyType.APPLY_SUCCESS:
                        // 给对方发推送消息
                        JPushUtils.sendAgreeApplyOrNotJPush("好友申请结果", user.getNickName() + "已同意"  + "您的好友请求",
                                JPushType.FRIEND_APPLY_RESULT, Long.valueOf(user.getIdentify()), friend.getUserBase().getAccount());
                        // 给自己发送推送消息
                        JPushUtils.sendAgreeApplyOrNotJPush("好友申请结果", friend.getNickName() + "已同意"  + "您的好友请求",
                                JPushType.FRIEND_APPLY_RESULT, Long.valueOf(friend.getIdentify()), user.getUserBase().getAccount());
                        break;

                    case FriendApplyType.FRIEND_APPLY:
                        //添加JPush推送 将申请的id发送过去
                        JPushUtils.sendFriendApplyJPush("好友申请", user.getNickName() + "申请添加好友",
                                JPushType.FRIEND_APPLY, friendApply.getId(), friend.getUserBase().getAccount());
                        break;

                    case FriendApplyType.DUPLICATE_APPLY:
                        break;

                    default:
                        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }catch (Exception e){
                logger.error("FriendControllerImpl applyFriend error " + e);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    private FriendApply addOrUpdateApplyRecord(User user, User friend) {
        Map<String, Object> param = new HashMap<>();

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();
        FriendApply friendApply;
        try {
            param.put("userId", shiroService.getLoginUserBase().getId());
            param.put("friendId", friend.getUserId());
            param.put("type", FriendApplyType.SENDING);
            // 查询当前用户是否发过好友申请
            friendApply = friendApplyService.getFriendApply(param);
            if (friendApply != null) {
                param.clear();
                param.put("userId", friend.getUserId());
                param.put("content", user.getNickName() + "申请添加好友");
                Message message = messageService.getMessage(param);
                if (message != null) {
                    message.setCreateTime(new Date());
                    message.setIsRead(false);
                    messageService.updateMessage(message);
                }else{
                    //当好友申请消息被删除时，需要添加新的申请消息
                    //发送通知消息
                    addFriendMessage(friend.getUserId(), user.getNickName() + "申请添加好友",
                            MessageType.FRIEND_APPLY, friendApply.getId(), user);

                }
            } else {
                friendApply = new FriendApply();
                //userId 为好友id
                friendApply.setUserId(shiroService.getLoginUserBase().getId());
                friendApply.setFriendId(friend.getUserId());

                //添加好友申请
                friendApplyService.addFriendApply(friendApply);

                //发送通知消息
                addFriendMessage(friend.getUserId(), user.getNickName() + "申请添加好友",
                        MessageType.FRIEND_APPLY, friendApply.getId(), user);
            }

            /** 提交事务 */
            transactionHelper.commit(transactionStatus);

        } catch (Exception e) {
            logger.error("FriendControllerImpl addOrUpdateApplyRecord error " + e);
            transactionHelper.rollback(transactionStatus);
            return null;
        }

        return friendApply;
    }

    private int checkIfApplySuccess(User user, User friend, FriendApply myFriendApply) {
        Map<String, Object> param = new HashMap<>();

        /** 开启事务 */
        TransactionStatus transactionStatus = transactionHelper.start();

        try {
            //我的信息
            param.clear();
            param.put("userId", friend.getUserId());
            param.put("friendId", user.getUserId());
            param.put("type", FriendApplyType.SENDING);
            // 查询对方用户是否发送过好友申请
            FriendApply friendHaveApply = friendApplyService.getFriendApply(param);
            // 当用户发送好友申请时，对方也发起过好友申请时，直接互为好友
            if(friendHaveApply != null) {
                logger.info("FriendControllerImpl applyFriend friendHaveApply is not null");

                //添加互为好友关系
                List<FriendRelationship> list = new ArrayList<>();
                FriendRelationship friendRelationship = new FriendRelationship();
                friendRelationship.setUserId(friendHaveApply.getFriendId());
                friendRelationship.setFriendId(friendHaveApply.getUserId());
                friendRelationship.setType(FriendType.FROM_SEARCH);
                list.add(friendRelationship);

                FriendRelationship userfriendRelationship = new FriendRelationship();
                userfriendRelationship.setUserId(friendHaveApply.getUserId());
                userfriendRelationship.setFriendId(friendHaveApply.getFriendId());
                userfriendRelationship.setType(FriendType.FROM_SEARCH);
                list.add(userfriendRelationship);
                friendRelationshipService.addFriendRelationshipBatch(list);

                // 更新更新对方的好友申请类型
                friendHaveApply.setType(FriendApplyType.AGREE);
                friendApplyService.updateFriendApply(friendHaveApply);

                // 更新用户的好友申请，状态为已读
                myFriendApply.setType(FriendApplyType.AGREE);
                friendApplyService.updateFriendApply(myFriendApply);

                //更新双方的friendUpdateTime时间,使app刷新好友列表
                user.setFriendUpdateTime(new Date());
                friend.setFriendUpdateTime(new Date());
                userService.updateUserByUserId(user);
                userService.updateUserByUserId(friend);

                // 取得对方用户发起的好友申请消息，设置为已互为好友
                param.clear();
                param.put("userId", user.getUserId());
                param.put("content", friend.getNickName() + "申请添加好友");
                Message friendMessage = messageService.getMessage(param);
                if (friendMessage != null) {
                    friendMessage.setType(MessageType.FRIEND_APPLY_RESULT);
                    friendMessage.setContent("已添加好友");
                    friendMessage.setIsRead(true);
                    messageService.updateMessage(friendMessage);
                } else {
                    //当好友申请消息被删除时，需要添加新的申请消息
                    //发送通知消息
                    addFriendMessage(user.getUserId(), "已添加好友",
                            MessageType.FRIEND_APPLY_RESULT, friendHaveApply.getId(), friend);

                }

                // 取得当前用户发起的好友申请消息，设置为已互为好友
                param.clear();
                param.put("userId", friend.getUserId());
                param.put("content", user.getNickName() + "申请添加好友");
                Message myMessage = messageService.getMessage(param);
                if (myMessage != null) {
                    myMessage.setType(MessageType.FRIEND_APPLY_RESULT);
                    myMessage.setContent("已添加好友");
                    myMessage.setIsRead(true);
                    messageService.updateMessage(myMessage);
                } else {
                    //当好友申请消息被删除时，需要添加新的申请消息
                    //给对方发送互为好友的消息
                    addFriendMessage(friend.getUserId(), "已添加好友",
                            MessageType.FRIEND_APPLY_RESULT, myFriendApply.getId(), user);
                }

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
            } else {
                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
                return FriendApplyType.FRIEND_APPLY;
            }
        } catch (DuplicateKeyException e) {
            logger.error("FriendControllerImpl checkIfApplySuccess DuplicateKeyException " + e);
            transactionHelper.rollback(transactionStatus);
            return FriendApplyType.DUPLICATE_APPLY;
        } catch (Exception e) {
            logger.error("FriendControllerImpl checkIfApplySuccess error " + e);
            transactionHelper.rollback(transactionStatus);
            return FriendApplyType.EXCEPTION;
        }

        return FriendApplyType.APPLY_SUCCESS;
    }

    @Deprecated
    @Override
    public Object searchApply(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            switch (methodType) {
                case 1:
                    param.put("friendId", shiroService.getLoginUserBase().getId());
                    List<FriendApply> list = friendApplyService.getFriendApplyList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        FriendApply obj = friendApplyService.getFriendApplyById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
//                    if (pageEntity != null)
//                        pageEntity.setParam(pojoToMap(object));
                    PagingResult<FriendApply> pagingForKeyword = friendApplyService.getFriendApplyPage(pageEntity);
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


    // applyFriendLock 并发锁
//    private static Object agreeApplyOrNotLock=new Object();

    @Override
    public Object agreeApplyOrNot(Long messageId, Long friendApplyId, Boolean isAgree) {
        logger.info("FriendControllerImpl agreeApplyOrNot begin...");
        if (checkParaNULL(messageId, friendApplyId, isAgree)) {
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                FriendApply friendApply = friendApplyService.getFriendApplyById(friendApplyId);
                if (friendApply == null) {
                    return sendResult(ResultCode.CODE_715.code, ResultCode.CODE_715.msg, null);
                }

                if (isAgree) {
                    //添加好友关系
                    List<FriendRelationship> list = new ArrayList<>();
                    FriendRelationship friendRelationship = new FriendRelationship();
                    friendRelationship.setUserId(friendApply.getUserId());
                    friendRelationship.setFriendId(friendApply.getFriendId());
                    //表示好友的来源是通过搜索Id加的
                    friendRelationship.setType(FriendType.FROM_SEARCH);
                    list.add(friendRelationship);
                    friendRelationship = new FriendRelationship();
                    friendRelationship.setUserId(friendApply.getFriendId());
                    friendRelationship.setFriendId(friendApply.getUserId());
                    //表示好友的来源是通过搜索Id加的
                    friendRelationship.setType(FriendType.FROM_SEARCH);
                    list.add(friendRelationship);
                    boolean result = friendRelationshipService.addFriendRelationshipBatch(list);
                    //如果添加失败，则不发送消息
                    if (!result) {
                        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                    }
                }

                //更新好友请求
                friendApply.setType(isAgree ? FriendApplyType.AGREE : FriendApplyType.REJECT);
                boolean updateResult = friendApplyService.updateFriendApply(friendApply);

                //我的信息（由于是我同意别人的请求，所以userId是对方，friendId才是我）
                User user = userService.getByUserId(friendApply.getFriendId());

                //好友信息
                User friend = userService.getByUserId(friendApply.getUserId());

                //更新User表的friendUpdateTime时间,使app刷新好友列表
                user.setFriendUpdateTime(new Date());
                friend.setFriendUpdateTime(new Date());
                userService.updateUserByUserId(user);
                userService.updateUserByUserId(friend);

                //对方收到的信息
                boolean addMessageResult = addFriendMessage(friendApply.getUserId(),
                        (isAgree ? "已添加好友" : "已拒绝"), MessageType.FRIEND_APPLY_RESULT,
                        friendApply.getId(), user);

                //我的信息，将消息内容更改
                Message message = messageService.getMessageById(messageId);
                message.setType(MessageType.FRIEND_APPLY_RESULT);
                message.setContent((isAgree ? "已添加好友" : "已拒绝"));
                message.setIsRead(true);
                boolean updateMessageResult = messageService.updateMessage(message);

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);

                User loginUser = userService.getByUserId(shiroService.getLoginUserBase().getId());

                //添加jpush推送
                JPushUtils.sendAgreeApplyOrNotJPush("好友申请结果", user.getNickName() + (isAgree ? "已同意" : "已拒绝") + "您的好友请求",
                        JPushType.FRIEND_APPLY_RESULT, Long.valueOf(loginUser.getIdentify()), friend.getUserBase().getAccount());

                if (updateResult && addMessageResult && updateMessageResult) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }

            } catch (DuplicateKeyException e) {
                logger.error("FriendControllerImpl agreeApplyOrNot error " + e);
                transactionHelper.rollback(transactionStatus);
                if (e.getMessage().contains("uq_friend_friend_relationship_ref_user")) {
                    logger.error("FriendControllerImpl agreeApplyOrNot DuplicateKeyException 已经是好友 ");
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
            } catch (Exception e) {
                logger.error("FriendControllerImpl agreeApplyOrNot error " + e);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object deleteFriend(String identify) {
        logger.info("FriendControllerImpl deleteFriend begin...");
        if (checkParaNULL(identify)) {
            Map<String, Object> param = new HashMap<>();
            param.put("identify", identify);
            User friend = userService.getUser(param);

            if (null == friend) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }

            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                //好友表里每个好友关系都是两条记录，两天都要删除
                param.clear();
                param.put("userId", shiroService.getLoginUserBase().getId());
                param.put("friendId", friend.getUserId());

                //如果有好友记录,删除记录并更新friendUpdateTime
                if (friendRelationshipService.getFriendRelationship(param) != null) {
                    friendRelationshipService.removeFriendRelationshipBetween(param);

                    FriendDelete friendDelete = new FriendDelete();
                    friendDelete.setUserId(shiroService.getLoginUserBase().getId());
                    friendDelete.setFriendId(friend.getUserId());
                    friendDeleteService.addFriendDelete(friendDelete);

                    //好友列表变更,需要刷新双方的friendUpdateTime
                    User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                    user.setFriendUpdateTime(new Date());
                    userService.updateUserByUserId(user);

                    friend.setFriendUpdateTime(new Date());
                    userService.updateUserByUserId(friend);

                    //添加jpush推送
                    JPushUtils.sendFriendDeleteJPush("对方解除好友关系", user.getNickName()  + "解除了与您的好友关系",
                            JPushType.FRIEND_DELETE, user.getIdentify(), friend.getUserBase().getAccount());
                } else {
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }

                /** 提交事务 */
                transactionHelper.commit(transactionStatus);
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);

            } catch (Exception e) {
                logger.error("FriendControllerImpl deleteFriend error " + e);
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Deprecated
    @Override
    public Object isMyFriend(Long userId) {
        if (checkParaNULL(userId)) {

            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            //查询是否是好友
            param.put("userId", userId);
            param.put("friendId", shiroService.getLoginUserBase().getId());
            FriendRelationship friendRelationship = friendRelationshipService.getFriendRelationship(param);
            if (friendRelationship != null) {
                resultMap.put("isFriend", true);
            } else {
                resultMap.put("isFriend", false);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    /**
     * 添加消息进数据库
     *
     * @param userId        要接受消息的用户userId
     * @param content       消息内容
     * @param type          {@link MessageType}
     * @param friendApplyId 好友申请的id
     * @param user          用户对象
     * @return 是否添加成功
     */
    private boolean addFriendMessage(Long userId, String content, int type, Long friendApplyId, User user) {

        Message message = new Message();
        //要接受消息的用户
        message.setUserId(userId);
        message.setContent(content);
        message.setType(type);

        //消息额外参数
        MessageParam messageParam = new MessageParam();
        messageParam.setFriendApplyId(friendApplyId);
        messageParam.setAnnouncementContent(null);
        ossFileUtils.getUserHead(user, null);
        JSONUserBase userBase = ConvertBeanUtils.convertDBModelToJSONModel(user);
        userBase.setNickName(EmojiCharacterUtil.emojiConvert1(userBase.getNickName()));
        messageParam.setUserBase(userBase);
        message.setParam(messageParam.toJSONString());
        //添加消息
        return messageService.addMessage(message);
    }



    private List<JSONUserBase> convertDBListToJSONList(List<User> dbList) {
        List<JSONUserBase> jsonUserBases = new ArrayList<>();

        if (dbList != null && dbList.size() != 0) {
            for (User user : dbList) {
                JSONUserBase jsonModel = ConvertBeanUtils.convertDBModelToJSONModel(user);
                jsonUserBases.add(jsonModel);
            }
            return jsonUserBases;
        }
        return null;
    }
}
