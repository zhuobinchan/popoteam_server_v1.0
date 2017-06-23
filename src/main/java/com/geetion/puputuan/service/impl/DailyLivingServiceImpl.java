package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.DailyLivingDAO;
import com.geetion.puputuan.pojo.DailyLivingStatisData;
import com.geetion.puputuan.service.DailyLivingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/28.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class DailyLivingServiceImpl implements DailyLivingService {

    @Resource
    private DailyLivingDAO dailyLivingDAO;

    @Override
    public PagingResult<DailyLivingStatisData> getDailyLivingStaticDataByParamPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<DailyLivingStatisData> list = dailyLivingDAO.selectDailyLivingSum(pageEntity.getParam());
        PageInfo<DailyLivingStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    public List<DailyLivingStatisData> getDailyLivingStaticDataByParam(Map param) {
        return dailyLivingDAO.selectDailyLivingSum(param);
    }

    @Override
    public Workbook exportDailyLivingStaticDataToWorkBook(List<DailyLivingStatisData> dailyLivingStatisDatas) {
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("活跃量统计");

        int dateTitleRowCount = 0;
        Row dateTitleRow = sheet.createRow(0);
        Row idsTitleRow = sheet.createRow(1);
        for (DailyLivingStatisData dailyLivingStatisData:dailyLivingStatisDatas) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0,0,dateTitleRowCount,dateTitleRowCount+1);
            sheet.addMergedRegion(cellRangeAddress);
            Cell deteTitleCell = dateTitleRow.createCell(dateTitleRowCount);
            deteTitleCell.setCellValue(dailyLivingStatisData.getDate());//设置单元格的日期标题

            Cell userIdsTitleCell = idsTitleRow.createCell(dateTitleRowCount);
            userIdsTitleCell.setCellValue("用户ID");
            Cell groupIdsTitleCell = idsTitleRow.createCell(dateTitleRowCount+1);
            groupIdsTitleCell.setCellValue("队伍ID");

            int userIdsRowCount = 2;
            for (String userId:dailyLivingStatisData.getUserIds()) {
                Row userIdsRow = sheet.getRow(userIdsRowCount);
                if (null==userIdsRow)
                    userIdsRow = sheet.createRow(userIdsRowCount);
                Cell userIdCell = userIdsRow.createCell(dateTitleRowCount);
                userIdCell.setCellValue(userId);
                userIdsRowCount++;
            }
            int groupIdsRowCount = 2;
            for (String groupId:dailyLivingStatisData.getGroupIds()) {
                Row groupIdsRow = sheet.getRow(groupIdsRowCount);
                if (null == groupIdsRow)
                    groupIdsRow = sheet.createRow(groupIdsRowCount);
                Cell groupIdCell = groupIdsRow.createCell(dateTitleRowCount+1);
                groupIdCell.setCellValue(groupId);
                groupIdsRowCount++;
            }
            dateTitleRowCount = dateTitleRowCount +2;
        }
        return wb;
    }
}
