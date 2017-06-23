package com.geetion.generic.permission.shiro.filter;

import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by virgilyan on 1/21/15.
 */
public class ShiroTimeoutFilter extends AdviceFilter {


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            return true;
        } else {
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(200);
            response.getWriter().write("{\"code\":402,\"message\":\"会话超时\"}");
            response.getWriter().flush();
            return false;
        }
    }


}