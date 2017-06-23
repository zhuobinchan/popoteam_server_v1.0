package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.AppSetting;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.MatchWeight;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 后台 管理员 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/setting")
public interface SettingManageController {


    /**
     * 查询客户端设置
     *
     * @return
     */
    @RequestMapping(value = "/searchApp", method = RequestMethod.GET)
    @ResponseBody
    Object searchAppSetting();


    /**
     * 更新客户端设置
     *
     * @return
     */
    @RequestMapping(value = "/updateApp", method = RequestMethod.POST)
    @ResponseBody
    Object updateAppSetting(@ModelAttribute AppSetting appSetting);



    /**
     * 查询约会判断设置
     *
     * @return
     */
    @RequestMapping(value = "/searchDate", method = RequestMethod.GET)
    @ResponseBody
    Object searchDateSetting();


    /**
     * 更新客户端设置
     *
     * @return
     */
    @RequestMapping(value = "/updateDate", method = RequestMethod.POST)
    @ResponseBody
    Object updateDateSetting(@ModelAttribute AppSetting appSetting);


    /**
     * 查询匹配权重设置
     *
     * @return
     */
    @RequestMapping(value = "/searchMatchWeight", method = RequestMethod.GET)
    @ResponseBody
    Object searchMatchWeightSetting();


    /**
     * 更新匹配权重设置
     *
     * @return
     */
    @RequestMapping(value = "/updateMatchWeight", method = RequestMethod.POST)
    @ResponseBody
    Object updateMatchWeightSetting(@ModelAttribute MatchWeight matchWeight);

    /**
     * 查询参数设置，superlike次数，推荐队伍次数
     *
     * @return
     */
    @RequestMapping(value = "/searchVarSetting", method = RequestMethod.GET)
    @ResponseBody
    Object searchVarSetting();

    /**
     * 更新参数设置
     *
     * @return
     */
    @RequestMapping(value = "/updateVarSetting", method = RequestMethod.POST)
    @ResponseBody
    Object updateVarSetting(String superlike, String groupNum);

    @RequestMapping(value = "/userSuperLikeConfig/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateUserSuperLikeConfig(Long id,Integer times);

    @RequestMapping(value = "/userSuperLikeConfig/add", method = RequestMethod.POST)
    @ResponseBody
    Object addUserSuperLikeConfig(String userIdentify ,String userPhone,Integer times);

    @RequestMapping(value = "/userSuperLikeConfig/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserSuperLikeConfig(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                                     @ModelAttribute User object);

    @RequestMapping(value = "/userSuperLikeConfig/delete", method = RequestMethod.POST)
    @ResponseBody
    Object deleteUserSuperLikeConfig(Long[] ids);
}
