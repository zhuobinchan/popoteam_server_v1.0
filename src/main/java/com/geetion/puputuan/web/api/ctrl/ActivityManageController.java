package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 后台 用户管理 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/activity")
public interface ActivityManageController {

    /**
     * 查询约会
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，根据主键ID查询数据；
     *                          -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id                配合methodType = 2使用
     * @param pageEntity
     * @param object            User 的字段;  //可选，不填则默认选择所有
     * @param activity          Activity 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                  @ModelAttribute User object, @ModelAttribute Activity activity, String groupName, Date registerTimeBegin, Date registerTimeEnd);

    @RequestMapping(value = "/searchActivityStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchActivityStatisData(Integer methodType, Integer type, @ModelAttribute Activity activity, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(HttpServletRequest request, HttpServletResponse response, Integer type, String exportType);

    @RequestMapping(value = "/exportActivity", method = RequestMethod.GET)
    void exportActivity(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, String groupName, @ModelAttribute Activity activity, Date registerTimeBegin, Date registerTimeEnd, String age);
    /**
     * 更新约会
     * @param activity
     * @return
     */
    @RequestMapping(value = "/updateActivity", method = RequestMethod.POST)
    @ResponseBody
    Object updateActivity(@ModelAttribute Activity activity);

    /**
     * 强制解散
     * @param activityIds
     * @return
     */
    @RequestMapping(value = "/dissolution", method = RequestMethod.POST)
    @ResponseBody
    Object dissolution(Long[] activityIds);

    @RequestMapping(value = "/searchActivitySumStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchActivitySumStatisData();

    @RequestMapping(value = "/searcnActivitySumForBar", method = RequestMethod.GET)
    @ResponseBody
    Object searcnActivitySumForBar(Integer type, Date beginTime, Date endTime);
}
