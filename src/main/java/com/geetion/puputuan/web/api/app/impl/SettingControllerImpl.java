package com.geetion.puputuan.web.api.app.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.sms.constants.SmsTemplate;
import com.geetion.generic.sms.pojo.SmsCode;
import com.geetion.generic.sms.service.MsgService;
import com.geetion.generic.sms.service.SmsCodeService;
import com.geetion.puputuan.common.constant.EvaluateType;
import com.geetion.puputuan.common.constant.SmsTemplateType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.app.SettingController;
import com.geetion.puputuan.web.api.app.SmsController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class SettingControllerImpl extends BaseController implements SettingController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private ConsultationService consultationService;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private AppSettingService appSettingService;
    @Resource
    private EvaluateService evaluateService;
    @Resource
    private MessageService messageService;
    @Resource
    private AppVersionService appVersionService;
    @Resource
    private BarService barService;
    @Resource
    private SysDicService sysDicService;

    //默认的首页轮播图的数量
    private int advertisementPages = 3;
    //默认的约会定位时间间隔，单位分钟
    private int positionInterval = 15;
    //默认的轮播间隔时间，单位秒
    private int carouselInterval = 3;

    @Override
    public Object search() {
        logger.info("SettingControllerImpl search begin...");
        Map<String, Object> resultMap = new HashMap<>();

        PageEntity pageEntity = new PageEntity();
        AppSetting appSetting = appSettingService.getAppSetting(null);
        if (appSetting != null && appSetting.getAdvertisementPages() != null) {
            pageEntity.setPage(1);
            pageEntity.setSize(appSetting.getAdvertisementPages());
        } else {
            pageEntity.setPage(1);
            //默认拿5张
            pageEntity.setSize(advertisementPages);
        }

        PagingResult<Consultation> pagingForKeyword = consultationService.getConsultationPage(pageEntity);
        //设置图片的链接
        if (checkParaNULL(pagingForKeyword, pagingForKeyword.getResultList())) {
            for (Consultation consultation : pagingForKeyword.getResultList()) {
                if (consultation.getImageId() != null) {
                    consultation.setImage(ossFileUtils.getPictures(consultation.getImageId(), null));
                }
            }
        }

        resultMap.put("list", pagingForKeyword.getResultList());
        resultMap.put("advertisementPages", appSetting != null && appSetting.getAdvertisementPages() != null ?
                appSetting.getAdvertisementPages() : advertisementPages);
        resultMap.put("carouselInterval", appSetting != null && appSetting.getCarouselInterval() != null ?
                appSetting.getCarouselInterval() : carouselInterval);
        resultMap.put("positionInterval", appSetting != null && appSetting.getActivityPositionInterval() != null ?
                appSetting.getActivityPositionInterval() : positionInterval);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }

    @Override
    public Object searchMessageAndEvaluateNum() {

        Map<String, Object> resultMap = new HashMap<>();

        //计算用户有几条未读的信息
        Message message = new Message();
        message.setIsRead(false);
        message.setUserId(shiroService.getLoginUserBase().getId());
        int unReadNum = messageService.countMessageNum(message);
        resultMap.put("unReadNum", unReadNum);

        //计算用户有几条未评价的消息
        Evaluate evaluate = new Evaluate();
        evaluate.setUserId(shiroService.getLoginUserBase().getId());
        evaluate.setIsDelete(EvaluateType.NO_DELETE);
        int unEvaluateNum = evaluateService.countEvaluateNum(evaluate);

        resultMap.put("unEvaluateNum", unEvaluateNum);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }

    @Override
    public Object searchAppVersion(String version) {

        logger.info("SettingControllerImpl searchAppVersion begin...");
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();

        if (checkParaNULL(version)){
            param.put("version", version);
            AppVersion currentApp = appVersionService.getAppVersion(param);
            param.clear();
            param.put("type", 1);
            AppVersion laterApp = appVersionService.getAppVersion(param) ;

            if (null == currentApp){
//                resultMap.put("appVersion", laterApp);
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
            }

            if (currentApp.getCreateTime().before(laterApp.getCreateTime())){
                resultMap.put("appVersion", laterApp);
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
            }

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, resultMap);
    }

    @Override
    public Object searchBar() {
        logger.info("SettingControllerImpl searchBar begin...");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        params.put("status", 1);
        List<Bar> barList = barService.getBarList(params);

        List<Map<String, Object>> barObjectList = new ArrayList<>();
        if(null != barList && barList.size() > 0){
            for(Bar bar : barList){
                Map<String, Object> map = new HashMap<>();
                map.put("id", bar.getId());
                map.put("name", bar.getName());
                map.put("content", bar.getContent());
                map.put("url", bar.getUrl());
                map.put("actionType", bar.getActionType());
                barObjectList.add(map);
            }
        }
        resultMap.put("barList", barObjectList);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchMallIfOpen() {
        Map<String, Object> resultMap = new HashMap<>();
        SysDic ifMallOpen = sysDicService.getSysDicByKey("IF_MALL_OPEN");
        if (null == ifMallOpen){
            resultMap.put("result", "false");
        }else if(ifMallOpen.getValue().equals("0")){
            resultMap.put("result", "false");
        }else {
            resultMap.put("result", "true");
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }
}
