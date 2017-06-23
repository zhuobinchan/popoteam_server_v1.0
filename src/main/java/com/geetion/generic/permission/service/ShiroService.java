package com.geetion.generic.permission.service;


import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.userbase.pojo.UserBase;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 * Shiro服务，包含了所有认证跟session的API
 * Created by jian on 2015/6/17.
 */
public interface ShiroService {


    /**
     * 获得Shiro的SecurityUtils
     *
     * @return
     */
    public Subject getSecurityUtils();

    /**
     * 设置Shiro的session
     *
     * @return
     */
    public void setSession(Object key, Object value);

    /**
     * 获得Shiro的session
     *
     * @return
     */
    public Object getSession(Object key);


    /**
     * 将登录操作委托为Shiro进行操作
     *
     * @param account
     * @param password
     * @return 返回登录成功的用户对象，若登录失败，则返回null
     */
    public Object login(String account, String password) throws AuthenticationException;

    /**
     * 将登录操作委托为Shiro进行操作
     *
     * @param account
     * @param password
     * @param host 登录的是前台还是后台用户
     * @return 返回登录成功的用户对象，若登录失败，则返回null
     */
    public Object login(String account, String password, String host) throws AuthenticationException;


    /**
     * 登出操作
     */
    public void logout();


    /**
     * 设置当前登录的用户
     */
    public boolean setLoginUser(Object object);


    /**
     * 设置当前登录的用户基本信息
     */
    public boolean setLoginUserBase(UserBase userBase);


    /**
     * 获得当前登录的用户
     */
    public Object getLoginUser();


    /**
     * 获得当前登录的用户基本信息
     */
    public UserBase getLoginUserBase();


    /**
     * 设置当前登录的用户是前台还是后台
     */
    public boolean setCurrentType(String currentType);


    /**
     * 获得当前登录的用户类型，是前台还是后台
     */
    public String getCurrentType();


    /**
     * 设置当前登录的用户的角色（默认是单一角色）
     */
    public boolean setCurrentRole(Role role);


    /**
     * 获得当前登录的用户的角色（默认是单一角色）
     */
    public Role getCurrentRole();


}

