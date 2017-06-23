package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Location;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/activity")
public interface ActivityController {


    /**
     * 查询正在进行中的约会
     *
     * @return
     */
    @RequestMapping(value = "/runningActivity", method = RequestMethod.GET)
    @ResponseBody
    Object runningActivity(Integer methodType, @ModelAttribute PageEntity pageEntity);



    /**
     * 查询约会群
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/searchActivity", method = RequestMethod.GET)
    @ResponseBody
    Object searchActivity(Long activityId);

    /**
     * 查询约会的队友
     *
     * @return
     */
    @RequestMapping(value = "/member", method = RequestMethod.GET)
    @ResponseBody
    Object activityMember();


    /**
     * 更改约会信息
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateActivity(String groupName, String address, String area,
                          String locationName, Double longitude, Double latitude, Long barId);


    /**
     * 退出约会
     *
     * @return
     */
    @RequestMapping(value = "/quit", method = RequestMethod.POST)
    @ResponseBody
    Object quitActivity(Long activityId);




    /**
     * 查询可以评价的列表
     *
     * @return
     */
    @RequestMapping(value = "/searchEvaluate", method = RequestMethod.GET)
    @ResponseBody
    Object searchEvaluate(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity);


    /**
     * 评价约会的队友
     *
     * @return
     */
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    @ResponseBody
    Object evaluate(Long evaluateId, Boolean isLike);


    /**
     * 获得未评价的用户数量
     *
     * @return
     */
    @RequestMapping(value = "/unEvaluateNum", method = RequestMethod.GET)
    @ResponseBody
    Object unEvaluateNum();


}
