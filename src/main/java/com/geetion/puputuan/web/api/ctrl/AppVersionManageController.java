package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.AppVersion;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by chenzhuobin on 2017/3/29.
 */
@RequestMapping("/ctrl/app/version")
public interface AppVersionManageController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchAppVersion(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                               @ModelAttribute AppVersion object);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    Object addAppVersion(@RequestParam(value = "file",required = false) CommonsMultipartFile file, @ModelAttribute AppVersion appVersion);

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object deleteAppVersion(Long[] ids);

}
