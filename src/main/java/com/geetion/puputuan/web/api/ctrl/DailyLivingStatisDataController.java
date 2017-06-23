package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by chenzhuobin on 2017/3/28.
 */
@RequestMapping("/ctrl/daily/living")
public interface DailyLivingStatisDataController {
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchDailyLivingStatisData(Integer methodType, Integer type, Date dailyLivingTimeBegin, Date dailyLivingTimeEnd, @ModelAttribute PageEntity pageEntity);

    @Deprecated
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    void exportExcel(HttpServletRequest request, HttpServletResponse response, Integer type);


    @RequestMapping(value = "/newExportExcel", method = RequestMethod.GET)
    void newExportExcel(HttpServletRequest request,HttpServletResponse response,Integer type,Date dailyLivingTimeBegin, Date dailyLivingTimeEnd);
}
