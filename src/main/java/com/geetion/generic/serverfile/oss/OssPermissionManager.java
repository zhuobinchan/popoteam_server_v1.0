package com.geetion.generic.serverfile.oss;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Beary on 16/1/7.
 */
@Component
public class OssPermissionManager {
    public final String REGION_CN_HANGZHOU = "cn-hangzhou";
    public final String STS_API_VERSION = "2015-04-01";

    protected AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
                                            String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);

            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);

            return response;
        } catch (ClientException e) {
            throw e;
        }
    }


    public static String ReadJson(BufferedReader reader) {
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            //每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    public Map<String, String> doAction() {
        OssOption.init();
        ClassPathResource configjson = new ClassPathResource("permission_config.json");
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(configjson.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = ReadJson(bf);
        JSONObject jsonObj = JSONObject.fromObject(data);


        // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        String accessKeyId = jsonObj.getString("AccessKeyID");
        String accessKeySecret = jsonObj.getString("AccessKeySecret");

        // RoleArn 需要在 RAM 控制台上获取
        String roleArn = jsonObj.getString("RoleArn");
        long durationSeconds = jsonObj.getLong("TokenExpireTime");
        String policypath = jsonObj.getString("PolicyFile");
        ClassPathResource policyfile = new ClassPathResource(policypath);
        try {
            bf = new BufferedReader(new InputStreamReader(policyfile.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String policy = ReadJson(bf);
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        String roleSessionName = "zhongtu";

        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;

        try {
            final AssumeRoleResponse stsResponse = assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName,
                    policy, protocolType, durationSeconds);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("status", "200");
            respMap.put("AccessKeyId", stsResponse.getCredentials().getAccessKeyId());
            respMap.put("AccessKeySecret", stsResponse.getCredentials().getAccessKeySecret());
            respMap.put("SecurityToken", stsResponse.getCredentials().getSecurityToken());
            respMap.put("Expiration", stsResponse.getCredentials().getExpiration());

            return respMap;

        } catch (ClientException e) {

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("status", e.getErrCode());
            respMap.put("AccessKeyId", "");
            respMap.put("AccessKeySecret", "");
            respMap.put("SecurityToken", "");
            respMap.put("Expiration", "");
            return respMap;

        }
    }


    /**
     * 加密URL
     *
     * @param accessId      OSS账号id
     * @param accessSecret  OSS账号secret
     * @param securityToken OSS云端获取的token
     * @param bucket        eg. "bucket-zhongtu"
     * @param objectPath    eg. "zhongtu/user/2/user-1456389391243-onepunchman3.jpg@100w.jpg"
     *                      ps:后面@100w代表100像素宽,".jpg"为格式
     * @param isPic         true(加密图片URL),false(加密文件URL),如果需要使用图片加密,OSS账号需要开通图片服务
     * @return
     */
    public String cipherUrl(String accessId, String accessSecret, String securityToken, String bucket, String objectPath, boolean isPic) {
        OSSClient client = new OSSClient(isPic == true ? OssOption.IMGENDPOINT : OssOption.ENDPOINT,
                accessId,
                accessSecret,
                securityToken);
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 55 * 60 * 1000);
        // System.out.println("\n截取的url是:  " + cutUrl + "\n");
        GeneratePresignedUrlRequest generatePresignedUrlRequest =

                new GeneratePresignedUrlRequest(bucket, objectPath);

        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

}
