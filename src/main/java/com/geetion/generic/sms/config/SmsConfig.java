package com.geetion.generic.sms.config;

/**
 * Created by jian on 2016/4/27.
 */
public class SmsConfig {


    /**
     * DefaultTaobaoClient的URL
     * HTTP请求地址
     * 正式环境 http://gw.api.taobao.com/router/rest
     * 沙箱环境 http://gw.api.tbsandbox.com/router/rest
     * <p/>
     * HTTPS请求地址
     * 正式环境 https://eco.taobao.com/router/rest
     * 沙箱环境 https://gw.api.tbsandbox.com/router/rest
     *
     * @param args
     */
    //正式请求环境HTTPS
    public static String URL = "https://eco.taobao.com/router/rest";

    //APP_KEY 阿里大鱼管理中心获得
    public static String APP_KEY = "23355290";
    //APP_SECRET 阿里大鱼管理中心获得
    public static String APP_SECRET = "18900d708a857e1b2b9e3d5680f79275";


    //短信类型
    public static String SMS_TYPE = "normal";

    /*
     * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。
     * 如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。
     * 短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
     */
    public static String SMS_FREE_SIGN_NAME = "蒲蒲团";

    /*
     * 产品名称 ，用于在短信显示是什么产品
     */
    public static String PRODUCT = "蒲蒲团";


}
