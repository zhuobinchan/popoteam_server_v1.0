package com.geetion.puputuan.web.api.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 用户 接口
 *
 */
@RequestMapping("/app/advertise")
public interface AdvertisementController {


    /**
     * 查询广告消息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search();

    @RequestMapping(value = "/searchRecentAd", method = RequestMethod.GET)
    @ResponseBody
    Object searchRecentAd();

}
