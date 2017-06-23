package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 后台 公告及咨询 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/manage")
public interface ManageController {


    //============================================== 公告 ==============================================

    /**
     * 查询公告
     *
     * @param methodType    -- 1，不分页查询所有数据；
     *                      -- 2，根据主键ID查询数据；
     *                      -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id            配合methodType = 2使用
     * @param pageEntity
     * @param object        Announcement 的字段;  //可选，不填则默认选择所有
     * @param sendTimeBegin 公告发送的起始时间
     * @param sendTimeEnd   公告发送的截止时间
     * @return
     */
    @RequestMapping(value = "/announcement/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchAnnouncement(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                              @ModelAttribute Announcement object, Date sendTimeBegin, Date sendTimeEnd);

    /**
     * 发布公告
     *
     * @param announcement 公告详情
     * @param districts    地区
     * @param types        地区对应的级别，0代表省，1代表市，2代表区
     * @param editorArea 富文本编辑区，生成html网页
     * @return
     */
    @RequestMapping(value = "/announcement/release", method = RequestMethod.POST)
    @ResponseBody
    Object releaseAnnouncement(@ModelAttribute Announcement announcement, @RequestParam(value = "districts[]", required = false) Integer[] districts, @RequestParam(value = "types[]", required = false) Integer[] types, @RequestParam(value = "file" , required = false) CommonsMultipartFile imageFile,String editorArea);


    @RequestMapping(value="/announcement/uploadUserToRelease", method=RequestMethod.POST )
    @ResponseBody
    Object uploadUserToRelease(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes, @ModelAttribute Announcement announcement, String userType, @RequestParam(value = "imageFile", required = false) CommonsMultipartFile imageFile,String editorArea);

    /**
     * 开放或屏蔽公告
     *
     * @param announcementIds 公告id
     * @param isOpen          开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/announcement/openOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object openAnnouncementOrNot(Long[] announcementIds, Boolean isOpen);


    /**
     * 导入被推送的用户
     * @param announcementId
     * @return
     */
    @RequestMapping(value = "/announcement/exportUserById", method = RequestMethod.GET)
    void exportUserById(Long announcementId, HttpServletRequest request, HttpServletResponse response);

    @RequestMapping(value="/announcement/image/upload", method=RequestMethod.POST)
    @ResponseBody
    void uploadEditorAreaImage(@RequestParam(value = "image",required = false) CommonsMultipartFile imageFile,HttpServletRequest request, HttpServletResponse response);


    //============================================== 咨询 ==============================================

    /**
     * 查询资讯
     *
     * @param methodType    -- 1，不分页查询所有数据；
     *                      -- 2，根据主键ID查询数据；
     *                      -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id            配合methodType = 2使用
     * @param pageEntity
     * @param object        Consultation 的字段;  //可选，不填则默认选择所有
     * @param sendTimeBegin 资讯发送起始时间
     * @param sendTimeEnd   资讯发送的截止时间
     * @return
     */
    @RequestMapping(value = "/consultation/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchConsultation(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                              @ModelAttribute Consultation object, Date sendTimeBegin, Date sendTimeEnd,String style);

    /**
     * 发布资讯
     *
     * @param consultation 咨询详情
     * @return
     */
    @RequestMapping(value = "/consultation/release", method = RequestMethod.POST)
    @ResponseBody
    Object releaseConsultation(@ModelAttribute Consultation consultation);

    /**
     * 编辑资讯
     *
     * @param consultation 资讯详情
     * @return
     */
    @RequestMapping(value = "/consultation/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateConsultation(@ModelAttribute Consultation consultation);

    /**
     * 开放或屏蔽资讯
     *
     * @param consultationIds 资讯id
     * @param isOpen          开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/consultation/openOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object openConsultationOrNot(Long[] consultationIds, Boolean isOpen);


    //============================================== 兴趣标签 ==============================================


    /**
     * 查询兴趣
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Interest 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/interest/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchInterest(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                          @ModelAttribute Interest object);


    /**
     * 添加系统标签 -- 兴趣
     *
     * @param name 兴趣
     * @param status   开放0或屏蔽1
     * @return
     */
    @RequestMapping(value = "/interest/add", method = RequestMethod.POST)
    @ResponseBody
    Object addInterestLabel(String name, Integer status);

    /**
     * 开放或屏蔽标签 -- 兴趣
     *
     * @param interestIds 兴趣id
     * @param isOpen      是否屏蔽
     * @return
     */
    @RequestMapping(value = "/interest/openOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object openInterestLabelOrNot(Long[] interestIds, Boolean isOpen);

    /**
     * 编辑标签 -- 兴趣
     *
     * @param id 兴趣id
     * @param name       兴趣名字
     * @param status     开放1或屏蔽0
     * @return
     */
    @RequestMapping(value = "/interest/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateInterestLabel(Long id, String name, Integer status);

    //============================================== 职业标签 ==============================================


    /**
     * 查询职业
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Interest 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/job/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchJob(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                     @ModelAttribute Job object);

    /**
     * 添加系统标签 -- 职业
     *
     * @param name    兴趣
     * @param status 是否默认
     * @return
     */
    @RequestMapping(value = "/job/add", method = RequestMethod.POST)
    @ResponseBody
    Object addJobLabel(String name, Integer status);

    /**
     * 开放或屏蔽标签 -- 职业
     *
     * @param jobIds 兴趣id
     * @param isOpen 开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/job/openOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object openJobLabelOrNot(Long[] jobIds, Boolean isOpen);

    /**
     * 编辑标签 -- 职业
     *
     * @param id  职业id
     * @param name   职业名字
     * @param status 开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/job/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateJobLabel(Long id, String name, Integer status);


    //============================================== 喜爱夜蒲标签 ==============================================

    /**
     * 查询职业
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Interest 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/fancy/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchFancy(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                     @ModelAttribute Fancy object);

    /**
     * 添加系统标签 -- 职业
     *
     * @param name    兴趣
     * @param status 是否默认
     * @return
     */
    @RequestMapping(value = "/fancy/add", method = RequestMethod.POST)
    @ResponseBody
    Object addFancyLabel(String name, Integer status);

    /**
     * 开放或屏蔽标签 -- 职业
     *
     * @param fancyIds 兴趣id
     * @param isOpen 开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/fancy/openOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object openFancyLabelOrNot(Long[] fancyIds, Boolean isOpen);

    /**
     * 编辑标签 -- 职业
     *
     * @param id  职业id
     * @param name   职业名字
     * @param status 开放或屏蔽
     * @return
     */
    @RequestMapping(value = "/fancy/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateFancyLabel(Long id, String name, Integer status);


}
