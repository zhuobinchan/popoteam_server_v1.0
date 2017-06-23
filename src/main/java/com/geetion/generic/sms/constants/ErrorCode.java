package com.geetion.generic.sms.constants;

/**
 * Created by jian on 2016/4/27.
 */
public class ErrorCode {


    /*
     * 业务停机	登陆www.alidayu.com充值
     */
    public static String OUT_OF_SERVICE = "isv.OUT_OF_SERVICE";
    /*
    * 产品服务未开通	登陆www.alidayu.com开通相应的产品服务
    */
    public static String PRODUCT_UNSUBSCRIBE = "isv.PRODUCT_UNSUBSCRIBE";
    /*
    * 账户信息不存在	登陆www.alidayu.com完成入驻
    */
    public static String ACCOUNT_NOT_EXISTS = "isv.ACCOUNT_NOT_EXISTS";
    /*
    * 账户信息异常	联系技术支持
    */
    public static String ACCOUNT_ABNORMAL = "isv.ACCOUNT_ABNORMAL";
    /*
    * 模板不合法	登陆www.alidayu.com查询审核通过短信模板使用
    */
    public static String SMS_TEMPLATE_ILLEGAL = "isv.SMS_TEMPLATE_ILLEGAL";
    /*
    * 签名不合法	登陆www.alidayu.com查询审核通过的签名使用
    */
    public static String SMS_SIGNATURE_ILLEGAL = "isv.SMS_SIGNATURE_ILLEGAL";
    /*
    * 手机号码格式错误	使用合法的手机号码
    */
    public static String MOBILE_NUMBER_ILLEGAL = "isv.MOBILE_NUMBER_ILLEGAL";
    /*
    * 手机号码数量超过限制	批量发送，手机号码以英文逗号分隔，不超过200个号码
    */
    public static String MOBILE_COUNT_OVER_LIMIT = "isv.MOBILE_COUNT_OVER_LIMIT";
    /*
    * 短信模板变量缺少参数	确认短信模板中变量个数，变量名，检查传参是否遗漏
    */
    public static String TEMPLATE_MISSING_PARAMETERS = "isv.TEMPLATE_MISSING_PARAMETERS";
    /*
    * 参数异常	检查参数是否合法
    */
    public static String INVALID_PARAMETERS = "isv.INVALID_PARAMETERS";
    /*
    * 触发业务流控限制	短信验证码，使用同一个签名，对同一个手机号码发送短信验证码，允许每分钟1条，累计每小时7条。
    * 短信通知，使用同一签名、同一模板，对同一手机号发送短信通知，允许每天50条（自然日）。
    */
    public static String BUSINESS_LIMIT_CONTROL = "isv.BUSINESS_LIMIT_CONTROL";
    /*
    * JSON参数不合法	JSON参数接受字符串值。例如{"code":"123456"}，不接收{"code":123456}
    */
    public static String INVALID_JSON_PARAM = "isv.INVALID_JSON_PARAM";
    /*
    * 系统错误
    */
    public static String SYSTEM_ERROR = "isp.SYSTEM_ERROR";
    /*
    * 模板变量中存在黑名单关键字。如：阿里大鱼	黑名单关键字禁止在模板变量中使用，若业务确实需要使用，建议将关键字放到模板中，进行审核。
    */
    public static String BLACK_KEY_CONTROL_LIMIT = "isv.BLACK_KEY_CONTROL_LIMIT";


}
