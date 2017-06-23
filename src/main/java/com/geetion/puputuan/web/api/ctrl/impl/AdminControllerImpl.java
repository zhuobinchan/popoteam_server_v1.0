package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.pojo.UserRoleRelative;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.permission.service.UserRoleRelativeService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.model.AdminLoginHis;
import com.geetion.puputuan.pojo.AdminRole;
import com.geetion.puputuan.service.AdminLoginHisService;
import com.geetion.puputuan.service.AdminService;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.AdminController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class AdminControllerImpl extends BaseController implements AdminController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private UserBaseService userBaseService;
    @Resource
    private AdminService adminService;
    @Resource
    private AdminLoginHisService adminLoginHisService;
    @Resource
    private UserRoleRelativeService userRoleRelativeService;
    @Resource
    private TransactionHelper transactionHelper;

    @Override
    public Object register(String account, String password, String nickName, Long[] roles) {
        if (checkParaNULL(account, password)) {

            Map<String, Object> resultMap = new HashMap<>();
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                UserBase userBase = userBaseService.getByAccount(account);
                if (userBase == null) {
                    //插入用户基本信息
                    userBase = new UserBase();
                    userBase.setAccount(account);
                    userBase.setPassword(password);
                    userBaseService.add(userBase);
                    //插入管理员详细信息
                    Admin admin = new Admin();
                    admin.setUserId(userBase.getId());
                    admin.setNickName(account);
                    adminService.addAdmin(admin);

                    Long userId = userBase.getId();
                    addRoleRelative(userId, roles);

                    //查询管理员信息
                    admin = adminService.getAdminByUserId(userBase.getId());
                    resultMap.put("object", admin);
                    /** 提交事务 */
                    transactionHelper.commit(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
                }
                return sendResult(ResultCode.CODE_716.code, ResultCode.CODE_716.msg, null);
            } catch (Exception e) {
                e.printStackTrace();
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object update(@ModelAttribute Admin admin) {
        if (checkParaNULL(admin)) {
            admin.setType(0);
            admin.setId(shiroService.getLoginUserBase().getId());
            adminService.updateAdmin(admin);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updatePassword(String account, String password) {
        if (checkParaNULL(account,password)) {

            userBaseService.updatePassword(account,password);

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchLoginHis(Integer methodType, @ModelAttribute PageEntity pageEntity, @ModelAttribute Admin object, Date loginTimeBegin, Date loginTimeEnd) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("loginTimeBegin", loginTimeBegin);
            param.put("loginTimeEnd", loginTimeEnd);

            PagingResult<AdminLoginHis> pagingForKeyword = null;

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    param.putAll(pojoToMap(object));
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = adminLoginHisService.getAdminLoginHisPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingForKeyword);
                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object search(Integer methodType, @ModelAttribute PageEntity pageEntity, @ModelAttribute Admin object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            PagingResult<Admin> pagingForKeyword = null;

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    param.putAll(pojoToMap(object));
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = adminService.getAdminPage(pageEntity);
                    List<AdminRole> adminRoleList = new ArrayList<>();
                    StringBuffer sb = new StringBuffer();
                    StringBuffer roleIds = new StringBuffer();
                    for(Admin admin : pagingForKeyword.getResultList()){
                        AdminRole ar = new AdminRole();
                        ar.setUserId(admin.getUserId());
                        ar.setAccount(admin.getNickName());
                        ar.setNickName(admin.getNickName());
                        ar.setCreateTime(admin.getCreateTime());
                        sb.setLength(0);
                        roleIds.setLength(0);
                        for(Role r :admin.getRoleList()){
                            sb.append(r.getName()).append(",");
                            roleIds.append(r.getId()).append(",");
                        }
                        if(sb.length() > 0){
                            ar.setRoleName(sb.substring(0, sb.length() - 1));
                            ar.setRoleIds(roleIds.substring(0, roleIds.length() - 1));
                        }
                        adminRoleList.add(ar);
                    }

                    if(null != pagingForKeyword){
                        resultMap.put("list", adminRoleList);
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
    @Transactional
    public Object delete(Long userId) {

        if (checkParaNULL(userId)) {
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userId);
            // 删除登录记录
            adminLoginHisService.deleteAdminLoginBatch(userIdList);

            Long[] userIds = new Long[]{userId};
            // 删除角色
            userRoleRelativeService.deleteBatchByUserId(userIds);

            Admin admin = adminService.getAdminByUserId(userId);
            // 删除管理员
            adminService.removeAdmin(admin.getId());

            // 删除管理员用户
            userBaseService.deleteUserBaseBatch(userIds);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updateRole(Long userId, Long[] roles) {
        if(checkParaNULL(userId)){
            Long[] userIds = new Long[]{userId};
            //先删除用户对应角色
            userRoleRelativeService.deleteBatchByUserId(userIds);
            //添加新的用户角色
            addRoleRelative(userId, roles);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updatePsw(String oldPsw, String newPsw) {

        boolean right = userBaseService.checkOldPassword(shiroService.getLoginUserBase().getAccount(), oldPsw);
        if (right){
            /** 更新蒲蒲团的账户的密码*/
            userBaseService.updatePassword(shiroService.getLoginUserBase().getAccount(), newPsw);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }else {
            return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
        }

    }

    private void addRoleRelative(Long userId, Long[] roles){
        //添加对应用户角色
        if(roles == null){
            return;
        }
        Stream<Long> stream = Arrays.stream(roles);
        stream.forEach(role -> {
            UserRoleRelative userRoleRelative = new UserRoleRelative();
            userRoleRelative.setUserId(userId);
            userRoleRelative.setRoleId(role);
            userRoleRelativeService.add(userRoleRelative);
        });
    }
}
