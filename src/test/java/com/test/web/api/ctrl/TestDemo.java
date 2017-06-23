package com.test.web.api.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.test.utils.HttpUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.client.ClientProtocolException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.util.Map;


/**
 * Created by chenzhuobin on 2017/5/12.
 */
public class TestDemo {
    public Assertion assertion;
    @BeforeClass
    public void beforeClass() {
        assertion = new Assertion();
    }

    @BeforeMethod
    public void runBeforeMethod() {
        // 打开httpclient,相当于打开一个浏览器
        HttpUtils.OpenHttpClient();//这边一定要记得在测试用例开始之前打开浏览器，否则会出现空指针的错误
    }
    @AfterMethod
    public void runAfterMethod() {
        // 打开httpclient,相当于打开一个浏览器
        HttpUtils.CloseHttpClient();
    }

    @org.testng.annotations.Test
    public void f() throws ClientProtocolException, IOException {

        String loginUrl = "http://localhost:8080/login";
        Map<String,Object> map = new HashedMap();
        map.put("account","admin");
        map.put("password",1);
        JSONObject json = HttpUtils.postUrl(loginUrl,map);
        boolean success = json.getBoolean("success");
//        String enterTrainningUrl = "http://xx.xxx.cn/Training/enterTrainingCamp.do?roomid=1111";
//        System.out.println(enterTrainningUrl);
//        JSONObject enterObj = HttpUtils.visitUrl(enterTrainningUrl);
//        System.out.println(enterObj.toString());
//        boolean success2 = enterObj.getBoolean("success");
//        assertion.assertTrue(success);
    }

}
