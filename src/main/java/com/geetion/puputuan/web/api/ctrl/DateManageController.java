package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Admin;
import com.geetion.puputuan.model.Group;
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
@RequestMapping("/ctrl/date")
public interface DateManageController {


    /**
     * 查询同性群组
     *
     * @param methodType     -- 1，不分页查询所有数据；
     *                       -- 2，根据主键ID查询数据；
     *                       -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id             配合methodType = 2使用
     * @param pageEntity
     * @param object         Group 的字段;  //可选，不填则默认选择所有
     * @param groupTimeBegin 组队的起始时间
     * @param groupTimeEnd   组队的截止时间
     * @param number         人数
     * @return
     */
    @RequestMapping(value = "/searchGroup", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroup(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, Long userId,
                       @ModelAttribute Group object, Date groupTimeBegin, Date groupTimeEnd, String number);


    /**
     * 查询约会群组
     *
     * @param methodType     -- 1，不分页查询所有数据；
     *                       -- 2，根据主键ID查询数据；
     *                       -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id             配合methodType = 2使用
     * @param pageEntity
     * @param object         Group 的字段;  //可选，不填则默认选择所有
     * @param groupTimeBegin 组队的起始时间
     * @param groupTimeEnd   组队的截止时间
     * @param number         人数
     * @return
     */
    @RequestMapping(value = "/searchActivity", method = RequestMethod.GET)
    @ResponseBody
    Object searchActivity(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, Long userId,
                          @ModelAttribute Group object, Date groupTimeBegin, Date groupTimeEnd, String number);


}
