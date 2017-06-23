package com.geetion.puputuan.test;

/**
 * Created by Administrator on 2017/5/3.
 */
public class StringUtils {
    static public boolean isEmpty(Object... arg) {
        if (arg != null) {
            for (int i = 0; i < arg.length; i++) {
                if (null == arg[i] || "".equals(arg[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
