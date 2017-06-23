package com.geetion.puputuan.supervene.recommend.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by mac on 16/4/5.
 */
public class SpringLoader {
    private static SpringLoader springLoader;
    private static Object LOCK_OBJECT = new Object();
    private static WebApplicationContext wac;

    public static SpringLoader getInstance() {
        synchronized (LOCK_OBJECT) {
            if (springLoader == null) {
                springLoader = new SpringLoader();
                wac = ContextLoader.getCurrentWebApplicationContext();
            }
        }
        return springLoader;
    }

    public <T> T getBean(Class<T> var1) {
        return wac.getBean(var1);
    }

}
