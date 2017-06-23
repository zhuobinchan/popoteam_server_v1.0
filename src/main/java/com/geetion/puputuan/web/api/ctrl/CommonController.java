package com.geetion.puputuan.web.api.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jian on 16/3/15.
 */
public interface CommonController {


    //================================= 地区模块 =======================================

    /**
     * 获取地区数据
     *
     * @param code 父code,获取省数据时可以为空
     * @param type 1:省,2:市,3:区
     * @return
     */
    @RequestMapping(value = "/ctrl/common/getDistrict", method = RequestMethod.GET)
    @ResponseBody
    Object getDistrict(Integer code, Integer type);

    //================================== 图片模块 ======================================

    /**
     * 批量文件上传
     *
     * @param files 文件
     * @return
     */
    @RequestMapping(value = "/ctrl/common/upload", method = RequestMethod.POST)
    @ResponseBody
    Object upload(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes);


    /**
     * 获取文件或者图片
     *
     * @param fileIds 文件id
     * @return
     */
    @RequestMapping(value = "/ctrl/common/download", method = RequestMethod.GET)
    @ResponseBody
    Object download(Long[] fileIds);

    /**
     * 获取裁剪图片
     *
     * @param fileIds
     * @return
     */
    @RequestMapping(value = "/ctrl/common/getPictures", method = RequestMethod.GET)
    @ResponseBody
    Object getPictures(Long[] fileIds, String style);

    /**
     * 获取裁剪图片
     *
     * @param fileId
     * @param style
     * @return
     */
    @RequestMapping(value = "/ctrl/common/redirectPicture", method = RequestMethod.GET)
    String redirectPicture(Long fileId, String style);


    //================================== 其他 ======================================

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    ModelAndView login();

    /**
     * 管理员登录
     *
     * @param account
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    ModelAndView login(String account, String password);


    /**
     * 管理员登出
     *
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    ModelAndView logout();

    @RequestMapping(value="/ctrl/common/uploadRobotConf", method=RequestMethod.POST )
    @ResponseBody
    Object uploadRobotConf(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    ModelAndView index();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    Object test(String teamId);

    @RequestMapping(value = "/ctrl/common/clearGroupsHuanXin", method = RequestMethod.POST)
    @ResponseBody
    Object clearGroupsHuanXin(@RequestParam("uploadfiles") CommonsMultipartFile[] files);

    @RequestMapping(value = "/ctrl/common/clearUsersHuanXin", method = RequestMethod.POST)
    @ResponseBody
    Object clearUsersHuanXin(@RequestParam("uploadfiles") CommonsMultipartFile[] files, String cursor);

    @RequestMapping(value="/ctrl/common/uploadFriendFile", method=RequestMethod.POST )
    @ResponseBody
    Object uploadFriendFile(@RequestParam("friendFile") CommonsMultipartFile[] files, Long[] sizes, Integer userType, Integer actionType);

    @RequestMapping(value="/ctrl/common/getBarList", method=RequestMethod.GET )
    @ResponseBody
    Object getBarList();

    @RequestMapping(value="/ctrl/common/uploadGroupFile", method=RequestMethod.POST )
    @ResponseBody
    Object uploadGroupFile(@RequestParam("groupFile") CommonsMultipartFile[] files, Long[] sizes, Integer userType, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Long barId, String sex);
}
