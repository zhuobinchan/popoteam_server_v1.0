package com.geetion.puputuan.web.api.app.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.AdvertisementType;
import com.geetion.puputuan.common.constant.MessageType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.model.Notice;
import com.geetion.puputuan.service.AdvertisementService;
import com.geetion.puputuan.web.api.app.AdvertisementController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class AdvertisementControllerImpl extends BaseController implements AdvertisementController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private AdvertisementService advertisementService;

    @Override
    public Object search() {
        logger.info("AdvertisementControllerImpl search begin...");
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        //查询当前有效的广告列表
        param.put("status", AdvertisementType.STATUS_VALID);
        List<Advertisement> list = advertisementService.getAdvertisementList(param);
        resultMap.put("list", list);
        this.searchRecentAd();
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchRecentAd() {

        logger.info("AdvertisementControllerImpl searchRecentAd begin...");
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        //查询当前有效的广告列表
        param.put("status", AdvertisementType.STATUS_VALID);
        param.put("orderBySort", "asc");
        List<Advertisement> list = advertisementService.getAdvertisementList(param);
        resultMap.put("list", list);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }
}
