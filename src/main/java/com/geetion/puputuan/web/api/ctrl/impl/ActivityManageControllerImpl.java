package com.geetion.puputuan.web.api.ctrl.impl;


import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ActivityDetailWithCount;
import com.geetion.puputuan.pojo.ActivityStatisData;
import com.geetion.puputuan.pojo.GroupStatisData;
import com.geetion.puputuan.pojo.UserStatisData;
import com.geetion.puputuan.service.ActivityService;
import com.geetion.puputuan.service.GroupService;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.utils.ExcelUtil;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.ActivityManageController;
import com.geetion.puputuan.web.api.ctrl.GroupManageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class ActivityManageControllerImpl extends BaseController implements ActivityManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private ActivityService activityService;


    @Override
    public Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object,
                         @ModelAttribute Activity activity, String groupName, Date registerTimeBegin, Date registerTimeEnd) {

        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("registerTimeBegin", registerTimeBegin);
            param.put("registerTimeEnd", registerTimeEnd);


            switch (methodType) {
                case 1:

                    break;
                case 2:
                    param.putAll(pojoToMap(activity));
                    param.put("groupName", groupName);
                    if (pageEntity != null){
                        pageEntity.setParam(param);
                        PagingResult<ActivityDetailWithCount> activityPage = activityService.getActivityDetailWithCountPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, activityPage);
                    }
                    break;
                case 3:
                    param.putAll(pojoToMap(object));
                    if (pageEntity != null){
                        pageEntity.setParam(param);
                        PagingResult<Activity> activityPage = activityService.getActivityPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, activityPage);
                    }
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchActivityStatisData(Integer methodType, Integer type, @ModelAttribute Activity activity, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("type", type);
            switch (methodType) {
                case 1:

                    break;
                case 2:

                    if (pageEntity != null){
                        pageEntity.setParam(param);
                        PagingResult<ActivityStatisData> activityPage = activityService.getActivityStatisDataPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, activityPage);
                    }
                    break;
                case 3:

                    if (pageEntity != null){
                        pageEntity.setParam(param);
                        PagingResult<ActivityStatisData> activityPage = activityService.getActivityMatchStatisDataPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, activityPage);
                    }
                    break;
                case 4:

                    if (pageEntity != null){
                        pageEntity.setParam(param);
                        PagingResult<ActivityStatisData> activityPage = activityService.getActivityAreaStatisDataPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, activityPage);
                    }
                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, Integer type, String exportType) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);

        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = null;
        try
        {

            OutputStream baos = response.getOutputStream();
            List<String[]> headNames = new ArrayList<String[]>();
            List<String[]> fieldNames = new ArrayList<String[]>();
            LinkedHashMap<String, List> map = new LinkedHashMap<>();
            if ("1".equals(exportType)){
                headNames.add(new String[] { "日期", "约会总量", "SuperLike数", "约会平均人数"});
                fieldNames.add(new String[] { "date", "activityTotal", "activitySuperLike", "activityMemberAvg"});
                List<ActivityStatisData> groupStatisData = activityService.getActivityStatisData(param);
                map.put("基本统计", groupStatisData);
            }else if("2".equals(exportType)){
                headNames.add(new String[] { "日期", "约会总量", "同地区同类型", "同地区不同类型", "同市同类型","同市不同类型"});
                fieldNames.add(new String[] { "date", "activityTotal", "activitySasb", "activitySadb", "activityScsb","activityScdb"});
                List<ActivityStatisData> groupStatisData = activityService.getActivityMatchStatisData(param);
                map.put("按约会类型", groupStatisData);
            }else {
                headNames.add(new String[] { "日期", "约会总量","市", "同地区同类型", "同地区不同类型", "同市同类型","同市不同类型"});
                fieldNames.add(new String[] { "date", "activityTotal","activityCity", "activitySasb", "activitySadb", "activityScsb","activityScdb"});
                List<ActivityStatisData> groupStatisData = activityService.getActivityAreaStatisData(param);
                map.put("按约会地区", groupStatisData);
            }


            ExcelUtil.ExportSetInfo setInfo = new ExcelUtil.ExportSetInfo();
            setInfo.setObjsMap(map);
            setInfo.setFieldNames(fieldNames);
            setInfo.setHeadNames(headNames);
            setInfo.setOut(baos);

            // 将需要导出的数据输出到baos
            ExcelUtil.export2Excel(setInfo);

        }
        catch (UnsupportedEncodingException e1)
        {}
        catch (Exception e)
        {}
        finally
        {
            session.setAttribute("state", "open");
        }
        logger.debug("文件生成...");
    }

    @Override
    public void exportActivity(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, String groupName, @ModelAttribute Activity activity, Date registerTimeBegin, Date registerTimeEnd, String age) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        param.put("registerTimeBegin", registerTimeBegin);
        param.put("registerTimeEnd", registerTimeEnd);
        param.putAll(pojoToMap(activity));
        param.put("groupName", groupName);
        List<ActivityDetailWithCount> activityList = activityService.getActivityDetailWithCount(param);
        for(ActivityDetailWithCount ad : activityList){
            if(ad.getType() == 1){
                ad.setActivityStatus("解散");
            }else {
                ad.setActivityStatus("有效");
            }
        }
        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = null;
        try
        {

            OutputStream baos = response.getOutputStream();
            List<String[]> headNames = new ArrayList<String[]>();
            List<String[]> fieldNames = new ArrayList<String[]>();
            LinkedHashMap<String, List> map = new LinkedHashMap<>();

            headNames.add(new String[] { "Id", "约会名", "队伍A", "队伍B", "状态","是否SuperLike（0：否，1：是）","是否永久有效（0：否，1：是）","约会人数","创建日期"});
            fieldNames.add(new String[] { "id", "name", "groupAName", "groupBName", "activityStatus","superLike","isExpire","activityMemberCount","createTime"});
            map.put("约会数据", activityList);

            ExcelUtil.ExportSetInfo setInfo = new ExcelUtil.ExportSetInfo();
            setInfo.setObjsMap(map);
            setInfo.setFieldNames(fieldNames);
            setInfo.setHeadNames(headNames);
            setInfo.setOut(baos);

            // 将需要导出的数据输出到baos
            ExcelUtil.export2Excel(setInfo);

        }
        catch (UnsupportedEncodingException e1)
        {}
        catch (Exception e)
        {}
        finally
        {
            session.setAttribute("state", "open");
        }
        logger.debug("文件生成...");
    }


    @Override
    public Object updateActivity(@ModelAttribute Activity activity) {
        if(checkParaNULL(activity.getId())){
            activityService.updateActivity(activity);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object dissolution(Long[] activityIds) {
        if(checkParaNULL(activityIds)){
            for (Long id : activityIds){
                Activity activity = new Activity();
                activity.setId(id);
                activity.setType(ActivityTypeAndStatus.DISSOLUTION);
                activity.setExpireType(2);
                activity.setExpireTime(new Date());
                activityService.updateActivity(activity);
            }
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    @Override
    public Object searchActivitySumStatisData() {
        ActivityStatisData activitySumStatisData = activityService.getActivitySumStatisData();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("activitySumStatisData", activitySumStatisData);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searcnActivitySumForBar(Integer type, Date beginTime, Date endTime) {
        if (checkParaNULL(type, beginTime, endTime)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            param.put("beginTime", beginTime);
            param.put("endTime", endTime);
            List<ActivityStatisData> activitySumForBar = activityService.getActivitySumForBar(param);

            SimpleDateFormat formatter;
            Calendar cal=Calendar.getInstance();
            if(0 == type){
                formatter = new SimpleDateFormat ("yyyy-MM-dd");
            }else {
                formatter = new SimpleDateFormat ("yyyy-MM");
            }

            Map<String, Integer> bars = new HashMap<>();
            cal.setTime(beginTime);
            bars.put(formatter.format(beginTime), 0);
            while (beginTime.before(endTime)){
                if(0 == type){
                    cal.add(Calendar.DATE, +1);
                }else{
                    cal.add(Calendar.MONTH, +1);
                }
                beginTime = cal.getTime();

                bars.put(formatter.format(beginTime), 0);
            }

            for(ActivityStatisData u :activitySumForBar){
                if(bars.containsKey(u.getDate())){
                    bars.put(u.getDate(), u.getActivityTotal());
                }
            }
            TreeMap treemap = null;
            if(0 == type){
                treemap = new TreeMap(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // 降序排列
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = sdf.parse(o1.replace("\"", ""));
                            d2 = sdf.parse(o2.replace("\"", ""));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return d2.before(d1) ? 1 : -1;
                    }
                });


            }else {
                treemap = new TreeMap(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // 降序排列
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = sdf.parse(o1.replace("\"", ""));
                            d2 = sdf.parse(o2.replace("\"", ""));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return d2.before(d1) ? 1 : -1;
                    }
                });
            }

            treemap.putAll(bars);
            resultMap.put("item", treemap.keySet());
            resultMap.put("data", treemap.values());
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);    }
}
