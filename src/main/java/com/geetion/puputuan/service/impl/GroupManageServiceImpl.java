package com.geetion.puputuan.service.impl;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.HuanXinSendMessageUtils;
import com.geetion.puputuan.utils.RunMatchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/4/12.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class GroupManageServiceImpl implements GroupManageService {
    protected static final Logger logger = LoggerFactory.getLogger(GroupManageServiceImpl.class);

    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private GroupService groupService;
    @Resource
    private RecommendService recommendService;
    @Resource
    private UserRecommendService userRecommendService;
    @Resource
    private RecommendSuccessService recommendSuccessService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMemberHistoryService groupMemberHistoryService;
    @Resource
    private CommonService commonService;
    @Resource
    private RunMatchUtil runMatchUtil;


    @Override
    public void dissolutionGroup(Long groupId) throws Exception {
        TransactionStatus transactionStatus = transactionHelper.start();

        /** 开启事务 */
        Group group = groupService.getGroupById(groupId);

        try {
            group.setStatus(GroupTypeAndStatus.GROUP_DISSOLUTION);
            group.setModifyTime(new Date());
            groupService.updateGroup(group);
            recommendService.sumGroupRecommend(group.getId());

            //清除成员的userRecommend数据
            Map<String, Object> param = new HashMap<>();
            param.put("groupId", groupId);
            userRecommendService.removeUserRecommendByGroupId(param);

            //清除队伍的recommend success数据
            recommendSuccessService.removeRecommendSuccessByMainGroup(groupId);

            //清除队伍的recommend数据
            recommendService.removeMainIdRecommend(groupId);

//                userRecommendService.removeUserRecommendByGroupId(params);

            String[] groupMembers = commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT);

            Map<String, Object> params = new HashMap<>();
            params.put("groupId", group.getId());
            // 直接从成员表中删除
            groupMemberService.removeGroupMemberByParam(params);
            // 添加用户的群历史记录状态（退出）
//                addGroupMemberHistory(group.getId(), user.getUserId(),
//                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_QUIT);

            // 解散环信群
            HuanXinChatGroupService.deleteChatGroup(group.getRoomId());
            HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
            huanXinMessageExtras.setRoomId(group.getRoomId());
            HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "您好，您所在的队伍已经被队长解散。",
                    HuanXinSendMessageType.GROUP_DISSOLUTION, huanXinMessageExtras,
                    groupMembers);

        } catch (HuanXinMessageException e) {
            logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
            /** 回滚事务 */
            transactionHelper.rollback(transactionStatus);
            throw new HuanXinMessageException();
        } catch (HuanXinChatGroupException e) {
            logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
            /** 回滚事务 */
            transactionHelper.rollback(transactionStatus);
            throw new HuanXinChatGroupException();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
            /** 回滚事务 */
            transactionHelper.rollback(transactionStatus);
            throw new Exception();
        }
        runMatchUtil.cleanRedis(group.getId());
        transactionHelper.commit(transactionStatus);
    }
}
