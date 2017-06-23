package com.geetion.generic.permission.shiro.realm;

import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.service.PermissionService;
import com.geetion.generic.permission.service.RolePermissionRelativeService;
import com.geetion.generic.permission.service.RoleService;
import com.geetion.generic.permission.service.UserRoleRelativeService;
import com.geetion.generic.permission.shiro.Constants;
import com.geetion.generic.permission.utils.PasswordHelper;
import com.geetion.generic.permission.utils.UUIDUtils;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by virgilyan on 12/27/14.
 */
public class UserVerifyRealm extends AuthorizingRealm {

    @Resource(name = "geetionPermissionUUIDUtils")
    private UUIDUtils permissionUUIDUtils;

    @Resource(name = "geetionPermissionPasswordHelper")
    private PasswordHelper permissionPasswordHelper;

    @Resource
    private UserBaseService userBaseService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleRelativeService userRoleRelativeService;

    @Resource
    private RolePermissionRelativeService rolePermissionRelativeService;

    /**
     * 赋予登录用户角色和权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()
//        String account = (String) super.getAvailablePrincipal(principals);
        System.out.println("赋予权限：" + ReflectionToStringBuilder.toString(principals, ToStringStyle.MULTI_LINE_STYLE));
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();

//        根据账户名和密码从数据库获取用户
        UserBase userBase = userBaseService.getByAccount(principals.getPrimaryPrincipal().toString());
        if (null == userBase) {
            throw new AuthorizationException();
        }

        /**
         * 根据session存放的type判断是前台用户还是后台用户，
         * 1.若是前台user，则根据登录时指定的角色获得权限
         * 2.若是后台admin，则从数据库里获得管理员的所有角色的所有权限
         * 这样做的理由是：前台每个用户可以动态指定不同的角色，而后台只有唯一一个角色
         */

        //前台
        if(getSession(Constants.CURRENT_TYPE).equals(Constants.CURRENT_USER)){
            //从session中获取角色，根据角色的名字获取权限
//            GeetionRole geetionRole = geetionRoleService.getByRole((String) getSession(Constants.CURRENT_ROLE));
            Role role = roleService.getById(((Role) getSession(Constants.CURRENT_ROLE)).getId());
            roleList.add(role.getRole());
            System.out.println("\nuser id " + userBase.getId() + "  have role : " + role.getRole() + "  role Id " + role.getId());
            List<Permission> permissionRecords = rolePermissionRelativeService.getAllPermissionBelongRole(role);

            //添加权限
            if (null != permissionRecords && permissionRecords.size() > 0) {
                for (Permission permission : permissionRecords) {
                    permissionList.add(permission.getPermission());
                    System.out.println("\nuser id " + userBase.getId() + "  have permission : " + permission.getPermission() + "  permission id  " + permission.getId());
                }
            }
        }else {
            //后台
            //获取该用户的角色
            List<Role> roleRecords = userRoleRelativeService.getRoleByUserId(userBase.getId());

            //实体类User中包含有用户角色的实体类信息
            if (null != roleRecords && roleRecords.size() > 0) {
                for (Role role : roleRecords) {
                    //添加角色
                    roleList.add(role.getRole());
                    System.out.println("\nuser id " + userBase.getId() + "  have role : " + role.getRole() + "  role Id " + role.getId());
                    //根据角色查询所有的权限
                    List<Permission> permissionRecords = rolePermissionRelativeService.getAllPermissionBelongRole(role);
                    //添加权限
                    if (null != permissionRecords && permissionRecords.size() > 0) {
                        for (Permission permission : permissionRecords) {
                            //如果permissionList没有这个权限，则添加进去
                            if(!permissionList.contains(permission.getPermission())) {
                                permissionList.add(permission.getPermission());
                                System.out.println("\nuser id " + userBase.getId() + "  have permission : " + permission.getPermission() + "  permission id  " + permission.getId());
                            }
                        }
                    }
                }
            } else {
                System.out.println("\nrole is null\n");
            }
        }

//        为当前用户设置角色和权限
        if (roleList.size() > 0 && permissionList.size() > 0) {
            SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
            simpleAuthorInfo.addRoles(roleList);
            simpleAuthorInfo.addStringPermissions(permissionList);
            return simpleAuthorInfo;
        }
        //若该方法什么都不做直接返回null的话,就会导致任何用户访问/admin/listUser.jsp时都会自动跳转到unauthorizedUrl指定的地址
        //详见applicationContext.xml中的<quartz id="shiroFilter">的配置
        return null;
    }

    /**
     * 验证登录用户资格
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException UsernameNotFoundException 用户找不到
     *                                 BadCredentialsException 坏的凭据
     *                                 AccountStatusException 用户状态异常它包含如下子类
     *                                 AccountExpiredException 账户过期
     *                                 LockedException 账户锁定
     *                                 DisabledException 账户不可用
     *                                 CredentialsExpiredException 证书过期
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个authcToken是从LoginController里面currentUser.add(token)传过来的
        //两个token的引用都是一样的
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        System.out.println("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        //此处无需比对,比对的逻辑Shiro会做,我们只需返回一个和令牌相关的正确的验证信息
        //说白了就是第一个参数填登录用户名,第二个参数填合法的登录密码(可以是从数据库中取到的,本例中为了演示就硬编码了)
        //这样一来,在随后的登录页面上就只有这里指定的用户和密码才能通过验证

        //根据账户名和密码从数据库获取用户
        UserBase userBase = userBaseService.getByAccount(token.getPrincipal().toString());

        if (null == userBase) {
            throw new UnknownAccountException();//没找到帐号
        } else if (userBase.getFreeze() == 1) {
            throw new LockedAccountException();//帐号锁定
        }

        //设置token及登录时间
        if (token.getHost().equals("ctrl")) {
            userBase.setToken(permissionUUIDUtils.getUUID().replaceAll("-", ""));
            userBase.setLoginTime(new Date());
            userBaseService.updateWithoutPassword(userBase);
        } else if (token.getHost().equals("app")) {
            //用户token在登录注册时由loginController内的接口设置,
            //这里不另外设置,避免调用Shiro的login接口导致token变化没有反应到返回的JSON消息中
//            userBase.setToken(permissionUUIDUtils.getUUID().replaceAll("-", ""));
            userBase.setLoginTime(new Date());
            userBaseService.updateWithoutPassword(userBase);
        }

        //TODO 唔知系乜嘢
        AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(
                userBase.getAccount(),
                userBase.getPassword(),
                ByteSource.Util.bytes(userBase.getAccount()),
                userBase.getAccount()
        );

        //清空用户的缓存
        setAuthenticationCachingEnabled(false);

        //如果是后台用户，则加入缓存
        if (token.getHost().equals("ctrl")) {
//            this.setSession(Constants.CURRENT_USER, geetionUserBase);

            //开启用户缓存，将整个用户对象放进session中
            setAuthenticationCachingEnabled(true);
        }

        return authcInfo;

    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     *
     * @param key
     * @param value
     */
    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }

    /**
     * 从session中取出数据
     * @param key
     * @return
     */
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
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    public Object getUsername(PrincipalCollection principals) {
        Object primary = null;
        if (!CollectionUtils.isEmpty(principals)) {
            Collection thisPrincipals = principals.fromRealm(getName());
            if (!CollectionUtils.isEmpty(thisPrincipals)) {
                primary = thisPrincipals.iterator().next();
            } else {
                //no principals attributed to this particular realm.  Fall back to the 'master' primary:
                primary = principals.getPrimaryPrincipal();
            }
        }
        return primary;
    }

}
