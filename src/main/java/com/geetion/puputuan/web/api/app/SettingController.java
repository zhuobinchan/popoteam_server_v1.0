package com.geetion.puputuan.web.api.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 客户端设置 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/setting")
public interface SettingController {

    /**
     * 查询客户端的设置以及首页轮播图
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search();


    /**
     * 查询消息数量和评价数量
     * @return
     */
    @RequestMapping(value = "/searchMessageAndEvaluateNum", method = RequestMethod.GET)
    @ResponseBody
    Object searchMessageAndEvaluateNum();


    /**
     * 根据app传的版本号判断当前是否是最新的，如果不是，则返回最新的版本信息
     * @return
     */
    @RequestMapping(value = "/searchAppVersion", method = RequestMethod.GET)
    @ResponseBody
    Object searchAppVersion(String version);

    @RequestMapping(value = "/searchBar", method = RequestMethod.GET)
    @ResponseBody
    Object searchBar();

    @RequestMapping(value = "/searchMallIfOpen", method = RequestMethod.GET)
    @ResponseBody
    Object searchMallIfOpen();

}
