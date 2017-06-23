package com.geetion.generic.serverfile.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

/**
 * Created by Beary on 16/1/5.
 */
public class OssManager {

    private OSSClient ossClient;

    /**
     * 构造函数,网络参数使用默认
     */
    public OssManager() {
        if (ossClient == null) {
            ossClient = new OSSClient(OssOption.ENDPOINT, OssOption.ACCESSKEYID, OssOption.ACCESSKEYSECRET);
        }
    }

    /**
     * 构造函数,传入自定义的网络参数,配置最大重试和超时时间
     *
     * @param configuration
     */
    public OssManager(ClientConfiguration configuration) {
        if (ossClient == null) {
            ossClient = new OSSClient(OssOption.ENDPOINT, OssOption.ACCESSKEYID, OssOption.ACCESSKEYSECRET, configuration);
        }
    }

    public OSSClient getOssClient() {
        if (ossClient != null) {
            return ossClient;
        } else {
            throw new IllegalStateException("you should call structure first!");
        }
    }

    /**
     * 每次使用完client都必须关闭
     */
    public void closeClient() {
        if (ossClient != null) {
            ossClient.shutdown();
        } else {
            throw new IllegalStateException("you should call structure first!");
        }
    }

}

