package com.geetion.generic.sms.constants;

/**
 * Created by jian on 2016/4/27.
 */
public class SmsTemplate {

    /*
     * 具体查看 阿里大鱼 - 服务管理 - 短信模板管理
     * {@link http://www.alidayu.com/admin/service/tpl}
     */

    /**
     * 身份验证验证码模板
     */
    public static final String Identity_authentication = "SMS_8270370";

    /**
     * 短信测试
     */
    public static final String SMS_test = "SMS_8270369";

    /**
     * 登录确认验证码
     */
    public static final String Login_confirmation = "SMS_8270368";


    /**
     * 登录异常验证码
     */
    public static final String Login_exception = "SMS_8270367";

    /**
     * 用户注册验证码
     */
    public static final String User_registration = "SMS_8270366";

    /**
     * 活动确认验证码
     */
    public static final String Activity_confirmation = "SMS_8270365";

    /**
     * 修改密码验证码
     */
    public static final String Modify_password = "SMS_8270364";

    /**
     * 信息变更验证码
     */
    public static final String Information_change = "SMS_8270363";

    /**
     * 统一验证码
     */
    public static final String Common_Verification = "SMS_29065051";

    /**
     * 设置短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
     * 模板内容示例：验证码${code}，您正在参加${product}的${item}活动，请确认系本人申请。
     * @param template
     * @param code
     * @param product
     * @param item
     * @return
     */
    public static String setSmsParamString(String template, String code, String product, String item) {

        switch (template) {

            case Identity_authentication:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case SMS_test:
                return "{\"customer\":\"" + product + "\"}";

            case Login_confirmation:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case Login_exception:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case User_registration:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case Activity_confirmation:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product +"\",\"item\":\"" + item + "\"}";

            case Modify_password:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case Information_change:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

            case Common_Verification:
                return "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";
        }
        return null;
    }

}
