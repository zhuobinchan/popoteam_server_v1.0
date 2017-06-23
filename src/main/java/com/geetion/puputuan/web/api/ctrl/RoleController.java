package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.pojo.RolePermission;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 角色 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/role")
public interface RoleController {

    /**
     * 查询角色权限
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/searchRolePermissionList", method = RequestMethod.GET)
    @ResponseBody
    Object searchRolePermissionList(Integer methodType, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/addRolePermission", method = RequestMethod.POST)
    @ResponseBody
    Object addRolePermission(String roleName, String extra , String permissionList);

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object delete(Long roleId);

    @RequestMapping(value = "/updateRolePermission", method = RequestMethod.POST)
    @ResponseBody
    Object updateRolePermission(@ModelAttribute RolePermission rolePermission);
}
