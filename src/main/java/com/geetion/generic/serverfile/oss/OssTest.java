package com.geetion.generic.serverfile.oss;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Beary on 16/1/5.
 */

public class OssTest {


    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "ke8llSa5uxhDeq3B";
    private static String accessKeySecret = "XGdXu2MbugvipfhZAhdtG1kAbjnqVa";

    private static OSSClient client = null;

    public static void main(String[] args) throws IOException {

        // Create a new OSSClient instance
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String bucketName = "my-first-oss-bucket" + UUID.randomUUID();
            client.createBucket(bucketName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }
}
