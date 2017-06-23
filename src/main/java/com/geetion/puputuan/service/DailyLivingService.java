package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.pojo.DailyLivingStatisData;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.List;

/**
 * Created by chenzhuobin on 2017/3/28.
 */
public interface DailyLivingService {
    PagingResult<DailyLivingStatisData> getDailyLivingStaticDataByParamPage(PageEntity pageEntity);
    List<DailyLivingStatisData> getDailyLivingStaticDataByParam(Map param);

    Workbook exportDailyLivingStaticDataToWorkBook(List<DailyLivingStatisData> dailyLivingStatisDatas);
}
