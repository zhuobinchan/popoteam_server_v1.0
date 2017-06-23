package com.geetion.puputuan.web.api.ctrl.impl;


import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.GroupDetailWithCount;
import com.geetion.puputuan.pojo.GroupStatisData;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.EchartsManageController;
import com.geetion.puputuan.web.api.ctrl.GroupManageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class EchartsManageControllerImpl extends BaseController implements EchartsManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private GroupService groupService;
    @Resource
    private RecommendService recommendService;


    @Override
    public Object search(Integer methodType) {
        return null;
    }
}
