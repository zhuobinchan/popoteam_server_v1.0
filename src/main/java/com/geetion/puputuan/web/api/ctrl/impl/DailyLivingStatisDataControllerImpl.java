package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.pojo.DailyLivingStatisData;
import com.geetion.puputuan.service.DailyLivingService;
import com.geetion.puputuan.utils.ExcelUtil;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.DailyLivingStatisDataController;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by chenzhuobin on 2017/3/28.
 */@Controller
public class DailyLivingStatisDataControllerImpl extends BaseController implements DailyLivingStatisDataController {
    @Resource
    DailyLivingService dailyLivingService;

    @Override
    public Object searchDailyLivingStatisData(Integer methodType, Integer type, Date dailyLivingTimeBegin, Date dailyLivingTimeEnd, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("type", type);
            param.put("dailyLivingTimeBegin", dailyLivingTimeBegin);
            param.put("dailyLivingTimeEnd", dailyLivingTimeEnd);
            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<DailyLivingStatisData> pagingForKeyword = dailyLivingService.getDailyLivingStaticDataByParamPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingForKeyword);
                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
    @Override
    @Deprecated
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, Integer type) {
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
            headNames.add(new String[] { "日期", "用户活跃量", "群组活跃量" });
            List<String[]> fieldNames = new ArrayList<String[]>();
            fieldNames.add(new String[] { "date", "userDailyLivingTotal", "groupDailyLivingTotal"});

            List<DailyLivingStatisData> dailyLivingStatisDatas = dailyLivingService.getDailyLivingStaticDataByParam(param);
            LinkedHashMap<String, List> map = new LinkedHashMap<>();
            map.put("基本统计", dailyLivingStatisDatas);

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
    public void newExportExcel(HttpServletRequest request, HttpServletResponse response, Integer type, Date dailyLivingTimeBegin, Date dailyLivingTimeEnd) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("dailyLivingTimeBegin", dailyLivingTimeBegin);
        param.put("dailyLivingTimeEnd", dailyLivingTimeEnd);

        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        List<DailyLivingStatisData> dailyLivingStatisDatas = dailyLivingService.getDailyLivingStaticDataByParam(param);

        Workbook wb = dailyLivingService.exportDailyLivingStaticDataToWorkBook(dailyLivingStatisDatas);

        try {
            OutputStream baos = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();            //生成流对象
            wb.write(byteArrayOutputStream);                                //将excel写入流
            baos.write(byteArrayOutputStream.toByteArray());
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.debug("文件生成...");
    }
}
