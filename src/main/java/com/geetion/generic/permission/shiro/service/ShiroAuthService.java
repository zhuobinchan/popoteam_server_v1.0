package com.geetion.generic.permission.shiro.service;

import com.geetion.generic.permission.service.PermissionUrlService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by mac on 15/4/23.
 */
@Service(value = "authService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShiroAuthService {

    @Resource
    private PermissionUrlService permissionUrlService;

    private static final String CRLF = "\r\n";


    /**
     * 返回URL配置路径
     * URL表达式说明
     * 1、URL目录是基于HttpServletRequest.getContextPath()此目录设置
     * 2、URL可使用通配符，**代表任意子目录
     * 3、Shiro验证URL时，URL匹配成功便不再继续匹配查找。所以要注意配置文件中的URL顺序，尤其在使用通配符时。
     * 注意：/** = authc 表示所有路径都经过过滤，应该放在所有路径的最后
     */
    public String loadFilterChainDefinitions() {
        StringBuffer sb = new StringBuffer();
        initFromFile(sb);
        initPermissionUrlFromDB(sb);
        /** initAuthcFiter方法 一定要放在最后 */
        initAuthcFiter(sb);
        return sb.toString();
    }

    /**
     * 从数据库中加载所有需要权限验证的url，并放入shiro中
     *
     * @param stringBuffer
     */
    public void initPermissionUrlFromDB(StringBuffer stringBuffer) {

        //从数据库中加载所有需要权限验证的url
        Map<String, String> map = permissionUrlService.putUrlPermissionToMap();
        //转化为shiro可以读取的方式
        InitAuthUrl.initPermissionUrlFromDB(stringBuffer, map);
    }

    /**
     * 加入过滤的url地址，默认加入所有
     *
     * @param stringBuffer
     */
    public void initAuthcFiter(StringBuffer stringBuffer) {
//        stringBuffer.append("/logout = authc,forceLogout").append(CRLF);
        stringBuffer.append("/** = authc").append(CRLF);
    }

    /**
     * 从资源文件中读取不过滤的url路径
     */
    public void initFromFile(StringBuffer stringBuffer) {
        //读取文件
        ClassPathResource cp = new ClassPathResource("shiroAnonFilter.properties");
        try {
            if (cp.exists()) {
                InputStream inputStream = cp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append(CRLF);
                }
            } else {
                System.out.println("\n shiroAnonFilter.properties 文件不存在，默认对静态资源及登录路径不过滤，对其他所有路径进行过滤");
            }
            //加载默认的不过滤的路径
            initDefaultAnonFilter(stringBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 默认的不过滤的url地址
     * 对静态资源及登录路径不过滤
     *
     * @param stringBuffer
     */
    public void initDefaultAnonFilter(StringBuffer stringBuffer) {
        stringBuffer.append("/js/** = anon").append(CRLF);
        stringBuffer.append("/dep/** = anon").append(CRLF);
        stringBuffer.append("/dist/** = anon").append(CRLF);
        stringBuffer.append("/res/** = anon").append(CRLF);
        stringBuffer.append("/src/** = anon").append(CRLF);
        stringBuffer.append("/upload/** = anon").append(CRLF);
        stringBuffer.append("/favicon.ico = anon").append(CRLF);
        stringBuffer.append("/login.jsp = anon").append(CRLF);
        stringBuffer.append("/login = anon").append(CRLF);

    }

}
