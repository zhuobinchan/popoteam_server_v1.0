package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.model.FriendRelationship;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 后台 用户管理 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/user")
public interface UserManageController {


    /**
     * 查询用户
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，根据主键ID查询数据；
     *                          -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id                配合methodType = 2使用
     * @param pageEntity
     * @param object            User 的字段;  //可选，不填则默认选择所有
     * @param registerTimeBegin 用户注册的起始时间
     * @param registerTimeEnd   用户注册的截止时间
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                  @ModelAttribute User object, Date registerTimeBegin, Date registerTimeEnd, String age);


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, Date registerTimeBegin, Date registerTimeEnd, String age);


    /**
     * 通过上传的txt文件进行用户的筛选
     * @param request
     * @param response
     * @param file
     * @param userType 1 代表用户id identity 字段
     *                 2 代表用户手机号码
     */
    @RequestMapping(value = "/exportByFile", method = RequestMethod.POST)
    void exportByFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("userFile") CommonsMultipartFile file,Integer userType);



    /**
     * 查询用户的好友
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     User 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/searchFriend", method = RequestMethod.GET)
    @ResponseBody
    Object searchFriend(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                        @ModelAttribute FriendRelationship object);

    /**
     * 查询用户的相册
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Photo 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/searchPhoto", method = RequestMethod.GET)
    @ResponseBody
    Object searchPhoto(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                       @ModelAttribute Photo object, String style);


    /**
     * 批量冻结或解冻用户
     *
     * @param userIds 用户id
     * @param freeze  是否冻结，true表示冻结
     * @return
     */
    @RequestMapping(value = "/freezeUserOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object freezeUserOrNot(Long[] userIds, Boolean freeze);


    /**
     * 查询所有跟用户相关的统计字段
     *
     * @return
     */
    @RequestMapping(value = "/searchAllUserCount", method = RequestMethod.GET)
    @ResponseBody
    Object searchAllUserCount();


    /**
     * 查询被投诉的用户
     *
     * @param methodType         -- 1，不分页查询所有数据；
     *                           -- 2，根据主键ID查询数据；
     *                           -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id                 配合methodType = 2使用，这里是photoId
     * @param pageEntity
     * @param object             User 的字段;  //可选，不填则默认选择所有
     * @param releaseTimeBegin   投诉时间
     * @param releaseTimeEnd     投诉时间
     * @param complainTimesBegin 最少投诉次数
     * @param complainTimesEnd   最多投诉次数
     * @param age                年龄
     * @return
     */
    @RequestMapping(value = "/searchComplain", method = RequestMethod.GET)
    @ResponseBody
    Object searchComplain(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                          @ModelAttribute User object, Date releaseTimeBegin,
                          Date releaseTimeEnd, Integer complainTimesBegin,
                          Integer complainTimesEnd, String age, String style);


    /**
     * 删除被投诉的相册
     *
     * @param photoId
     * @return
     */
    @RequestMapping(value = "/deletePhoto", method = RequestMethod.POST)
    @ResponseBody
    Object deletePhoto(Long photoId);


    /**
     * 警告被投诉的用户
     *
     * @param photoId
     * @return
     */
    @RequestMapping(value = "/warnUser", method = RequestMethod.POST)
    @ResponseBody
    Object warnUser(Long photoId, Long userId);

    /**
     * 查询用户统计数据
     * @param methodType
     * @param type
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/searchUserStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserStatisData(Integer methodType, Integer type, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    void exportExcel(HttpServletRequest request, HttpServletResponse response, Integer type);

    @RequestMapping(value = "/searchUserInterestStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserInterestStatisData(Integer methodType, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/searchUserJobStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserJobStatisData(Integer methodType, @ModelAttribute PageEntity pageEntity);

    @RequestMapping(value = "/exportMonthData", method = RequestMethod.GET)
    void exportMonthData(HttpServletRequest request, HttpServletResponse response, Integer exportType);

    @RequestMapping(value = "/searcnUserSumStatisData", method = RequestMethod.GET)
    @ResponseBody
    Object searcnUserSumStatisData();

    @RequestMapping(value = "/searcnUserPermission", method = RequestMethod.GET)
    @ResponseBody
    Object searcnUserPermission();

    @RequestMapping(value = "/searcnUserSumForCharts", method = RequestMethod.GET)
    @ResponseBody
    Object searcnUserSumForCharts(Integer type);

    @RequestMapping(value = "/searcnUserSumForBar", method = RequestMethod.GET)
    @ResponseBody
    Object searcnUserSumForBar(Integer type, Date beginTime, Date endTime);

    @RequestMapping(value = "/searchUserBlackList", method = RequestMethod.GET)
    @ResponseBody
    Object searchUserBlackList(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                               @ModelAttribute User object);

    @RequestMapping(value = "/setUserBlackListStatus", method = RequestMethod.POST)
    @ResponseBody
    Object setUserBlackListStatus(Long[] ids,Integer status);

    /**
     * 通过用户的identity或者电话数组，进行添加用户
     * @param identifiesOrPhones
     * @param type
     * type = 1 代表 identity
     * type = 2 代表电话
     * @return
     */
    @RequestMapping(value = "addUserBlackLists" , method = RequestMethod.POST)
    @ResponseBody
    Object addUserBlackLists(String[] identifiesOrPhones , Integer type);


}
