package com.geetion.puputuan.web.api.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by jian on 25/3/16.
 */
public interface AppCommonController {

    /**
     * 获取地区数据
     *
     * @param code 父code,获取省数据时可以为空
     * @param type 1:省,2:市,3:区
     * @return
     */
    @RequestMapping(value = "/app/common/getDistrict", method = RequestMethod.GET)
    @ResponseBody
    Object getDistrict(Integer code, Integer type);


    //================================文件图片上下传==============================================


    /**
     * 批量文件上传
     *
     * @param files 文件
     * @return
     */
    @RequestMapping(value = "/app/common/upload", method = RequestMethod.POST)
    @ResponseBody
    Object upload(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes);


    /**
     * 获取文件或者图片
     *
     * @param fileIds 文件id
     * @return
     */
    @RequestMapping(value = "/app/common/download", method = RequestMethod.GET)
    @ResponseBody
    Object download(Long[] fileIds);

    /**
     * 获取裁剪图片
     *
     * @param fileIds
     * @return
     */
    @RequestMapping(value = "/app/common/getPictures", method = RequestMethod.GET)
    @ResponseBody
    Object getPictures(Long[] fileIds, String style);

    /**
     * 获取裁剪图片
     *
     * @param fileId
     * @param style
     * @return
     */
    @RequestMapping(value = "/app/common/redirectPicture", method = RequestMethod.GET)
    String redirectPicture(Long fileId, String style);

    //================================文件图片上下传==============================================
}
