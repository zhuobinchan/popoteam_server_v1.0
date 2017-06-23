package com.geetion.generic.permission.utils;

import com.geetion.generic.userbase.pojo.UserBase;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Component("geetionPermissionPasswordHelper")
public class PasswordHelper {

    /* 指定hash算法为MD5 */
    public static final String algorithmName = "md5";
    /* 指定散列次数为2次 */
    public static final int hashIterations = 2;

    public String encryptPassword(UserBase user) {
        /* SimpleHash的四个参数分别标识算法名称，散列对象，散列使用的salt值，散列次数。 */
        return new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getAccount()), hashIterations).toHex();
    }

    public String encryptPassword(String account, String password){
        return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(account), hashIterations).toHex();
    }

//    public String decryptKey(String key){
//        return key;//TODO 与加密算法相逆的解密算法
//    }
}
