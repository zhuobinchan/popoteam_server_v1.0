package com.geetion.puputuan.aop;

import com.geetion.generic.userbase.service.UserBaseService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by yoan on 16/3/11.
 */
@Aspect
public class OperationLogAop {

    private final static String OPERATE_METHOD = "POST";

    private final static String LOGIN_OPERATION_DESCRIPTION = "%s 于 %s 登陆";
    private final static String OPERATION_DESCRIPTION = "%s 执行了 %s%s 操作";

    private final static String LOGIN_METHOD = "login";//用户登陆的方法名

    @Autowired
    private HttpServletRequest request;
    @Resource
    private UserBaseService userBaseService;

    @AfterReturning(value = "execution(* com.geetion.puputuan.web.api.*.*(..)))", argNames = "jp, rtv", returning = "rtv")
    public void afterReturning(JoinPoint jp, Object rtv) {

    }

}
