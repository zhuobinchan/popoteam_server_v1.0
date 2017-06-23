package com.geetion.puputuan.common.utils;

/**
 * Created by yoan on 16/3/14.
 */
public class StringUtils {

    public static String surroundWith(String source, String surroundedString){
        StringBuilder sb = new StringBuilder(source);
        sb.insert(0, surroundedString);
        sb.append(surroundedString);
        return sb.toString();
    }
}
