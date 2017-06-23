package com.geetion.puputuan.web.api.app.impl;

import com.easemob.server.example.exception.HuanXinUserException;
import com.easemob.server.example.service.HuanXinUserService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.service.SmsCodeService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.InterestTypeAndStatus;
import com.geetion.puputuan.common.constant.JobTypeAndStatus;
import com.geetion.puputuan.common.constant.SexType;
import com.geetion.puputuan.common.constant.SmsTemplateType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.utils.MD5Utils;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.common.utils.UuidUtils;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.model.jsonModel.*;
import com.geetion.puputuan.pojo.ConfigInfo;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.ConvertBeanUtils;
import com.geetion.puputuan.utils.EmojiCharacterUtil;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.app.UserController;
import com.geetion.puputuan.web.api.base.BaseController;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class UserControllerImpl extends BaseController implements UserController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource
    private JobService jobService;
    @Resource
    private JobUserService jobUserService;
    @Resource
    private InterestService interestService;
    @Resource
    private InterestUserService interestUserService;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private SmsCodeService smsCodeService;
    @Resource
    private BeViewedService beViewedService;
    // TODO add by simon at 2016/08/10
    @Resource
    private  UserSettingService userSettingService;
    @Resource
    private ComplainUserService complainUserService;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private UserSuperLikeService userSuperLikeService;
    @Resource
    private UserSuperLikeConfigService userSuperLikeConfigService;
    @Resource(name = "puputuanApplication")
    private Application application;

    @Resource
    private OssFileUtils ossFileUtils;

    @Resource
    private PhotoService photoService;

    @Resource
    private GroupService groupService;

    @Resource
    private FancyService fancyService;

    @Resource
    private FancyUserService fancyUserService;

    @Autowired
    private ConfigInfo configInfo;
    /**
     * 验证码开关
     */
