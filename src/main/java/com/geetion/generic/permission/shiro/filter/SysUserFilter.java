package com.geetion.generic.permission.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysUserFilter extends PathMatchingFilter {
    @Resource
    private UserBaseService userBaseService;
    @Resource
    private ShiroService shiroService;

    //protected static final Logger LOG = LoggerFactory.getLogger(SysUserFilter.class);


    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String token = ((HttpServletRequest) request).getHeader("Authorization");
        //System.out.println("token: " + token);
        UserBase user = null;
        if (token != null) {
            user = userBaseService.getByToken(token);
        }
        if (user != null) {
            shiroService.setLoginUserBase(user);
            return true;
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> params = new HashMap<>();
            params.put("code", 402);
            params.put("message", "访问用户的token错误");
            response.getWriter().append(JSON.toJSONString(params));
            return false;
        }
    }
}
