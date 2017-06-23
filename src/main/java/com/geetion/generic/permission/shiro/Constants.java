package com.geetion.generic.permission.shiro;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class Constants {

    /* 当前用户 */
    public static final String CURRENT_USER = "user";
    /* 用这个字段存放当前用户的基本信息 */
    public static final String CURRENT_USERBASE = "userBase";
    /* 表示当前是管理员登录 */
    public static final String CURRENT_ADMIN = "admin";
    /* 用这个字段存放当前用户的详细信息 */
    public static final String CURRENT_LOGIN = "login";
    /* 当前登录的角色 */
    public static final String CURRENT_ROLE = "role";
    /* 当前用户登录的类型，是普通用户还是管理员 */
    public static final String CURRENT_TYPE = "type";
    public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
}
