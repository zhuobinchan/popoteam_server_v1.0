package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.FriendRelationship;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 后台 用户管理 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/group")
public interface GroupManageController {


    /**
     * 查询用户所在队伍，有效队伍
     * @param object
     * @return
     */
    @RequestMapping(value = "/searchGroupByUserId", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupByUserId(@ModelAttribute User object);

    /**
     * 查询队伍
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，根据主键ID查询数据；
     *                          -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id                配合methodType = 2使用
     * @param pageEntity
     * @param object            User 的字段;  //可选，不填则默认选择所有
     * @param groupIds        该字段是队伍的id组，用逗号作为分隔符可以通过逗号分隔进行批量查询队伍
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                  @ModelAttribute User object, @ModelAttribute Group group, Date registerTimeBegin, Date registerTimeEnd,String groupIds);

    @RequestMapping(value = "/dissolution", method = RequestMethod.POST)
    @ResponseBody
    Object dissolution(Long[] groupIds);

    @RequestMapping(value = "/searchGroupStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupStatisData(Integer methodType, Integer type, @ModelAttribute Group group, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/exportGroup", method = RequestMethod.GET)
    void exportGroup(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, @ModelAttribute Group group, Date registerTimeBegin, Date registerTimeEnd, String age);

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(HttpServletRequest request, HttpServletResponse response, Integer type, String exportType, String city);

    @RequestMapping(value = "/searchGroupSumStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupSumStatisData();

    @RequestMapping(value = "/searchGroupSumForCharts", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupSumForCharts(Integer type);

    @RequestMapping(value = "/searchGroupMemberByGroupId", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupMemberByGroupId(@ModelAttribute Group group);

    @RequestMapping(value = "/searcnGroupSumForBar", method = RequestMethod.GET)
    @ResponseBody
    Object searcnGroupSumForBar(Integer type, Date beginTime, Date endTime);

    /**
     * 通过上传的txt文件进行队伍的筛选
     * 导出的只是队伍的信息
     * @param request
     * @param response
     * @param file
     */
    @RequestMapping(value = "/exportGroupByFile", method = RequestMethod.POST)
    void exportGroupByFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("groupFile") CommonsMultipartFile file);

    @RequestMapping(value = "/exportGroupWithUserByFile", method = RequestMethod.POST)
    void exportGroupWithUserByFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("groupFile") CommonsMultipartFile file);


    @RequestMapping(value = "/searchGroupMemberHistoryByGroupIds", method = RequestMethod.POST)
    @ResponseBody
    Object searchGroupMemberHistoryByGroupIds(Long[] ids);

}
