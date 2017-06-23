package com.geetion.puputuan.common.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by yoan on 16/3/7.
 */
@Component
public class UuidUtils {

    public static String generateUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
