package com.test.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/5/15.
 */
public class HttpUtils {
    static CloseableHttpClient httpclient =null;
    public static void OpenHttpClient()
    {
        //打开浏览器
        httpclient = HttpClients.createDefault();
    }

    public static void CloseHttpClient()
    {
        //关闭浏览器
        try {
            httpclient.close();
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpclient = null;
    }

//    public static JSONObject visitUrl(String url)
//    {
//        //CloseableHttpClient httpclient = HttpClients.createDefault();
//
//        HttpGet httpGet = new HttpGet(url);
////        HttpPost httpPost = new HttpPost(url);
//        return excuteUrl(httpGet);
//    }

    public static JSONObject getUrl(String url, Map<String,Object> paramMap){
        HttpGet httpGet = new HttpGet(url);
        HttpParams params = new BasicHttpParams();
        for (String key : paramMap.keySet()) {
            params.setParameter(key,paramMap.get(key));
        }
        httpGet.setParams(params);
        return excuteUrl(httpGet);
    }

    public static JSONObject postUrl(String url,Map<String,Object> paramMap){
        HttpPost httpPost = new HttpPost(url);

        HttpParams params = new BasicHttpParams();
        for (String key : paramMap.keySet()) {
            params.setParameter(key,paramMap.get(key));
        }
        httpPost.setParams(params);
        return excuteUrl(httpPost);
    }

    private static JSONObject excuteUrl(HttpRequestBase requestBase){
        JSONObject jsonObj=null;
        try {
            CloseableHttpResponse response = httpclient.execute(requestBase);
            HttpEntity entity = response.getEntity();

            StringBuilder jsonStr = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"),
                    8 * 1024);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStr.append(line + "/n");
            }
            EntityUtils.consume(entity);
            //获取JSON对象的值
            jsonObj = JSON.parseObject(jsonStr.toString());
            response.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return jsonObj;
    }
}