//    private final boolean IF_CHECK_SMS_CODE = true;
    private Logger logger = Logger.getLogger(UserControllerImpl.class);

    /**
     * 添加用户时的用户标识，从 600000 开始递增
     */
    private static final Long BASE_IDENTIFY = 600000L;

    private static final String SHIELD_PHONE_ON = "1";

    // 返回super like的次数
    private final static String NUM_SUPER_LIKE = "NUM_SUPER_LIKE";
    @Override
    public Object login(String account, String password, String code, Integer type, Integer device) {
        logger.info("UserControllerImpl login begin ...");

        if (checkParaNULL(account, type, device)) {
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                User user = null;
                Map<String, Object> param = new HashMap<>();
                Map<String, Object> resultMap = new HashMap<>();

                switch (type) {
                    case 1:
                        //密码登录
                        if (checkParaNULL(password)) {

                            param.put("phone", account);
//                            user = userService.getUser(param);
                            user = userService.getUserByParam(param);
                            if (user == null) {
                                transactionHelper.rollback(transactionStatus);
                                return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
                            }

                            if(user.getUserBase().getFreeze() == 1){
                                transactionHelper.rollback(transactionStatus);
                                return sendResult(ResultCode.CODE_706.code, ResultCode.CODE_706.msg, null);
                            }
                            //调用Shiro的登录功能
                            shiroService.login(user.getUserBase().getAccount(), password, "app");

                        } else {
                            return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                        }
                        break;
                    case 2:
                        //微信登录
                        param.put("wechatId", account);
                        user = userService.getUserByParam(param);

                        if (user == null) {
                            transactionHelper.rollback(transactionStatus);
                            //user为空，第一次用微信登录,需要先注册个人信息
                            return sendResult(ResultCode.CODE_714.code, ResultCode.CODE_714.msg, resultMap, true);
                        }

                        if(user.getUserBase().getFreeze() == 1){
                            transactionHelper.rollback(transactionStatus);
                            return sendResult(ResultCode.CODE_706.code, ResultCode.CODE_706.msg, null);
                        }
                        break;
                    default:
                        transactionHelper.rollback(transactionStatus);
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }

                user.getUserBase().setToken(UuidUtils.generateUuid());
                userBaseService.updateWithoutPassword(user.getUserBase());
                user.setDevice(device);
                userService.updateUserByUserId(user);


                //获取图片
                ossFileUtils.getPhotoList(user.getAlbum(), null);

                transactionHelper.commit(transactionStatus);
                // TODO test by simon at 2016/07/29
                //resultMap.put("object", user);
                this.setUserInterestAndJob(user);
                JSONUser jsonUser = convertDBModelToJSONModel(user);
                resultMap.put("object", jsonUser);
                resultMap.put("haveRunningGroup", groupService.getRunningGroup(user.getUserId()) != null ? true : false);
                resultMap.put("authorization", getAuthorizationFromUser(user));

                /**  获取环信用户并返回给移动端登录  */
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);

            } catch (UnknownAccountException uae) {
                logger.error("UserControllerImpl login error " + uae);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
            } catch (IncorrectCredentialsException ice) {
                logger.error("UserControllerImpl login error " + ice);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
            } catch (LockedAccountException lae) {
                logger.error("UserControllerImpl login error " + lae);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_706.code, ResultCode.CODE_706.msg, null);
            } catch (IllegalArgumentException iae) {
                logger.error("UserControllerImpl login error " + iae);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_703.code, ResultCode.CODE_703.msg, null);
            } catch (ExcessiveAttemptsException e) {
                logger.error("UserControllerImpl login error " + e);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_710.code, ResultCode.CODE_710.msg, null);
            } catch (Exception ex) {
                logger.error("UserControllerImpl login error " + ex);
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object register(@ModelAttribute User userReg, @ModelAttribute UserBase userBaseReg, String code, @RequestParam("uploadfile") CommonsMultipartFile file) {
        logger.info("UserControllerImpl register begin ...");

        if (checkParaNULL(userBaseReg, userBaseReg.getAccount(), userBaseReg.getPassword(), userReg, userReg.getBirthday(),
                userReg.getSex(), userReg.getNickName(), code, file)) {

            //TODO 修改注册流程
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("phone", userBaseReg.getAccount());
            User user = userService.getUser(param);
            if (user == null) {
                /** 开启事务 */
                TransactionStatus transactionStatus = transactionHelper.start();

                if( configInfo.isSmsOpen() ) {
                    //获取短信验证码信息
                    ResultCode resultCode = checkSmsCode(userBaseReg.getAccount(), code);
                    if (resultCode != ResultCode.CODE_200) {
                        /** 回滚事务 */
                        transactionHelper.rollback(transactionStatus);
                        return sendResult(resultCode.code, resultCode.msg, null);
                    }
                }

                UserBase userBase = new UserBase();
//                userBase.setAccount("popoteam_user_" + System.currentTimeMillis());
                userBase.setAccount(configInfo.getAccountPrex() + System.currentTimeMillis());
                userBase.setToken(UuidUtils.generateUuid());
                userBase.setPassword(userBaseReg.getPassword());

                try {
                    //添加用户基本信息
                    userBaseService.add(userBase);
                    File pojoFile = ossFileUtils.uploadFile(userBase.getId(), file, (long) 0);
                    if (null == pojoFile) {
                        /** 回滚事务 */
                        transactionHelper.rollback(transactionStatus);
                        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                    }

                    //插入相册表,设置为头像
                    Photo photo = new Photo();
                    photo.setImageId(pojoFile.getId());
                    photo.setUserId(userBase.getId());
//                    photo.setIsDelete(false);
                    photo.setIsAvatar(true);
                    photo.setImage(pojoFile);
                    photoService.addPhoto(photo);

                    user = new User();
                    user.setUserId(userBase.getId());
                    user.setPhone(userBaseReg.getAccount());
                    user.setIdentify(application.getBASE_USER_IDENTIFY() + "");
                    user.setBirthday(userReg.getBirthday());
                    user.setConstellation(getConstellation(user.getBirthday()));
                    //设置性别只能是 M 或 F
                    user.setSex(userReg.getSex().equals(SexType.MALE) ? SexType.MALE : SexType.FEMALE);
                    // 过滤器没起作用，手动转码emji表情
                    user.setNickName(EmojiCharacterUtil.emojiConvert1(userReg.getNickName()));
                    // 用户昵称对应的拼音，过滤emoji表情
                    String filterEmojiNickName = EmojiCharacterUtil.filter(EmojiCharacterUtil.emojiRecovery2(userReg.getNickName()));
                    user.setNickNameChr(PinyinHelper.convertToPinyinString(filterEmojiNickName.equals("") ? "#" : filterEmojiNickName
                            , "", PinyinFormat.WITHOUT_TONE));
                    user.setWechatId(userReg.getWechatId());
                    user.setHeadId(photo.getImageId());
                    user.setUserBase(userBase);

                    List <Photo> album = new ArrayList<>();
                    album.add(photo);
                    user.setAlbum(album);

                    //添加用户详细信息
                    userService.addUser(user);

                    /**  注册环信 注册成功之后再向数据库插入数据 */
                    //查询用户是否存在
                    boolean isUserExist = HuanXinUserService.userExistOrNot(userBase.getAccount());
                    if (!isUserExist) {
                        //如果环信用户不存在，则新建一个
                        HuanXinUserService.createNewIMUser(userBase.getAccount(), user.getNickName());
                    }

                    /** 提交事务 */
                    transactionHelper.commit(transactionStatus);
                } catch (AuthenticationException e) {
                    logger.error("UserControllerImpl register error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

                } catch (IllegalArgumentException e) {
                    logger.error("UserControllerImpl register error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                } catch (PinyinException e) {
                    //转换拼音异常
                    logger.error("UserControllerImpl register error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                } catch (HuanXinUserException e) {
                    logger.error("UserControllerImpl register error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
                } catch (Exception e) {
                    logger.error("UserControllerImpl register error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }

                //调用Shiro的登录功能
                shiroService.login(user.getUserBase().getAccount(), userBaseReg.getPassword(), "app");

                // TODO test by simon at 2016/07/29
                //resultMap.put("object", user);
                this.setUserInterestAndJob(user);
                JSONUser jsonUser = convertDBModelToJSONModel(user);
                resultMap.put("object", jsonUser);

                resultMap.put("authorization", getAuthorizationFromUser(user));

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);
            }
            return sendResult(ResultCode.CODE_702.code, ResultCode.CODE_702.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object registerInfo(@ModelAttribute User user, @ModelAttribute UserBase userBase) {
        logger.info("UserControllerImpl registerInfo begin ...");
        if (checkParaNULL(userBase, userBase.getPassword(), user, user.getNickName(),
                user.getBirthday(), user.getSex(), user.getDevice(), user.getHeadId())) {
            Map<String, Object> resultMap = new HashMap<>();
            //设置性别只能是 M 或 F
            user.setSex(user.getSex().equals(SexType.MALE) ? SexType.MALE : SexType.FEMALE);
            //先保存旧密码，因为在添加用户时，密码会被加密
            String password = userBase.getPassword();
            UserBase userBaseFromDB = shiroService.getLoginUserBase();
            if (userBaseFromDB != null) {
                /** 开启事务 */
                TransactionStatus transactionStatus = transactionHelper.start();
                try {
                    //更改用户密码
                    userBaseService.updatePassword(userBaseFromDB.getAccount(), password);
                    //从数据库获得用户详细信息
                    User userFromDB = userService.getByUserId(userBaseFromDB.getId());
                    user.setId(userFromDB.getId());
                    //更新用户信息
                    userService.updateUser(user);
//                    User lastUser = userService.getUser(null);
//                    user.setUserId(userBaseFromDB.getId());
//                    //设置用户添加好友时的标识
//                    if (lastUser != null) {
//                        user.setIdentify((Long.valueOf(lastUser.getIdentify()) + 1) + "");
//                    } else {
//                        user.setIdentify(BASE_IDENTIFY + "");
//                    }
//                    //添加用户项目信息
//                    userService.addUser(user);

                    /**  注册环信 注册成功之后再向数据库插入数据 */
                    //查询用户是否存在
                    boolean isUserExist = HuanXinUserService.userExistOrNot(userBaseFromDB.getAccount());
                    if (!isUserExist) {
                        //如果环信用户不存在，则新建一个
                        HuanXinUserService.createNewIMUser(userBaseFromDB.getAccount(), user.getNickName());
                    }
//                    Object iMUser = HuanXinUserService.getIMUsersByUserName(userBaseFromDB.getAccount(), user.getNickName());
                    ImUser imUser = new ImUser();
                    imUser.setUserName(user.getUserBase().getAccount());
                    imUser.setPassword(MD5Utils.getMd5(user.getUserBase().getAccount()));

                    /** 提交事务 */
                    transactionHelper.commit(transactionStatus);

                    //调用Shiro的登录功能
//                    resultMap = shiroLogin(userBaseFromDB.getAccount(), password, user.getDevice());
                    resultMap.put("object", userService.getByUserId(userBaseFromDB.getId()));
                    resultMap.put("IMUser", imUser);

                } catch (AuthenticationException e) {
                    logger.error("UserControllerImpl registerInfo error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

                } catch (HuanXinUserException e) {
                    logger.error("UserControllerImpl registerInfo error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
                } catch (Exception e) {
                    logger.error("UserControllerImpl registerInfo error " + e);
                    /** 回滚事务 */
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);
            }
            return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);


//        if (checkParaNULL(userBase, userBase.getPassword(), user, user.getNickName(),
//                user.getBirthday(), user.getSex(), user.getDevice(), user.getHeadId())) {
//
//            //设置性别只能是 M 或 F
//            user.setSex(user.getSex().equals(SexType.MALE) ? SexType.MALE : SexType.FEMALE);
//            //先保存旧密码，因为在添加用户时，密码会被加密
//            String password = userBase.getPassword();
//            UserBase userBaseFromDB = shiroService.getLoginUserBase();
//            if (userBaseFromDB != null) {
//                /** 开启事务 */
//                TransactionStatus transactionStatus = transactionHelper.start();
//                try {
//                    //添加用户基本信息
//                    userBaseService.updatePassword(userBaseFromDB.getAccount(), password);
//
//                    User lastUser = userService.getUser(null);
//                    user.setUserId(userBaseFromDB.getId());
//                    //设置用户添加好友时的标识
//                    if (lastUser != null) {
//                        user.setIdentify((Long.valueOf(lastUser.getIdentify()) + 1) + "");
//                    } else {
//                        user.setIdentify(BASE_IDENTIFY + "");
//                    }
//                    //添加用户项目信息
//                    userService.addUser(user);
//
//                    /**  注册环信 注册成功之后再向数据库插入数据 */
//                    HuanXinUserService.createNewIMUser(userBaseFromDB.getAccount(), user.getNickName());
//                    Object iMUser = HuanXinUserService.getIMUsersByUserName(userBaseFromDB.getAccount(), user.getNickName());
//
//                    /**  注册环信 */
//
//                    /** 提交事务 */
//                    transactionHelper.commit(transactionStatus);
//
//                    //调用Shiro的登录功能
//                    resultMap = shiroLogin(userBaseFromDB.getAccount(), password, user.getDevice());
//                    resultMap.put("IMUser", iMUser);
//
//                } catch (AuthenticationException e) {
//                    e.printStackTrace();
//                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
//
//                } catch (HuanXinUserException e) {
//                    e.printStackTrace();
//                    /** 回滚事务 */
//                    transactionHelper.rollback(transactionStatus);
//                    return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    /** 回滚事务 */
//                    transactionHelper.rollback(transactionStatus);
//                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
//                }
//                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);
//            }
//            return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
//        }
//        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object resetPassword(String account, String password, String code) {
        logger.info("UserControllerImpl resetPassword begin...");
        if (checkParaNULL(account, password, code)) {
            Map<String, Object> resultMap = new HashMap<>();

            if( configInfo.isSmsOpen() ) {
                //获取短信验证码信息
                ResultCode resultCode = checkSmsCode(account, code);
                if (resultCode != ResultCode.CODE_200) {
                    return sendResult(resultCode.code, resultCode.msg, null);
                }
            }

            Map<String, Object> param = new HashMap<>();
            param.put("phone", account);
            User user = userService.getUserByParam(param);

            //用户不存在
            if (null == user) {
                return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
            }

            user.getUserBase().setToken(UuidUtils.generateUuid());
            user.getUserBase().setPassword(password);
            userBaseService.update(user.getUserBase());

            //调用Shiro的登录功能
            shiroService.login(user.getUserBase().getAccount(), password, "app");

            //获取图片
            ossFileUtils.getPhotoList(user.getAlbum(), null);
            this.setUserInterestAndJob(user);
            JSONUser jsonUser = convertDBModelToJSONModel(user);
            resultMap.put("object", jsonUser);
            resultMap.put("authorization", getAuthorizationFromUser(user));
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object getInfo() {
        logger.info("UserControllerImpl getInfo begin...");

        userService.updateUserDailyLiving(shiroService.getLoginUserBase().getId());
        try {
            Map<String, Object> resultMap = new HashMap<>();
            //查询用户信息
            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());

//            User user = userService.getByUserIdWithSystem(shiroService.getLoginUserBase().getId());
            //获取头像
//            ossFileUtils.getUserHead(user, null);

            //获取图片
            ossFileUtils.getPhotoList(user.getAlbum(), null);
            this.setUserInterestAndJob(user);
            //resultMap.put("object", user);
            // TODO test by simon at 2016/07/29
            JSONUser jsonUser = convertDBModelToJSONModel(user);
            resultMap.put("object", jsonUser);
            resultMap.put("haveRunningGroup", groupService.getRunningGroup(user.getUserId()) != null ? true : false);

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap, true);  // TODO mark by simon 这个不加true的话，会过滤掉 token 和 password 两个字段

        } catch (UnknownAccountException uae) {
            logger.error("UserControllerImpl getInfo error " + uae);
            return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
        } catch (IncorrectCredentialsException ice) {
            logger.error("UserControllerImpl getInfo error " + ice);
            ice.printStackTrace();
            return sendResult(ResultCode.CODE_701.code, ResultCode.CODE_701.msg, null);
        } catch (LockedAccountException lae) {
            logger.error("UserControllerImpl getInfo error " + lae);
            return sendResult(ResultCode.CODE_706.code, ResultCode.CODE_706.msg, null);
        } catch (IllegalArgumentException iae) {
            logger.error("UserControllerImpl getInfo error " + iae);
            return sendResult(ResultCode.CODE_703.code, ResultCode.CODE_703.msg, null);
        } catch (ExcessiveAttemptsException e) {
            logger.error("UserControllerImpl getInfo error " + e);
            return sendResult(ResultCode.CODE_710.code, ResultCode.CODE_710.msg, null);
        } catch (Exception ex) {
            logger.error("UserControllerImpl getInfo error " + ex);
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
    }

    public void setUserInterestAndJob(User user){
        List<Interest> interesList = interestService.getInterestWithSystem(user.getUserId());
        List<Job> joblist = jobService.getJobWithSystem(user.getUserId());
        List<Fancy> fancyList = fancyService.getFancyWithSystem(user.getUserId());
        List<Interest> userInterest = user.getInterestList();
        List<Job> userJob = user.getJobList();
        List<Fancy> userFancy = user.getFancyList();

        if (null != userInterest){
            for (Interest in : interesList){
                for (Interest ui : userInterest){
                    if (in.getId().equals(ui.getId())){
                        in.setHaveSelect(true);
                    }
                }
            }
        }

        if (null != userJob){
            for (Job j : joblist){
                for (Job uj : userJob){
                    if (j.getId().equals(uj.getId())){
                        j.setHaveSelect(true);
                    }
                }
            }
        }

        if (null != userFancy){
            for (Fancy f : fancyList){
                for (Fancy uf : userFancy){
                    if (f.getId().equals(uf.getId())){
                        f.setHaveSelect(true);
                    }
                }
            }
        }
        user.setInterestList(interesList);
        user.setJobList(joblist);
        user.setFancyList(fancyList);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object updateInfo(@ModelAttribute User user, Long avatarId, String[] jobStrings, String[] interestStrings, String[] fancyStrings) {
        logger.info("UserControllerImpl updateInfo begin...");
        boolean needUpdateUser = false;

        User userUpdate = new User();
        userUpdate.setUserId(shiroService.getLoginUserBase().getId());

        Long imageId;
        if (null != avatarId) {
            imageId = updateAvatar(avatarId);
            //输入的avatarId错误,返回失败响应
            if (null == imageId) {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }

            userUpdate.setHeadId(imageId);
            needUpdateUser = true;
        }

        if (checkParaNULL(user)) {
            // TODO: 16/8/27  这里要考虑出现异常时,之前的照片数据如何回退,事务能否在这里catch异常后回退?
            try {
                //是否需要更新，当所有字段都为空时不更新
//                boolean needUpdateUser = false;
//                for (Field field : user.getClass().getDeclaredFields()) {
//                    field.setAccessible(true);
//                    //有一个字段不为空，则要更新，否则不更新
//                    if (field.get(user) != null) {
//                        needUpdateUser = true;
//                    }
//                }

                /**
                 * 如果更改了用户名，则对应更新环信账户的用户名
                 * 同时还需要更新nickNameChar字段
                 * */
                if (user.getNickName() != null && !user.getNickName().equals("")) {
//                    user.setNickName(EmojiCharacterUtil.emojiConvert1(user.getNickName()));
                    HuanXinUserService.modifyIMUserNickName(shiroService.getLoginUserBase().getAccount(), user.getNickName());
                    userUpdate.setNickName(user.getNickName());
                    // 用户昵称对应的拼音，过滤emoji表情
                    String filterEmojiNickName = EmojiCharacterUtil.filter(EmojiCharacterUtil.emojiRecovery2(user.getNickName()));
                    userUpdate.setNickNameChr(PinyinHelper.convertToPinyinString(filterEmojiNickName.equals("") ? "#" : filterEmojiNickName
                            , "", PinyinFormat.WITHOUT_TONE));

                    needUpdateUser = true;
                }
            } catch (PinyinException e) {
                logger.error("UserControllerImpl updateInfo error " + e);
                //转换拼音异常
                e.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            } catch (HuanXinUserException e) {
                logger.error("UserControllerImpl updateInfo error " + e);
                e.printStackTrace();
                return sendResult(ResultCode.CODE_600.code, e.getMessage(), null);
            } catch (Exception e) {
                logger.error("UserControllerImpl updateInfo error " + e);
                e.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

            if (user.getSign() != null) {
                userUpdate.setSign(user.getSign());
                needUpdateUser = true;
            }

            /** 如果生日不为空，则计算出星座 */
            if (user.getBirthday() != null) {
                userUpdate.setBirthday(user.getBirthday());
                userUpdate.setConstellation(getConstellation(user.getBirthday()));
                needUpdateUser = true;
            }

            if (user.getProvinceId() != null) {
                userUpdate.setProvinceId(user.getProvinceId());
                needUpdateUser = true;
            }

            if (user.getProvince() != null) {
                userUpdate.setProvince(user.getProvince());
                needUpdateUser = true;
            }

            if (user.getCityId() != null) {
                userUpdate.setCityId(user.getCityId());
                needUpdateUser = true;
            }

            if (user.getCity() != null) {
                userUpdate.setCity(user.getCity());
                needUpdateUser = true;
            }

            if (user.getAreaId() != null) {
                userUpdate.setAreaId(user.getAreaId());
                needUpdateUser = true;
            }

            if (user.getArea() != null) {
                userUpdate.setArea(user.getArea());
                needUpdateUser = true;
            }
        }

        if (needUpdateUser) {
            //更新数据库记录
            userService.updateUserByUserId(userUpdate);
        }

        //step2：若职业不为空，更新职业信息
        updateJob(jobStrings);

        //step3：若兴趣不为空，更新兴趣信息
        updateInterest(interestStrings);

        // 更新喜爱夜蒲类型
        updateFancy(fancyStrings);

        //个人信息更新后把所有信息重新返回给app,用于刷新界面
        return getInfo();
    }

    @Override
    public Object updatePassword(String oldPassword, String password) {
        logger.info("UserControllerImpl updatePassword begin...");
        if (checkParaNULL(password)) {
            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                // 判断旧密码是否正确
                boolean right = userBaseService.checkOldPassword(shiroService.getLoginUserBase().getAccount(), oldPassword);
                if (right){
                    /** 更新蒲蒲团的账户的密码*/
                    userBaseService.updatePassword(shiroService.getLoginUserBase().getAccount(), password);
                    /** 提交事务 */
                    transactionHelper.commit(transactionStatus);
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }else {
                    transactionHelper.rollback(transactionStatus);
                    return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
                }

            } catch (Exception e) {
                logger.error("UserControllerImpl updatePassword error " + e);
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object search(String identify) {
        logger.info("UserControllerImpl search begin...");
        if (checkParaNULL(identify)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
//            param.put("nickName", name);
            if(identify.length() == 11){
                param.put("phone", "+86" + identify);
            }else{
                param.put("identify", identify);
            }
            User user = userService.getUserByParam(param);
            if (null == user) {
                return sendResult(ResultCode.CODE_722.code, ResultCode.CODE_722.msg, resultMap);
            }

            if(identify.length() == 11){
                param.clear();
                param.put("userId", user.getUserId());
                UserSetting userSetting = userSettingService.getSetting(param);
                // 判断当前手机号码是否进行屏蔽
                if(null != userSetting && "0".equals(userSetting.getShieldPhoneOn())){
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
                }
            }

            //获取头像
//            ossFileUtils.getUserHead(user, null);
            //获取图片
            ossFileUtils.getPhotoList(user.getAlbum(), null);
            //查询用户信息
            // TODO test by simon at 2016/08/03
            //resultMap.put("object", user);
            this.setUserInterestAndJob(user);
            JSONUser jsonUser = convertDBModelToJSONModel(user);
            resultMap.put("object", jsonUser);

            param.clear();
            //查询是否是好友
            param.put("userId", user.getUserId());
            param.put("friendId", shiroService.getLoginUserBase().getId());
            FriendRelationship friendRelationship = friendRelationshipService.getFriendRelationship(param);
            if (friendRelationship != null) {
                resultMap.put("isFriend", true);
            } else {
                resultMap.put("isFriend", false);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object searchByUserId(Long userId) {
        if (checkParaNULL(userId)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            User user = userService.getByUserId(userId);
            //获取头像
            ossFileUtils.getUserHead(user, null);
            //查询用户信息
            resultMap.put("object", user);

            //添加该用户被浏览记录
            BeViewed beViewed = new BeViewed();
            beViewed.setUserId(shiroService.getLoginUserBase().getId());
            beViewed.setBeViewUserId(userId);
            beViewed.setType(0);
            beViewedService.addBeViewed(beViewed);

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object searchAllInterest() {

        Map<String, Object> resultMap = new HashMap<>();

        List<Interest> list = interestService.getInterestWithSystem(shiroService.getLoginUserBase().getId());

        resultMap.put("list", list);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }

    @Deprecated
    @Override
    public Object searchAllJob() {
        Map<String, Object> resultMap = new HashMap<>();

        List<Job> list = jobService.getJobWithSystem(shiroService.getLoginUserBase().getId());

        resultMap.put("list", list);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object bindPhone(String phone, String code, String password) {
        logger.info("UserControllerImpl bindPhone begin...");
        if (checkParaNULL(phone, code)) {
            Map<String, Object> param = new HashMap<>();

            if(configInfo.isSmsOpen()){

                //获取短信验证码信息
                param.put("account", phone);
                SmsCode smsCode = smsCodeService.getSmsCode(param);

                //如果短信验证码为空或者不等于用户提交的code，则提示验证码出错
                if (smsCode == null || !code.equals(smsCode.getCode())) {
                    return sendResult(ResultCode.CODE_802.code, ResultCode.CODE_802.msg, null);
                }
                //如果验证码已使用过，则提示重新获取
                if (smsCode.getType() == SmsTemplateType.HAVE_USE) {
                    return sendResult(ResultCode.CODE_803.code, ResultCode.CODE_803.msg, null);
                }
                //更新验证码为已使用
                smsCode.setType(SmsTemplateType.HAVE_USE);
                smsCodeService.updateSmsCode(smsCode);
            }

            // 判断密码是否正确
            boolean right = userBaseService.checkOldPassword(shiroService.getLoginUserBase().getAccount(), password);
            if(!right){
                return sendResult(ResultCode.CODE_701.code, ResultCode.CODE_701.msg, null);
            }

            param.clear();
            param.put("phone", phone);
            User user = userService.getUser(param);
            //如果该手机号没有被绑定，则可以继续绑定流程
            if (user == null) {
                user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                user.setPhone(phone);
                boolean result = userService.updateUser(user);
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_718.code, ResultCode.CODE_718.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    @Transactional(timeout = 5000)
    public Object bindWeChat(String wechatId) {
        logger.info("UserControllerImpl bindWeChat begin...");
        if (checkParaNULL(wechatId)) {
            Map<String, Object> param = new HashMap<>();
            param.put("wechatId", wechatId);
            User user = userService.getUser(param);
            //如果该微信号没有被绑定，则可以继续绑定流程
            if (user == null) {
                user = userService.getByUserId(shiroService.getLoginUserBase().getId());
                user.setWechatId(wechatId);
                boolean result = userService.updateUser(user);
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
            return sendResult(ResultCode.CODE_717.code, ResultCode.CODE_717.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Deprecated
    @Override
    public Object getUserSetting() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        Long userId = shiroService.getLoginUserBase().getId();
        param.put("userId", userId);
        UserSetting userSetting = userSettingService.getSetting(param);

        if (userSetting == null) {
            userSetting = new UserSetting();
            userSetting.setUserId(userId);
            userSetting.setNtyBySound(1);
            userSetting.setNtyByVibration(1);
            userSetting.setIsSpeakerOn(1);
            userSetting.setShowFriendsToStranger(1);

            if (!userSettingService.addSetting(userSetting)) {
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }

        resultMap.put("userSetting", userSetting);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Deprecated
    @Override
    public Object updateUserSetting(int ntyBySound, int ntyByVibration, int isSpeakerOn, int showFriendsToStranger) {
        //if (checkParaNULL(userSetting)) {
            Map<String, Object> param = new HashMap<>();
            Long userId = shiroService.getLoginUserBase().getId();
            param.put("userId", userId);
            UserSetting userSetting = userSettingService.getSetting(param);

            //UserSetting userSetting = new UserSetting();
            //userSetting.setUserId(shiroService.getLoginUserBase().getId());
            userSetting.setNtyBySound(ntyBySound);
            userSetting.setNtyByVibration(ntyByVibration);
            userSetting.setIsSpeakerOn(isSpeakerOn);
            userSetting.setShowFriendsToStranger(showFriendsToStranger);

            if (userSettingService.updateSetting(userSetting)) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
        //}
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object complainUser(String identify, Integer type, String content) {
        logger.info("UserControllerImpl complainUser begin...");
        if (checkParaNULL(identify, type)) {
            ComplainUser complainUser = new ComplainUser();

            Map<String, Object> param = new HashMap<>();
            param.put("identify", identify);
            User user = userService.getUser(param);
            if (user != null) {
                complainUser.setUserId(user.getUserId());
            }
            complainUser.setIdentify(identify);
            complainUser.setType(type);
            complainUser.setContent(content);
            complainUser.setCreateTime(new Date());

            boolean result = complainUserService.addComplainUser(complainUser);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object shieldPhoneOn(String ifOn) {
        logger.info("UserControllerImpl shieldPhoneOn begin...");
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        Long userId = shiroService.getLoginUserBase().getId();
        param.put("userId", userId);
        UserSetting userSetting = userSettingService.getSetting(param);

        if (userSetting == null) {
            userSetting = new UserSetting();
            userSetting.setUserId(userId);
            userSetting.setNtyBySound(1);
            userSetting.setNtyByVibration(1);
            userSetting.setIsSpeakerOn(1);
            userSetting.setShowFriendsToStranger(1);
            userSetting.setShieldPhoneOn(Integer.valueOf(ifOn));
            if (!userSettingService.addSetting(userSetting)) {
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }else{
            userSetting.setShieldPhoneOn(Integer.valueOf(ifOn));
            if (!userSettingService.updateSetting(userSetting)) {
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object ifShieldPhoneOn() {
        logger.info("UserControllerImpl ifShieldPhoneOn begin...");
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Long userId = shiroService.getLoginUserBase().getId();


        param.put("userId", userId);
        UserSetting userSetting = userSettingService.getSetting(param);
        if(null == userSetting){
           // 配置为空时，默认可见
            resultMap.put("ifOn", 1);
        }else if(userSetting.getShieldPhoneOn() == 1){
            resultMap.put("ifOn", 1);
        }else{
            resultMap.put("ifOn", 0);
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchUserByPhones(String[] phones) {
        logger.info("UserControllerImpl searchUserByPhones begin...");
        if(phones.length == 0){
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();


        int totalNum = phones.length;
        int perNum = 10;
        int circleNum = totalNum / perNum;
        int remainNum = totalNum % perNum;

        List<String> phoneList = Arrays.asList(phones);
        List<User> userList = new ArrayList<>();
        int index = 0;
        for(int i = 0; i < circleNum; i++){
            param.clear();
            param.put("phones", phoneList.subList(index, index + perNum));
            param.put("filterShieldPhone","true");
            userList.addAll(userService.getUserList(param));
            index = index + perNum;
        }

        if(remainNum > 0){
            param.clear();
            param.put("phones", phoneList.subList(index, phoneList.size()));
            param.put("filterShieldPhone","true");
            userList.addAll(userService.getUserList(param));
        }

        List<Map> resultMapList = new ArrayList<>();
        User currentUser = userService.getByUserId(shiroService.getLoginUserBase().getId());
        for(User user : userList){

            // 过滤被冻结的用户
            if(user.getUserBase().getFreeze() == 1){
                continue;
            }

            // 过滤本机号码
            if(currentUser.getPhone().equals(user.getPhone())){
                continue;
            }

            //获取头像
            ossFileUtils.getUserHead(user, null);
            Map<String, Object> map = new HashMap<>();
            map.put("phone", user.getPhone());
            map.put("user", ConvertBeanUtils.convertDBModelToJSONModel(user));
            param.clear();
            //查询是否是好友
            param.put("userId", user.getUserId());
            param.put("friendId", shiroService.getLoginUserBase().getId());
            FriendRelationship friendRelationship = friendRelationshipService.getFriendRelationship(param);
            if (friendRelationship != null) {
                map.put("isFriend", true);
            } else {
                map.put("isFriend", false);
            }
            resultMapList.add(map);
        }
        resultMap.put("userList", resultMapList);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchUserSuperLikeTimes() {
        logger.info("UserControllerImpl searchUserSuperLikeTimes begin...");

        int numOfSuperLike;
        List<UserSuperLike> userSuperLikes = userSuperLikeService.getTodaySuperLike(shiroService.getLoginUserBase().getId());
        UserSuperLikeConfig userSuperLikeConfig = userSuperLikeConfigService.getUserSuperLikeConfig(shiroService.getLoginUserBase().getId());

        if(null != userSuperLikeConfig){
            numOfSuperLike = userSuperLikeConfig.getTimes();
        }else {
            numOfSuperLike = Integer.valueOf(sysDicService.getSysDicByKey(NUM_SUPER_LIKE).getValue());
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userSuperLike", userSuperLikes.size());
        resultMap.put("superLikeConfig", numOfSuperLike);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }


    /**
     * 根据生日获取星座
     * <p/>
     * 魔羯座 (12/22 - 1/19) Capricorn
     * 水瓶座 (1/20 - 2/18) Aquarius
     * 双鱼座 (2/19 - 3/20) Pisces
     * 白羊座 (3/21 - 4/20) Aries
     * 金牛座 (4/21 - 5/20) Taurus
     * 双子座 (5/21 - 6/21) Gemini
     * 巨蟹座 (6/22 - 7/22) Cancer
     * 狮子座 (7/23 - 8/22) Leo
     * 处女座 (8/23 - 9/22) Virgo
     * 天秤座 (9/23 - 10/22) Libra
     * 天蝎座 (10/23 - 11/21) Scorpio
     * 射手座 (11/22 - 12/21) Sagittarius
     *
     * @param birthday
     * @return
     */
    public String getConstellation(Date birthday) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] starArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};

        int[] beginDay = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日--开始
        int[] endDay = {19, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};  // 两个星座分割日--结束

        // 所查询日期在分割日之后，索引+1，否则不变
        if (day > endDay[month]) {
            month = month + 1;
        }
        //当超过12月份的，则回到1月份
        if (month >= 0 && month <= 11) {
            return starArr[month];
        }
        // 返回索引指向的星座string
        return starArr[0];
    }


    // TODO test by simon at 2016/07/29
    private JSONUser convertDBModelToJSONModel(User user) {
        JSONUser jsonUser = new JSONUser();
        jsonUser.setUserId(user.getUserId());
        jsonUser.setIdentify(user.getIdentify());
        jsonUser.setNickName(EmojiCharacterUtil.emojiRecovery2(user.getNickName()));
        jsonUser.setNickNameChar(user.getNickNameChr());
        jsonUser.setSex(user.getSex());
        jsonUser.setHaveBindedWeChat(user.getWechatId() == null ? false : true);

//        IMUserBody iMUser = null;
//        try {
//            iMUser = HuanXinUserService.getIMUsersByUserName(user.getUserBase().getAccount());
//        } catch (HuanXinUserException e) {
//            e.printStackTrace();
//            logger.error("convertDBModelToJSONModel get HuanXin account fail with " + user.getUserBase().getAccount() + ".");
//
//            iMUser = new IMUserBody(user.getUserBase().getAccount(), MD5Utils.getMd5(user.getUserBase().getAccount()), null);
//        }
        ImUser imUser = new ImUser();
        imUser.setUserName(user.getUserBase().getAccount());
        imUser.setPassword(MD5Utils.getMd5(user.getUserBase().getAccount()));
        jsonUser.setImUser(imUser);

        jsonUser.setSign(user.getSign());
        jsonUser.setBirthday(user.getBirthday());
        jsonUser.setConstellation(user.getConstellation());

        if (user.getCity() == null) {
            jsonUser.setComeFrom(null);
        } else {
            ComeFrom comeFrom = new ComeFrom();
            comeFrom.setProvinceId(user.getProvinceId() !=null ? user.getProvinceId() : 0);
            comeFrom.setProvince(user.getProvince() !=null ? user.getProvince() : "");
            comeFrom.setAreaId(user.getAreaId() !=null ? user.getAreaId() : 0);
            comeFrom.setArea(user.getArea() !=null ? user.getArea() : "");
            comeFrom.setCityId(user.getCityId() !=null ? user.getCityId() : 0);
            comeFrom.setCity(user.getCity() !=null ? user.getCity() : "");
            jsonUser.setComeFrom(comeFrom);
        }

        if (user.getInterestList() != null && user.getInterestList().size() != 0) {
            List<JSONInterest> interestLists = new ArrayList<>();
            List<Interest> interests = user.getInterestList();
            for (Interest interest : interests) {
                if(null != interest.getId()){
                    JSONInterest temp1 = new JSONInterest();
                    temp1.setId(interest.getId());
                    temp1.setName(interest.getName());
                    temp1.setIsSelected(interest.getHaveSelect());
                    interestLists.add(temp1);
                }
            }
            jsonUser.setInterestList(interestLists);
        } else {
            jsonUser.setInterestList(null);
        }

        if (user.getJobList() != null && user.getJobList().size() != 0) {
            List<JSONJob> jobList = new ArrayList<>();
            List<Job> jobs = user.getJobList();
            for (Job job : jobs) {
                if(null != job.getId()){
                    JSONJob temp1 = new JSONJob();
                    temp1.setId(job.getId());
                    temp1.setName(job.getName());
                    temp1.setIsSelected(job.getHaveSelect());
                    jobList.add(temp1);
                }
            }
            jsonUser.setJobList(jobList);
        } else {
            jsonUser.setJobList(null);
        }

        if (user.getFancyList() != null && user.getFancyList().size() != 0) {
            List<JSONFancy> fancyList = new ArrayList<>();
            List<Fancy> fancies = user.getFancyList();
            for (Fancy fancy : fancies) {
                if(null != fancy.getId()){
                    JSONFancy temp1 = new JSONFancy();
                    temp1.setId(fancy.getId());
                    temp1.setName(fancy.getName());
                    temp1.setIsSelected(fancy.getHaveSelect());
                    fancyList.add(temp1);
                }
            }
            jsonUser.setFancyList(fancyList);
        } else {
            jsonUser.setFancyList(null);
        }

        //由于头像从相册中设置,所以这里最少会有一张照片
        List<AlbumPhoto> album = new ArrayList<>();
        List<Photo> photos = user.getAlbum();

        for (Photo photo : photos) {
            AlbumPhoto albumPhoto = new AlbumPhoto();
            albumPhoto.setAlbumPhotoId(photo.getId());
            albumPhoto.setImageId(photo.getImageId());
            albumPhoto.setImageUrl(photo.getImage().getUrl());
            albumPhoto.setIsAvatar(photo.getIsAvatar());
            album.add(albumPhoto);

            //如果图片被选为头像,则设置头像URL及ID
            if (photo.getIsAvatar()) {
                jsonUser.setAvatarId(photo.getId());
                jsonUser.setAvatarUrl(photo.getImage().getUrl());
            }
        }
        jsonUser.setAlbum(album);

        jsonUser.setFriendUpdateTime(user.getFriendUpdateTime());

        return jsonUser;
    }
    // TODO test end by simon at 2016/07/29

    //调用前需要确保user有userBase实例变量
    private Authorization getAuthorizationFromUser (User user) {
        Authorization authorization = new Authorization();
        authorization.setAccount(user.getPhone());
        authorization.setToken(user.getUserBase().getToken());
        return authorization;
    }

    /**
     * 检查短信验证码
     */
    private ResultCode checkSmsCode (String account, String code) {
        logger.info("UserControllerImpl checkSmsCode begin...");
        //获取短信验证码信息
        Map<String, Object> param = new HashMap<>();
        param.put("account", account);
        SmsCode smsCode = smsCodeService.getSmsCode(param);

        //如果短信验证码为空或者不等于用户提交的code，则提示验证码出错
        if (smsCode == null || !code.equals(smsCode.getCode())) {
            return ResultCode.CODE_802;
        }
        //如果验证码已使用过，则提示重新获取
        if (smsCode.getType() == SmsTemplateType.HAVE_USE) {
            return ResultCode.CODE_803;
        }
        //更新验证码为已使用
        smsCode.setType(SmsTemplateType.HAVE_USE);
        smsCodeService.updateSmsCode(smsCode);

        return ResultCode.CODE_200;
    }

    /**
     * 更新头像,返回对应的imageId
     */
    private Long updateAvatar(Long avatarId) {
        logger.info("UserControllerImpl updateAvatar begin...");
        Map<String, Object> param = new HashMap<>();
        param.put("id", avatarId);
        param.put("userId", shiroService.getLoginUserBase().getId());
        Photo photo = photoService.getPhoto(param);
        //输入的avatarId错误,返回失败响应
        if (null == photo) {
            return null;
        }

        //如果当前相片已经被设置为头像,则不再重新设置,否则找原来的头像照片设置为非头像
        if (!photo.getIsAvatar()) {
            param.clear();
            param.put("isAvatar", true);
            param.put("userId", shiroService.getLoginUserBase().getId());
            Photo photoAvatar = photoService.getPhoto(param);
            //有找到当前头像照片,则设置为非头像.如果找不到就直接将photo设为头像
            if (null != photoAvatar) {
                photoAvatar.setIsAvatar(false);
                photoService.updatePhoto(photoAvatar);
            }

            photo.setIsAvatar(true);
            photoService.updatePhoto(photo);
        }

        return photo.getImageId();
    }

    /**
     * 更新职业信息
     */
    private void updateJob(String[] jobStrings) {
        logger.info("UserControllerImpl updateJob begin...");
        Map<String, Object> param = new HashMap<>();
        if (jobStrings != null && jobStrings.length != 0) {
            //删除之前相关的jobUser
            jobUserService.removeJobUserByUserId(shiroService.getLoginUserBase().getId());

            for (String jobName : jobStrings) {
                param.put("name", jobName);
                Job jobFromDB = jobService.getJob(param);
                JobUser jobUser = new JobUser();
                jobUser.setUserId(shiroService.getLoginUserBase().getId());
                if (jobFromDB != null) {
                    //step2.1：如果该职业在数据库已存在，使用该职业的id
                    jobUser.setJobId(jobFromDB.getId());
                } else {
                    //step2.2：如果该职业在数据库不存在，则新建一个
                    Job job = new Job();
                    job.setName(jobName);
                    job.setIdentify(application.getBASE_JOB_IDENTIFY());
                    //用户创建的职业
                    job.setType(JobTypeAndStatus.USER_TYPE);
                    //该职业状态--开放
                    job.setStatus(JobTypeAndStatus.OPEN_STATUS);
                    jobService.addJob(job);
                    jobUser.setJobId(job.getId());
                }
                try {
                    //step2.3：添加职业信息
                    jobUserService.addJobUser(jobUser);
                } catch (DuplicateKeyException e) {
                    if (e.getMessage().contains("uq_job_relationship_user_idx_job_user")) {
                        System.out.println("\n重复添加职业，不做处理");
                    }
                }
            }
        }
    }

    /**
     * 更新兴趣信息
     */

    private void updateInterest(String[] interestStrings) {
        logger.info("UserControllerImpl updateInterest begin...");
        Map<String, Object> param = new HashMap<>();
        if (interestStrings != null && interestStrings.length != 0) {
            //删除之前相关的interestUser
            interestUserService.removeInterestUserByUserId(shiroService.getLoginUserBase().getId());

            for (String interestName : interestStrings) {
                param = new HashMap<>();
                param.put("name", interestName);
                Interest interestFromDB = interestService.getInterest(param);
                InterestUser interestUser = new InterestUser();
                interestUser.setUserId(shiroService.getLoginUserBase().getId());
                if (interestFromDB != null) {
                    //step3.1：如果该兴趣在数据库已存在，使用该兴趣的id
                    interestUser.setInterestId(interestFromDB.getId());
                } else {
                    //step3.2：如果该兴趣在数据库不存在，则新建一个
                    Interest interest = new Interest();
                    interest.setName(interestName);
                    interest.setIdentify(application.getBASE_INTERSET_IDENTIFY());
                    //用户创建的兴趣
                    interest.setType(InterestTypeAndStatus.USER_TYPE);
                    //该兴趣状态--开放
                    interest.setStatus(InterestTypeAndStatus.OPEN_STATUS);
                    interestService.addInterest(interest);
                    interestUser.setInterestId(interest.getId());
                }
                try {
                    //step3.3：添加兴趣信息
                    interestUserService.addInterestUser(interestUser);
                } catch (DuplicateKeyException e) {
                    if (e.getMessage().contains("uq_interset_user_idx_interset_id_user_id")) {
                        System.out.println("\n重复添加兴趣，不做处理");
                    }
                }
            }
        }
    }

    /**
     * 更新喜爱夜蒲类型
     */

    private void updateFancy(String[] fancyStrings) {
        logger.info("UserControllerImpl updateFancy begin...");
        Map<String, Object> param = new HashMap<>();
        if (fancyStrings != null && fancyStrings.length != 0) {
            //删除之前相关的interestUser
            fancyUserService.removeFancyUserByUserId(shiroService.getLoginUserBase().getId());

            for (String fancyName : fancyStrings) {

                Fancy fancyFromDB = fancyService.getFancyByName(fancyName);
                FancyUser fancyUser = new FancyUser();
                fancyUser.setUserId(shiroService.getLoginUserBase().getId());

                if (fancyFromDB != null) {
                    // 如果该类型在数据库已存在，使用该类型的id
                    fancyUser.setFancyId(fancyFromDB.getId());

                } else {
                    // 如果该类型在数据库不存在，则新建一个
                    Fancy fancy = new Fancy();
                    fancy.setName(fancyName);
                    fancy.setIdentify(application.getBASE_FANCY_IDENTIFY());
                    // 用户自建
                    fancy.setType(InterestTypeAndStatus.USER_TYPE);
                    // 状态--开放
                    fancy.setStatus(InterestTypeAndStatus.OPEN_STATUS);
                    fancyService.addFancy(fancy);
                    fancyUser.setFancyId(fancy.getId());
                }
                try {
                    //添加喜爱类型
                    fancyUserService.addFancyUser(fancyUser);

                } catch (DuplicateKeyException e) {

                    System.out.println("\n重复添加喜爱类型，不做处理");
                }
            }
        }
    }
}
