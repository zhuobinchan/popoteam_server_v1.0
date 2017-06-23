package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.RolePermissionRelative;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.permission.service.RolePermissionRelativeService;
import com.geetion.generic.permission.service.RoleService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.permission.service.UserRoleRelativeService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.pojo.RolePermission;
import com.geetion.puputuan.service.AdminService;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.RoleController;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class RoleControllerImpl extends BaseController implements RoleController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private UserBaseService userBaseService;
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @Resource
    private RolePermissionRelativeService rolePermissionRelativeService;
    @Resource
    private UserRoleRelativeService userRoleRelativeService;
    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            PagingResult<Admin> pagingForKeyword = null;

            switch (methodType) {
                case 1:
                    List<Role> all = roleService.getAll();
                    resultMap.put("list", all);
                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = adminService.getAdminPage(pageEntity);

                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchRolePermissionList(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            PagingResult<Role> pagingForKeyword = null;

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = roleService.getByPagination(pageEntity);
                    List<RolePermission> rolePermissionList = new ArrayList<>();

                    for (Role r : pagingForKeyword.getResultList()){
                        List<Permission> permissionBelongRole = rolePermissionRelativeService.getAllPermissionBelongRole(r);
                        StringBuffer persb = new StringBuffer();
                        StringBuffer idsb = new StringBuffer();

                        for(Permission p : permissionBelongRole){
                            persb.append(p.getName()).append(",");
                            idsb.append(p.getId()).append(",");
                        }

                        RolePermission rp = new RolePermission();
                        rp.setRoleId(r.getId());
                        rp.setRoleName(r.getName());
                        rp.setExtra(r.getExtra());
                        if(idsb.length() > 0){
                            rp.setPermissionIds(idsb.substring(0, idsb.length() - 1));
                        }
                        if(persb.length() > 0){
                            rp.setPermissionList(persb.substring(0, persb.length() - 1));
                        }
                        rolePermissionList.add(rp);
                    }

                    if(null != pagingForKeyword){
                        resultMap.put("list", rolePermissionList);
                        resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                        resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                        resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
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
    public Object addRolePermission(String roleName, String extra, String permissionList) {
        if(checkParaNULL(roleName)){
            Role role = new Role();
            try {
                role.setRole(PinyinHelper.convertToPinyinString(roleName , "", PinyinFormat.WITHOUT_TONE));
            } catch (PinyinException e) {
                e.printStackTrace();
            }
            role.setName(roleName);
            role.setExtra(extra);
            roleService.add(role);

            String[] strings = permissionList.split(",");
            Long[] permissionIdList = new Long[strings.length];
            for(int i = 0; i < strings.length; i++){
                permissionIdList[i] = Long.valueOf(strings[i]);
            }
            rolePermissionRelativeService.addPermissionToRole(permissionIdList, role.getId());
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object delete(Long roleId) {
        if(checkParaNULL(roleId)){
            Role role = roleService.getById(roleId);
            List<Permission> allPermissionBelongRole = rolePermissionRelativeService.getAllPermissionBelongRole(role);
            List<Long> permissionList = new ArrayList<>();
            for(Permission p : allPermissionBelongRole){
                permissionList.add(p.getId());
            }
            if(permissionList.size() > 0){
                rolePermissionRelativeService.removePermissionFromRole(permissionList.toArray(new Long[permissionList.size()]), roleId);
            }

            List<UserBase> allUserByRoleId = userRoleRelativeService.getAllUserByRoleId(roleId);
            List<Long> userList = new ArrayList<>();
            for (UserBase ub : allUserByRoleId){
                userList.add(ub.getId());
            }
            if (userList.size() > 0){
                userRoleRelativeService.deleteRoleBatchByUserId(userList.toArray(new Long[userList.size()]), roleId);
            }

            roleService.remove(roleId);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updateRolePermission(@ModelAttribute RolePermission rolePermission) {
        if(checkParaNULL(rolePermission.getRoleId())){
            Role role = new Role();
            role.setId(rolePermission.getRoleId());
            role.setName(rolePermission.getRoleName());
            role.setExtra(rolePermission.getExtra());
            roleService.update(role);

            List<Permission> allPermissionBelongRole = rolePermissionRelativeService.getAllPermissionBelongRole(role);
            List<Long> permissionList = new ArrayList<>();
            for(Permission p : allPermissionBelongRole){
                permissionList.add(p.getId());
            }

            if(permissionList.size() > 0){
                rolePermissionRelativeService.removePermissionFromRole(permissionList.toArray(new Long[permissionList.size()]), role.getId());
            }

            if(rolePermission.getPermissionIds() != null){
                String[] strings = rolePermission.getPermissionIds().split(",");
                Long[] newPermissionList = new Long[strings.length];
                for (int i = 0; i < strings.length; i++){
                    newPermissionList[i] = Long.valueOf(strings[i]);
                }

                rolePermissionRelativeService.addPermissionToRole(newPermissionList, role.getId());
            }

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
}
