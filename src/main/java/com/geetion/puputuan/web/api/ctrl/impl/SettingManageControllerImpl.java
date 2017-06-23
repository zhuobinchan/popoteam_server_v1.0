package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.DefaultSettingValue;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.web.api.app.ActivityController;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.DateManageController;
import com.geetion.puputuan.web.api.ctrl.SettingManageController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.persistence.Column;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class SettingManageControllerImpl extends BaseController implements SettingManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private UserBaseService userBaseService;
    @Resource
    private AppSettingService appSettingService;
    @Resource
    private MatchWeightService matchWeightService;
    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private UserSuperLikeConfigService userSuperLikeConfigService;
    @Resource
    private UserService userService;

    @Override
    public Object searchAppSetting() {
        AppSetting appSetting = appSettingService.getAppSetting(null);

        Map<String, Object> resultMap = new HashMap<>();

        //APP设置 -- 广告图页数上限
        resultMap.put("advertisementPages", appSetting == null ?
                DefaultSettingValue.advertisementPages : appSetting.getAdvertisementPages());
        //APP设置 -- 轮播间隔时间 -- 单位秒
        resultMap.put("carouselInterval", appSetting == null ?
                DefaultSettingValue.carouselInterval : appSetting.getCarouselInterval());

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object updateAppSetting(@ModelAttribute AppSetting appSetting) {
        if (checkParaNULL(appSetting)) {

            boolean result = false;
            //查询数据库是否有设置过了，无则新增，有则更新
            AppSetting appSettingFromDB = appSettingService.getAppSetting(null);
            if (appSettingFromDB == null) {
                result = appSettingService.addAppSetting(appSetting);
            } else {
                appSettingFromDB.setAdvertisementPages(appSetting.getAdvertisementPages());
                appSettingFromDB.setCarouselInterval(appSetting.getCarouselInterval());
                result = appSettingService.updateAppSetting(appSettingFromDB);
            }
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object searchDateSetting() {
        AppSetting appSetting = appSettingService.getAppSetting(null);
        Map<String, Object> resultMap = new HashMap<>();

        //约会判断设置 -- 定位时间间隔 -- 单位分钟
        resultMap.put("activityPositionInterval", appSetting == null ?
                DefaultSettingValue.activityPositionInterval : appSetting.getActivityPositionInterval());
        //约会判断设置 -- 新约会间隔时间 -- 单位小时
        resultMap.put("activityNewInterval", appSetting == null ?
                DefaultSettingValue.activityNewInterval : appSetting.getActivityNewInterval());
        //约会判断设置 -- 判断半径 -- 单位米
        resultMap.put("activityPositionRadius", appSetting == null ?
                DefaultSettingValue.activityPositionRadius : appSetting.getActivityPositionRadius());

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object updateDateSetting(@ModelAttribute AppSetting appSetting) {
        if (checkParaNULL(appSetting)) {

            boolean result = false;
            //查询数据库是否有设置过了，无则新增，有则更新
            AppSetting appSettingFromDB = appSettingService.getAppSetting(null);
            if (appSettingFromDB == null) {
                result = appSettingService.addAppSetting(appSetting);
            } else {
                appSettingFromDB.setActivityNewInterval(appSetting.getActivityNewInterval());
                appSettingFromDB.setActivityPositionInterval(appSetting.getActivityPositionInterval());
                appSettingFromDB.setActivityPositionRadius(appSetting.getActivityPositionRadius());
                result = appSettingService.updateAppSetting(appSettingFromDB);
            }
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchMatchWeightSetting() {
        MatchWeight matchWeight = matchWeightService.getMatchWeight(null);
        Map<String, Object> resultMap = new HashMap<>();

        //匹配权重 -- 相互点赞 -- 单位是百分比
        resultMap.put("mutualLike", matchWeight == null ?
                DefaultSettingValue.mutualLike : matchWeight.getMutualLike());
        //匹配权重 -- 关系度 -- 单位是百分比
        resultMap.put("relationship", matchWeight == null ?
                DefaultSettingValue.relationship : matchWeight.getRelationship());
        //匹配权重 -- 已投票结果 -- 单位是百分比
        resultMap.put("voteResult", matchWeight == null ?
                DefaultSettingValue.voteResult : matchWeight.getVoteResult());
        //匹配权重 -- 星座 -- 单位是百分比
        resultMap.put("constellation", matchWeight == null ?
                DefaultSettingValue.constellation : matchWeight.getConstellation());
        //匹配权重 -- 职业兴趣 -- 单位是百分比
        resultMap.put("interestionJob", matchWeight == null ?
                DefaultSettingValue.interestionJob : matchWeight.getInterestionJob());
        //匹配权重 -- 约会签到 -- 单位是百分比
        resultMap.put("dataSign", matchWeight == null ?
                DefaultSettingValue.dataSign : matchWeight.getDataSign());

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object updateMatchWeightSetting(@ModelAttribute MatchWeight matchWeight) {
        if (checkParaNULL(matchWeight)) {

            boolean result = false;
            //查询数据库是否有设置过了，无则新增，有则更新
            MatchWeight matchWeightFromDB = matchWeightService.getMatchWeight(null);
            if (matchWeightFromDB == null) {
                result = matchWeightService.addMatchWeight(matchWeight);
            } else {
                matchWeight.setId(matchWeightFromDB.getId());
                result = matchWeightService.updateMatchWeight(matchWeight);
            }
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchVarSetting() {

        SysDic numLocalGroup = sysDicService.getSysDicByKey("NUM_LOCAL_GROUP");
        SysDic numSuperLike = sysDicService.getSysDicByKey("NUM_SUPER_LIKE");

        Map<String, Object> resultMap = new HashMap<>();

        //推荐队伍数
        resultMap.put("groupNum", numLocalGroup == null ?
                0 : numLocalGroup.getValue());
        //superlike次数
        resultMap.put("superlike", numSuperLike == null ?
                0 : numSuperLike.getValue());

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object updateVarSetting(String superlike, String groupNum) {
        if(checkParaNULL(superlike)){

            SysDic numSuperLike = sysDicService.getSysDicByKey("NUM_SUPER_LIKE");
            //不更新该字段
            numSuperLike.setKey(null);
            numSuperLike.setValue(superlike);
            sysDicService.updateSysDic(numSuperLike);
        }
        if(checkParaNULL(groupNum)){
            SysDic numLocalGroup = sysDicService.getSysDicByKey("NUM_LOCAL_GROUP");
            //不更新该字段
            numLocalGroup.setKey(null);
            numLocalGroup.setValue(groupNum);
            sysDicService.updateSysDic(numLocalGroup);
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    @Override
    public Object updateUserSuperLikeConfig(Long id,Integer times){
        if (checkParaNULL(id,times)){
            UserSuperLikeConfig userSuperLikeConfig = userSuperLikeConfigService.getUserSuperLikeConfigById(id);
            userSuperLikeConfig.setTimes(times);
            userSuperLikeConfigService.updateUserSuperLikeConfig(userSuperLikeConfig);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object addUserSuperLikeConfig(String userIdentify ,String userPhone,Integer times) {
        if (checkParaNULL(times)&&(checkParaNULL(userIdentify)||checkParaNULL(userPhone))){
            Map<String, Object> param = new HashMap<>();
            if (checkParaNULL(userIdentify)){
                param.put("identify",userIdentify);
            }

            if (checkParaNULL(userPhone)){
                param.put("phone",userPhone);
            }
            User user =userService.getUserByParam(param);
            if (user == null){
                return sendResult(ResultCode.CODE_719.code, ResultCode.CODE_719.msg, null);
            }
            UserSuperLikeConfig object = new UserSuperLikeConfig();
            object.setUserId(user.getUserId());
            object.setTimes(times);
            try {
                userSuperLikeConfigService.addUserSuperLikeConfig(object);
            }catch (DuplicateKeyException e){
                logger.error("usersuperlikeconfig插入时，userid冲突,对象存在");
                return sendResult(ResultCode.CODE_501.code,ResultCode.CODE_501.msg,null);
            }

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchUserSuperLikeConfig(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<UserSuperLikeConfig> list =userSuperLikeConfigService.getUserSuperLikeConfigList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        UserSuperLikeConfig obj = userSuperLikeConfigService.getUserSuperLikeConfigById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null){
                        String identify = (String) param.get("identify");
                        if (checkParaNULL(identify)){
                            String[] identifies = identify.split(",");
                            param.put("identifies",identifies);
                            param.remove("identify");
                        }
                        pageEntity.setParam(param);
                    }
                    PagingResult<UserSuperLikeConfig> pagingForKeyword = userSuperLikeConfigService.getUserSuperLikeConfigPage(pageEntity);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object deleteUserSuperLikeConfig(Long[] ids) {
        if (checkParaNULL(ids)){
            for (Long id:ids) {
                userSuperLikeConfigService.removeUserSuperLikeConfig(id);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
}
