package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.GroupMemberWithSign;
import com.geetion.puputuan.pojo.GroupWithNumberList;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.app.ActivityController;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.DateManageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class DateManageControllerImpl extends BaseController implements DateManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private UserBaseService userBaseService;
    @Resource
    private GroupService groupService;
    @Resource
    private CommonService commonService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private ActivityController activityController;
    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private OssFileUtils ossFileUtils;


    @Override
    public Object searchGroup(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, Long userId,
                              @ModelAttribute Group object, Date groupTimeBegin, Date groupTimeEnd, String number) {

        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("groupTimeBegin", groupTimeBegin);
            param.put("groupTimeEnd", groupTimeEnd);
            param.put("number", number);
            param.put("userId", userId);

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<GroupWithNumberList> list = groupService.getGroupWithNumberList(param);
                    //获得头像
                    if (list != null && list.size() > 0) {
                        for (GroupWithNumberList groupWithNumberList : list) {
                            List<GroupMember> groupMemberList = commonService.getGroupMember(groupWithNumberList.getId());
                            ossFileUtils.getGroupWithMemberImageList(groupWithNumberList, groupMemberList, null);
                        }
                    }
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        param.clear();
                        param.put("id", id);
                        GroupWithNumberList groupWithNumberList = groupService.getGroupWithSignNumber(param);
                        param.clear();
                        param.put("groupId", id);
                        List<GroupMemberWithSign> groupMemberList = groupMemberService.getGroupMemberWithSign(param);

                        //获取用户的头像
                        if (groupMemberList != null && groupMemberList.size() != 0) {
                            for (GroupMemberWithSign groupMemberWithSign : groupMemberList) {
                                if (groupMemberWithSign.getUser() != null)
                                    groupMemberWithSign.getUser().setHead(
                                            ossFileUtils.getPictures(groupMemberWithSign.getUser().getHeadId(), null));
                            }
                        }
//                        ossFileUtils.getUserHeadGroupMemberList(groupMemberList, null);

                        resultMap.put("group", groupWithNumberList);
                        resultMap.put("groupMemberList", groupMemberList);

                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<GroupWithNumberList> pagingForKeyword = groupService.getGroupWithNumberPage(pageEntity);
                    //获得头像
                    if (pagingForKeyword != null && pagingForKeyword.getResultList().size() > 0) {
                        for (GroupWithNumberList groupWithNumberList : pagingForKeyword.getResultList()) {
                            List<GroupMember> groupMemberList = commonService.getGroupMember(groupWithNumberList.getId());
                            ossFileUtils.getGroupWithMemberImageList(groupWithNumberList, groupMemberList, null);
                        }
                    }
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
    public Object searchActivity(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, Long userId,
                                 @ModelAttribute Group object, Date groupTimeBegin, Date groupTimeEnd, String number) {
        return null;
    }
}
