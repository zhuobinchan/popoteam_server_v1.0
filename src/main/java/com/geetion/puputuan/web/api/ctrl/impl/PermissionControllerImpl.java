package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.RolePermissionRelative;
import com.geetion.generic.permission.service.PermissionService;
import com.geetion.generic.permission.service.RolePermissionRelativeService;
import com.geetion.generic.permission.service.RoleService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.pojo.RolePermission;
import com.geetion.puputuan.service.AdminService;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.PermissionController;
import com.geetion.puputuan.web.api.ctrl.RoleController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class PermissionControllerImpl extends BaseController implements PermissionController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;
    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            PagingResult<Permission> pagingForKeyword = null;

            switch (methodType) {
                case 1:
                    List<Permission> all = permissionService.getAll();
                    resultMap.put("list", all);
                    break;
                case 2:

                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


}
