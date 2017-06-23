package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单权限  接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/permission")
public interface PermissionController {


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, @ModelAttribute PageEntity pageEntity);
    
}
