package com.geetion.generic.serverfile.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.model.CannedAccessControlList;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Beary on 16/1/5.
 */
public class OssOption {

    public static final CannedAccessControlList[] ACLS = {
            CannedAccessControlList.Private,
            CannedAccessControlList.PublicRead,
            CannedAccessControlList.PublicReadWrite,
            CannedAccessControlList.Default
    };

    public static final int CONTROL_PRIVATE = 0;
    public static final int CONTROL_PUBLICREAD = 1;
    public static final int CONTROL_PUBLICREAD_WRITE = 2;
    public static final int CONTROL_DEFAULT = 3;

    public static String ENDPOINT = "";//默认外网连接域名
    public static String IMGENDPOINT = "";//默认外网图片服务域名
    public static String ACCESSKEYID = "";//id
    public static String ACCESSKEYSECRET = "";//key
    public static String DEFAULT_BUCKET = "";//默认使用的bucket名字
    public static String DEFAULT_DOMAIN = "";//用于存储文件的根文件夹名
    public static String DEFAULT_USER = "";
    public static String DEFAULT_PROJECT = "";
    public static String DEFAULT_TEST = "";

    private static ClientConfiguration configuration;
    private static OssOption option;


    public static OssOption init() {
        if (option == null) {
            option = new OssOption();
        }
        if (configuration == null) {
            configuration = new ClientConfiguration();
        }
        if (ENDPOINT.isEmpty()) {
            ClassPathResource configjson = new ClassPathResource("main_config.json");
            BufferedReader bf = null;
            try {
                bf = new BufferedReader(new InputStreamReader(configjson.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = OssPermissionManager.ReadJson(bf);
            JSONObject jsonObj = JSONObject.fromObject(data);
            ENDPOINT = jsonObj.getString("EndPoint");
            IMGENDPOINT = jsonObj.getString("ImageEndPoint");
            ACCESSKEYID = jsonObj.getString("MainAccessKeyId");
            ACCESSKEYSECRET = jsonObj.getString("MainAccessKeySecret");
            DEFAULT_BUCKET = jsonObj.getString("BucketName");
            DEFAULT_DOMAIN = jsonObj.getString("BuckDomain");
            DEFAULT_USER = jsonObj.getString("FolderUser");
            DEFAULT_PROJECT = jsonObj.getString("FolderProject");
            DEFAULT_TEST = jsonObj.getString("FolderTest");
        }
        return option;
    }

    /**
     * 设置最大超时时间,默认是50s
     *
     * @param timeOut
     * @return
     */
    public  OssOption setConnectTimeOut(int timeOut) {
        if (configuration != null) {
            configuration.setConnectionTimeout(timeOut);
        } else {
            throw new IllegalStateException("you should call OssOption.init() first");
        }
        return option;
    }

    /**
     * 设置最大重试次数,默认是3次
     *
     * @param times
     * @return
     */
    public  OssOption setMaxErrorRetry(int times) {
        if (configuration != null) {
            configuration.setMaxErrorRetry(times);
        } else {
            throw new IllegalStateException("you should call OssOption.init() first");
        }
        return option;
    }

    /**
     * 产生对象
     *
     * @return
     */
    public  ClientConfiguration build() {
        if (configuration != null) {
            return configuration;
        } else {
            throw new IllegalStateException("you should call OssOption.init() first");
        }
    }
}
