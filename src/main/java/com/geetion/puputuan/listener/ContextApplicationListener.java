package com.geetion.puputuan.listener;

import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinUserException;
import com.easemob.server.example.service.HuanXinUserService;
import com.geetion.generic.serverfile.oss.OssPermissionManager;
import com.geetion.generic.serverfile.pojo.OssAccessToken;
import com.geetion.generic.serverfile.utils.PathKit;
import com.geetion.generic.sms.service.MsgService;
import com.geetion.puputuan.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


@Service
public class ContextApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean isStart = false;


    @Autowired(required = true)
    private HttpServletRequest request;

    @Resource(name = "puputuanApplication")
    private Application application;

    @Resource
    private OssPermissionManager ossPermissionManager;

    private Map<String, String> token;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!isStart) {
            isStart = true;
            org.apache.ibatis.logging.LogFactory.useLog4JLogging();
//            org.apache.ibatis.logging.LogFactory.useNoLogging();
            /**
             * 每60分钟获取一次OSS有效令牌
             */
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("开始执行获取OSS有效令牌程序");
                        token = ossPermissionManager.doAction();
                        OssAccessToken accessToken = new OssAccessToken();
                        accessToken.setAccessKeyId(token.get("AccessKeyId"));
                        accessToken.setAccessKeySecret(token.get("AccessKeySecret"));
                        accessToken.setSecurityToken(token.get("SecurityToken"));
                        application.setOssAccessToken(accessToken);
//                         System.out.println("\n\n\nset完的token: \n"+accessToken.toString());
                    } catch (Exception e) {
                        System.out.println("注意!获取OSS有效令牌失败!  " + e.getMessage());
                    }
                }
            }, 100, 60 * 60 * 1000);

            /**
             * 每3个小时删除一次upload里面中转文件(更改时间大于30分钟)
             */
            Timer timerDelete = new Timer();
            final File uploadFile = new File(PathKit.getWebRootPath() + "/upload");
            timerDelete.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("开始执行缓存删除程序");
                    File[] lst = uploadFile.listFiles();
                    if (lst != null && lst.length > 0) {
                        for (File f : lst) {
                            if (new Date().getTime() - f.lastModified() > 30 * 60 * 1000) {
                                System.out.println("\n删除: " + f.getName());
                                f.delete();
                            }
                        }
                    }
                }
            }, 100, 1000 * 60 * 60 * 3);


            /**
             * 查询是否有环信系统用户，没有则创建一个。如果失败，则60秒后重试
             * 这个用户的用于系统的男女群聊群主
             */

            final Timer createHuanXinUserTimer = new Timer();
            createHuanXinUserTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        //查询是否有 SysUser 这个用户
                        Object responseWrapper = HuanXinUserService.getIMUsersFromHuanXin(HuanXinConstant.SYSUSER);

                        //有这个用户则取消定时器
                        if (responseWrapper != null) {
                            System.out.println("环信系统用户已存在");
                            createHuanXinUserTimer.cancel();
                        } else {
                            //没有这个用户则创建一个，创建成功则取消定时器，失败再等待一段时间继续创建
                            boolean result = HuanXinUserService.createNewIMUser(HuanXinConstant.SYSUSER, HuanXinConstant.SYSUSER_NICKNAME);
                            if (result) {
                                System.out.println("创建环信系统用户成功");
                                createHuanXinUserTimer.cancel();
                            }
                        }
                    } catch (HuanXinUserException e) {
                        e.printStackTrace();
                    }
                }
            }, 100, 60 * 1000);

            /**
             * 订阅淘宝开放平台消息服务,用于判断短信发送是否成功
             */
//            MsgService.subscribeMsg();

            //每次服务器启动时，从数据拿出最新一条 identity，无则使用默认的，持久化在内存中，给用户注册时使用
            application.setBASE_USER_IDENTIFY();
//            System.out.println("\n 设置用户的identity " + application.getBASE_USER_IDENTIFY());

            //每次服务器启动时，从数据拿出最新一条 identity，无则使用默认的，持久化在内存中，给添加兴趣时使用
            application.setBASE_INTERSET_IDENTIFY();
//            System.out.println("\n 设置兴趣的identity " + application.getBASE_INTERSET_IDENTIFY());

            //每次服务器启动时，从数据拿出最新一条 identity，无则使用默认的，持久化在内存中，给添加职业时使用
            application.setBASE_JOB_IDENTIFY();
//            System.out.println("\n 设置职业的identity " + application.getBASE_JOB_IDENTIFY());

            /**
             * 初始化APP设置和匹配权重等
             */
            application.initAppSetting();

            application.setMatcherOpen();

        }

    }
}
