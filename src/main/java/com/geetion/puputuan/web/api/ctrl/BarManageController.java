package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.model.Bar;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenzhuobin on 2017/3/27.
 */
@RequestMapping("/ctrl/bar")
public interface BarManageController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchBar(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                               @ModelAttribute Bar object);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateBar(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@RequestParam(value = "smallFile",required = false) CommonsMultipartFile smallFile, @ModelAttribute Bar bar);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    Object addBar(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@RequestParam(value = "smallFile",required = false) CommonsMultipartFile smallFile,@ModelAttribute Bar bar);

    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    Object setBarsStatus(Long[] ids,int status);

    @RequestMapping(value = "/setStatusScreen", method = RequestMethod.POST)
    @ResponseBody
    Object setStatusScreen(Long[] ids,Integer status,Integer groupHandle,Integer activityHandle);

    @RequestMapping(value = "/searchOtherBars", method = RequestMethod.POST)
    @ResponseBody
    Object searchOtherBars(Long[] ids,Integer status);


    @RequestMapping(value = "/setStatusScreen/GroupChgOtherBar", method = RequestMethod.POST)
    @ResponseBody
    Object setStatusScreenAndGroupChgOtherBar(Long[] barIds,Long otherBarId,Integer activityDissolve);


    @RequestMapping(value = "setBarStatusScreen/GroupWithActivityDissolve", method = RequestMethod.POST)
    @ResponseBody
    Object setStatusScreenAndGroupWithActivityDissolve(Long[] barIds,Integer activityDissolve) throws Exception;

}
