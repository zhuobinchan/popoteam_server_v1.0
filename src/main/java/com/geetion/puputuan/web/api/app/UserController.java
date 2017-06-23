package com.geetion.puputuan.web.api.app;

import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/user")
public interface UserController {

    /**
     * 用户登录接口
     *
     * @param account  账号
     * @param password 密码
     * @param code     验证码
     * @param type     标识是 密码登录 短信登录 还是 微信登录，1：密码登录，2：短信登录，3：微信登录
     * @param device   登录设备 0表示Android，1表示iOS
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    Object login(String account, String password, String code, Integer type, Integer device);


    /**
     * 用户注册接口
     *
     * @param user     用户对象
     * @param userBase 用户基本对象
     * @param code    验证码
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Object register(@ModelAttribute User user, @ModelAttribute UserBase userBase, String code, @RequestParam("uploadfile") CommonsMultipartFile file);


    /**
     * 用户注册接口
     *
     * @param user     用户对象
     * @param userBase 用户基本对象
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/registerInfo", method = RequestMethod.POST)
    @ResponseBody
    Object registerInfo(@ModelAttribute User user, @ModelAttribute UserBase userBase);

    /**
     * 重置密码
     *
     * @param password  重置的新密码
     * @param account   用户账号
     * @param code      验证码
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    Object resetPassword(String account, String password, String code);

    /**
     * 获得用户个人信息
     *
     * @return
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    Object getInfo();


    /**
     * 更新用户个人信息
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ResponseBody
    Object updateInfo(@ModelAttribute User user, Long avatarId, String[] jobStrings, String[] interestStrings, String[] fancyStrings);


    /**
     * 更新用户密码
     *
     * @param password
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    Object updatePassword(String oldPassword, String password);


    /**
     * 根据用户的identity查询用户
     *
     * @param identify 根据用户的identity查询
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(String identify);


    /**
     * 根据用户的userId查询用户
     *
     * @param userId 用户的userId
     * @return
     */
    @RequestMapping(value = "/searchByUserId", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchByUserId(Long userId);


    /**
     * 查询系统的还有用户自己创建的兴趣
     * * @return
     */
    @RequestMapping(value = "/searchAllInterest", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchAllInterest();


    /**
     * 查询系统的还有用户自己创建的职业
     * * @return
     */
    @RequestMapping(value = "/searchAllJob", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchAllJob();


    /**
     * 绑定手机
     * * @return
     */
    @RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
    @ResponseBody
    Object bindPhone(String phone,String code, String password);


    /**
     * 绑定微信
     * * @return
     */
    @RequestMapping(value = "/bindWeChat", method = RequestMethod.POST)
    @ResponseBody
    Object bindWeChat(String wechatId);

    // TODO add by simon at 2016/08/03
    /*
    *  获取用户配置
    *  * @return
    */
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object getUserSetting();

    /*
    *  更新用户配置
    *
    *  @param Integer ntyBySound
    *  @param Integer ntyByVibration
    *  @param Integer isSpeakerOn
    *  @param Integer showFriendsToStranger
    *  @return
    */
    @RequestMapping(value = "/updateSetting", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    Object updateUserSetting(int ntyBySound, int ntyByVibration, int isSpeakerOn, int showFriendsToStranger);

    // TODO add by simon at 2016/08/10
    /*
    *  举报
    *
    *  @param String identify
    *  @param Long type
    *  @param String content
    *  @return
    */
    @RequestMapping(value = "/complain", method = RequestMethod.POST)
    @ResponseBody
    Object complainUser(String identify, Integer type, String content);

    /**
     * 设置用户手机号码是否可见
     * @param ifOn 0:屏蔽 1：可见
     * @return
     */
    @RequestMapping(value = "/shieldPhoneOn", method = RequestMethod.POST)
    @ResponseBody
    Object shieldPhoneOn(String ifOn);

    /**
     * 查询用户手机号码是否可见
     * @return
     */
    @RequestMapping(value = "/ifShieldPhoneOn", method = RequestMethod.POST)
    @ResponseBody
    Object ifShieldPhoneOn();

    /**
     * 通过手机号码查询对应的用户，并过滤其他用户屏蔽的手机号码
     * @param phones
     * @return
     */
    @RequestMapping(value = "/searchUserByPhones", method = RequestMethod.POST)
    @ResponseBody
    Object searchUserByPhones(String[] phones);

    @RequestMapping(value = "/searchUserSuperLikeTimes", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserSuperLikeTimes();

}
