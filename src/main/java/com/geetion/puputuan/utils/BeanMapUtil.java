package com.geetion.puputuan.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/5/4.
 */
public class BeanMapUtil {

    private static Map<String, String> beanMap = new HashMap<String, String>();
    static {
        ClassPathResource configJson = new ClassPathResource("bean_map.json");
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(configJson.getInputStream()));
            StringBuffer data = new StringBuffer();
            String temp = null;
            while ((temp = bf.readLine()) != null) {
                data.append(temp);
            }
            beanMap = JSON.parseObject(data.toString(),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过HashMap集合,获取bean的具体值
     * @param beanKey
     * @return
     */
    public static String getBeanValue(String beanKey){
        if(beanMap.containsKey(beanKey)){
            return beanMap.get(beanKey);
        }else {
            throw new NullPointerException("key值没有对应的value值");
        }

    }

    /**
     * 首先获取beanKey，获取到对应的dao全名 如beanKey为cgpDaoActivity 则返回 activityDAO
     * 方便通过spring获取Dao
     * @param beanKey 如果beanKey为cgpdActivityDAO
     * @return 则返回 activityDAO
     */
    public static String getDaoSimpleName(String beanKey){
        String beanFullName = getBeanValue(beanKey);
        String name = beanFullName.substring(beanFullName.lastIndexOf(".")+1,beanFullName.length());
        String first = name.substring(0, 1).toLowerCase();
        String rest = name.substring(1, name.length());
        return new StringBuffer(first).append(rest).toString();
    }

    public static Map<String, String> getBeanMap() {
        return beanMap;
    }
}
