package com.geetion.generic.permission.shiro.service;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

/**
 *
 * 读取权限和url的对应关系
 *
 * Created by jian on 15/4/23.
 */
public class InitAuthUrl {


    private static Map<String, List<String>> multiPartMap = new HashMap<>();

    private static final String CRLF = "\r\n";

    /**
     * 将资源文件中的url-permission关系转化成可以被shiro读取的方式
     * @param stringBuffer
     * @return
     */
    public static StringBuffer initPermissionUrlFromProperties(StringBuffer stringBuffer) {
        /**
         * 将properties文件加载进Map
         */
        LoadPermission();

        Iterator<Map.Entry<String, List<String>>> it = multiPartMap.entrySet().iterator();
        String formatString = "%s = authc, perms[%s]";
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            String url = entry.getKey();
            List<String> permissions = entry.getValue();

            String permission = "";

            /**
             * 将admin.*  等的权限变成，admin:*
             * 将admin:create.*  等的权限变成，admin:create:*
             * 将admin:create:edit  等的权限变成，admin:create:edit
             */
            String persMutil[] = permissions.get(0).split("\\.");
            if (persMutil.length == 1) {
                permission = persMutil[0] + ":*";
            } else if (persMutil.length == 2) {
                permission = persMutil[0] + ":" + persMutil[1] + ":*";
            } else {
                permission = permissions.get(permissions.size() - 1).replaceAll("\\.", ":");
            }

            for (int i = 0; i < permissions.size() - 1; i++) {

                String persMutil1[] = permissions.get(i).split("\\.");
                if (persMutil.length == 1) {
                    permission += "," + persMutil1[0] + ":*";
                } else if (persMutil.length == 2) {
                    permission += "," + persMutil1[0] + ":" + persMutil1[1] + ":*";
                } else {
                    permission += "," + permissions.get(i).replaceAll("\\.", ":");
                }
            }

            String urlLink = String.format(formatString, url, permission);
            stringBuffer.append(urlLink).append(CRLF);
            System.out.println(urlLink);
        }
        return stringBuffer;
    }



    /**
     * 从资源文件中读取权限-路径（Permission-url）的对应关系
     * 例：base.user.add=/doTest /main
     * 可以有多个url，用空格分开
     */
    public static void LoadPermission() {
        ClassPathResource cp = new ClassPathResource("WEB-INF/properties/permission.properties");
        Properties properties = new Properties();
        try {
            properties.load(cp.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            String[] urls = value.split(" ");
            if (urls != null && urls.length > 0) {
                for (String url : urls) {
                    putToMultiPartMap(url, key);
                }
            } else {
                putToMultiPartMap(value, key);
            }
            System.out.println("\n\nkey  " + key + "  value  " + value);
        }
    }

    /**
     * 将url和Permission的对应关系放入map中，以url为key，多个Permission则放入list中
     * @param url
     * @param key
     */
    public static void putToMultiPartMap(String url, String key) {
        List<String> keyList = multiPartMap.get(url);
        if (keyList == null) {
            keyList = new ArrayList<>();
        }
        keyList.add(key);
        multiPartMap.put(url, keyList);
    }


    /**
     * 从数据库中读取permission-url关系
     * @param stringBuffer
     * @param map
     * @return
     */
    public static StringBuffer initPermissionUrlFromDB(StringBuffer stringBuffer,Map<String,String> map){

        String formatString = "%s = authc, perms[%s]";

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){

            Map.Entry<String, String> entry = it.next();
            String urlLink = String.format(formatString, entry.getKey(), entry.getValue());
            stringBuffer.append(urlLink).append(CRLF);
        }

        return stringBuffer;
    }

}
