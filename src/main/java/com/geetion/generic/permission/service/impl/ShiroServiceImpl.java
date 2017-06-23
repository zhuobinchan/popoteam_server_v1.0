package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.permission.shiro.Constants;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jian on 2015/6/17.
 */
@Service("geetionShiroService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ShiroServiceImpl implements ShiroService {


    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;


    @Override
    public Subject getSecurityUtils() {
        return SecurityUtils.getSubject();
    }

    @Override
    public void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }

    @Override
    public Object getSession(Object key) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                return session.getAttribute(key);
            }
        }
        return null;
    }

    @Override
    public Object login(String account, String password) throws AuthenticationException {
        //调用shiro的登录操作
        if (account != null && password != null) {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(account, password, false, "host");
            currentUser.login(token);
            return userBaseService.getByAccount(account);
        }
        return null;
    }

    @Override
    public Object login(String account, String password, String host) throws AuthenticationException {
        //调用shiro的登录操作
        if (account != null && password != null && host != null) {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(account, password, false, host);
            currentUser.login(token);
            return userBaseService.getByAccount(account);
        }
        return null;
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }


    @Override
    public boolean setLoginUser(Object object) {
        if (object != null) {
            setSession(Constants.CURRENT_LOGIN, object);
            return true;
        }
        return false;
    }

    @Override
    public boolean setLoginUserBase(UserBase userBase) {

        if (userBase != null) {
            setSession(Constants.CURRENT_USERBASE, userBase);
            return true;
        }
        return false;
    }

    @Override
    public Object getLoginUser() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            String account = (String) currentUser.getPrincipal();
            if (account != null) {
                Object loginUser = currentUser.getSession().getAttribute(Constants.CURRENT_LOGIN);
                if (loginUser != null)
                    return loginUser;
                else
                    throw new UnknownAccountException();//没找到帐号
            } else
                throw new UnknownAccountException();//没找到帐号
        } else
            throw new UnknownAccountException();//没找到帐号
    }

    @Override
    public UserBase getLoginUserBase() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
//            String account = (String) currentUser.getPrincipal();
//            if (account != null) {
            UserBase loginUser = (UserBase) currentUser.getSession().getAttribute(Constants.CURRENT_USERBASE);
            if (loginUser != null)
                return loginUser;
            else
                throw new UnknownAccountException();//没找到帐号
//            } else
//                throw new UnknownAccountException();//没找到帐号
        } else
            throw new UnknownAccountException();//没找到帐号
    }

    @Override
    public boolean setCurrentType(String currentType) {
        if (currentType != null) {
            setSession(Constants.CURRENT_TYPE, currentType);
            return true;
        }
        return false;
    }

    @Override
    public String getCurrentType() {
        return (String) getSession(Constants.CURRENT_TYPE);
    }

    @Override
    public boolean setCurrentRole(Role role) {
        if (role != null) {
            setSession(Constants.CURRENT_ROLE, role);
            return true;
        }
        return false;
    }

    @Override
    public Role getCurrentRole() {
        return (Role) getSession(Constants.CURRENT_ROLE);
    }
}

