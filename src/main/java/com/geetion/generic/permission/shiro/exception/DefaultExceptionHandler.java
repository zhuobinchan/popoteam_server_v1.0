package com.geetion.generic.permission.shiro.exception;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.HashMap;

/**
 * Created by virgilyan on 1/13/15.
 */
@ControllerAdvice("permissionDefaultExceptionHandler")
public class DefaultExceptionHandler {
    /**
     * 没有权限 异常
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
        HashMap params = new HashMap<>();
        params.put("code", HttpStatus.UNAUTHORIZED.value());
        params.put("message", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        params.put("exception", e.getMessage());
        return JSON.toJSON(params);
    }
}
