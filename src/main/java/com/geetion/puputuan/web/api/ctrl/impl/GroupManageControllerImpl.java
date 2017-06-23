package com.geetion.puputuan.web.api.ctrl.impl;


import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.HuanXinSendMessageType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.engine.thread.ShareCacheVar;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.GroupMemberHistory;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.*;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.GroupManageController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class GroupManageControllerImpl extends BaseController implements GroupManageController {

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
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMemberHistoryService groupMemberHistoryService;
    @Resource
    private TransactionHelper transactionHelper;
    @Resource
    private GroupManageService groupManageService;

    @Override
    public Object searchGroupByUserId(@ModelAttribute User object) {
        return null;
    }

    @Override
    public Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object, @ModelAttribute Group group, Date registerTimeBegin, Date registerTimeEnd,String groupIds) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            param.put("registerTimeBegin", registerTimeBegin);
            param.put("registerTimeEnd", registerTimeEnd);



            switch (methodType) {
                case 1:

                    break;
                case 2:
                    param.putAll(pojoToMap(group));
                    if (checkParaNULL(groupIds)){//批量获取
                        String[] groupIdArray = groupIds.trim().split(",");
                        param.put("ids",groupIdArray);
                    }

                    if(pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<GroupDetailWithCount> pagingGroupDetailResult = groupService.getGroupDetailCountPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingGroupDetailResult);

                    break;
                case 3:
                    param.putAll(pojoToMap(object));
                    if (pageEntity != null){
                        Map<String, Object> params = new HashMap<>();
                        params.put("groupMemberId", object.getUserId());
                        pageEntity.setParam(params);
                        PagingResult<Group> pagingResult = groupService.getGroupPage(pageEntity);
                        ResultUtils.setResultMap(resultMap, pagingResult);
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
    public Object dissolution(Long[] groupIds) {

        if(checkParaNULL(groupIds)){
            TransactionStatus transactionStatus = transactionHelper.start();
            for (int i = 0; i < groupIds.length; i++){
//                /** 开启事务 */
//                Group group = groupService.getGroupById(groupIds[i]);
//
//                try {
//                    group.setStatus(GroupTypeAndStatus.GROUP_DISSOLUTION);
//                    group.setModifyTime(new Date());
//                    groupService.updateGroup(group);
//                    recommendService.sumGroupRecommend(group.getId());
//
//                    //清除成员的userRecommend数据
//                    Map<String, Object> param = new HashMap<>();
//                    param.put("groupId", groupIds[i]);
//                    userRecommendService.removeUserRecommendByGroupId(param);
//
//                    //清除队伍的recommend success数据
//                    recommendSuccessService.removeRecommendSuccessByMainGroup(groupIds[i]);
//
//                    //清除队伍的recommend数据
//                    recommendService.removeMainIdRecommend(groupIds[i]);
//
////                userRecommendService.removeUserRecommendByGroupId(params);
//
//                    String[] groupMembers = commonService.getGroupMemberAccountOrToken(group.getId(), false, CommonService.USER_ACCOUNT);
//
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("groupId", group.getId());
//                    // 直接从成员表中删除
//                    groupMemberService.removeGroupMemberByParam(params);
//                    // 添加用户的群历史记录状态（退出）
////                addGroupMemberHistory(group.getId(), user.getUserId(),
////                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_QUIT);
//
//                    // 解散环信群
//                    HuanXinChatGroupService.deleteChatGroup(group.getRoomId());
//                    HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
//                    huanXinMessageExtras.setRoomId(group.getRoomId());
//                    HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "您好，您所在的队伍已经被队长解散。",
//                            HuanXinSendMessageType.GROUP_DISSOLUTION, huanXinMessageExtras,
//                            groupMembers);
//
//                } catch (HuanXinMessageException e) {
//                    logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
//                    /** 回滚事务 */
//                    transactionHelper.rollback(transactionStatus);
//                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
//                } catch (HuanXinChatGroupException e) {
//                    logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
//                    /** 回滚事务 */
//                    transactionHelper.rollback(transactionStatus);
//                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    logger.error("GroupControllerImpl dissolution error " + e.getStackTrace().toString());
//                    /** 回滚事务 */
//                    transactionHelper.rollback(transactionStatus);
//                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
//                }
//
//                runMatchUtil.cleanRedis(group.getId());
                try {
                    groupManageService.dissolutionGroup(groupIds[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("队伍解散失败");
                    transactionHelper.rollback(transactionStatus);
                }
            }
            transactionHelper.commit(transactionStatus);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_903.code, ResultCode.CODE_903.msg, null);
    }

    @Override
    public Object searchGroupStatisData(Integer methodType, Integer type, @ModelAttribute Group group,@ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("type", type);
            PagingResult<GroupStatisData> pagingForKeyword = null;
            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = groupService.getGroupStatisDataPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingForKeyword);
                    break;
                case 3:
                    param.putAll(pojoToMap(group));
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    pagingForKeyword = groupService.getGroupBarStatisDataPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingForKeyword);
                    break;
                case 4:
                    resultMap.put("list", groupService.getGroupRegionStatisData(null));
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public void exportGroup(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, @ModelAttribute Group group, Date registerTimeBegin, Date registerTimeEnd, String age) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        param.put("registerTimeBegin", registerTimeBegin);
        param.put("registerTimeEnd", registerTimeEnd);
        param.putAll(pojoToMap(group));

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
            List<GroupDetailWithCount> groupList = groupService.getGroupDetailCount(param);
            for(GroupDetailWithCount gs : groupList){
                if(gs.getType() == 0){
                    gs.setGroupType("男");
                }else {
                    gs.setGroupType("女");
                }

                if(gs.getStatus() == 1){
                    gs.setGroupStatus("有效");
                }else {
                    gs.setGroupStatus("无效");
                }

                gs.setBarName(gs.getBar().getName());
            }
            headNames.add(new String[] { "Id", "队名", "状态","性别","省", "市", "地区","类型","创建时间","修改日期","推荐队伍","组成约会数  "});
            fieldNames.add(new String[] { "id", "name", "groupStatus", "groupType", "province", "city","area", "barName","createTime","modifyTime","recommendCount","activityCount"});
            map.put("队伍信息", groupList);


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
    public void export(HttpServletRequest request, HttpServletResponse response, Integer type, String exportType, String city) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        if(checkParaNULL(city)){
            param.put("city", city);
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
            List<GroupStatisData> groupStatisData;
            switch (exportType){
                case "1":
                    headNames.add(new String[] { "日期", "队伍总量", "活跃队伍数", "男性队伍量", "男性占比","女性队伍量","女性占比","组成约会数","平均队伍人数"});
                    fieldNames.add(new String[] { "date", "groupTotal", "groupValidTotal", "maleTotal", "maleRatio","femaleTotal","femaleRatio","activityGroupTotal","groupMemberAvg"});
                    groupStatisData = groupService.getGroupStatisData(param);
                    map.put("基本统计", groupStatisData);
                    break;
                case "2":
                    headNames.add(new String[] { "日期", "队伍总量", "市","区","一起出去唱k", "今晚去蹦迪", "出去喝一杯","我们出去吧"});
                    fieldNames.add(new String[] { "date", "groupTotal", "groupCity", "groupArea", "groupBarOne", "groupBarTwo", "groupBarThree","groupBarFour"});
                    groupStatisData = groupService.getGroupBarStatisData(param);
                    map.put("按类型", groupStatisData);
                    break;
                case "3":
                    headNames.add(new String[] { "市", "区", "队伍总量"});
                    fieldNames.add(new String[] { "groupCity", "groupArea", "groupTotal"});
                    groupStatisData = groupService.getGroupRegionStatisData(param);
                    map.put("按类型", groupStatisData);
                    break;
                default:
                    return;
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
    public Object searchGroupSumStatisData() {
        GroupStatisData groupSumStatisData = groupService.getGroupSumStatisData();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("groupSumStatisData", groupSumStatisData);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searchGroupSumForCharts(Integer type) {

        if (checkParaNULL(type)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            GroupStatisData groupSumForCharts = groupService.getGroupSumForCharts(param);
            resultMap.put("data", groupSumForCharts);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);    }

    @Override
    public Object searchGroupMemberByGroupId(@ModelAttribute Group group) {
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", group.getId());

        List<User> list = new ArrayList<>();

        switch (group.getStatus()){
            case 0:
                break;
            case 1:
                List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(params);
                for (GroupMember gm: groupMemberList) {
                    list.add(gm.getUser());
                }
                resultMap.put("list",groupMemberList);
                break;
            case 2:
                break;
            case 3:
                List<GroupMemberHistory> groupMemberHistoryList = groupMemberHistoryService.getGroupMemberHistoryList(params);
                for (GroupMemberHistory gm : groupMemberHistoryList){
                    list.add(gm.getUser());
                }
                resultMap.put("list", groupMemberHistoryList);
                break;
            case 4:
                break;
            default:
                break;
        }

        //获取用户头像
        ossFileUtils.getUserHeadList(list,null);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searcnGroupSumForBar(Integer type, Date beginTime, Date endTime) {

        if (checkParaNULL(type, beginTime, endTime)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            param.put("beginTime", beginTime);
            param.put("endTime", endTime);
            List<GroupStatisData> groupSumForBar = groupService.getGroupSumForBar(param);

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

            for(GroupStatisData g :groupSumForBar){
                if(bars.containsKey(g.getDate())){
                    bars.put(g.getDate(), g.getGroupTotal());
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
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public void exportGroupByFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("groupFile") CommonsMultipartFile file) {
        response.setContentType("application/vnd.ms-excel");
        if(null!=file){
            List<List<String>> groupDatas = getDataFromFile(file);
            Map<String, Object> param = new HashMap<>();
            try
            {
                OutputStream baos = response.getOutputStream();
                List<String[]> headNames = new ArrayList<String[]>();
                List<String[]> fieldNames = new ArrayList<String[]>();
                headNames.add(new String[] { "Id", "队名", "状态","性别","省", "市", "地区","类型","创建时间","修改日期","推荐队伍","组成约会数  "});
                fieldNames.add(new String[] { "id", "name", "groupStatus", "groupType", "province", "city","area", "barName","createTime","modifyTime","recommendCount","activityCount"});



                List<GroupDetailWithCount> allGroupDateWithCounts = new ArrayList<GroupDetailWithCount>();
                for (List<String> groupData: groupDatas) {
                    param.put("groupData",groupData);
                    List<GroupDetailWithCount> groupDateWithCounts = groupService.getGroupDetailCountByIds(param);
                    allGroupDateWithCounts.addAll(groupDateWithCounts);
                }

                LinkedHashMap<String, List> map = new LinkedHashMap<>();
                map.put("队伍统计", allGroupDateWithCounts);

                ExcelUtil.ExportSetInfo setInfo = new ExcelUtil.ExportSetInfo();
                setInfo.setObjsMap(map);
                setInfo.setFieldNames(fieldNames);
                setInfo.setHeadNames(headNames);
                setInfo.setOut(baos);

                // 将需要导出的数据输出到baos
                ExcelUtil.export2Excel(setInfo);
            }
            catch (UnsupportedEncodingException e1)
            {
                e1.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            logger.debug("文件生成...");
        }
    }

    @Override
    public void exportGroupWithUserByFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("groupFile") CommonsMultipartFile file) {
        response.setContentType("application/vnd.ms-excel");
        if(null!=file) {
            List<List<String>> groupDatas = getDataFromFile(file);
            Map<String, Object> param = new HashMap<>();

            //创建一个Excel，并设置标题
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("队伍用户导出");
            Row titleRow = sheet.createRow(0);
            String[] titleStrings = new String[] {"队伍Id" , "用户Id" , "昵称" ,"性别"};//设置标题
            for (int i = 0;i<titleStrings.length;i++) {
                Cell titleRowCell = titleRow.createCell(i);
                titleRowCell.setCellValue(titleStrings[i]);
            }

            //批量获取队伍信息
            List<Group> allGroups = new ArrayList<Group>();
            for (List<String> groupData: groupDatas) {
                param.put("ids",groupData);
                List<Group> groups = groupService.getGroupList(param);
                allGroups.addAll(groups);
            }

            //遍历队伍，并获取用户信息
            int groupRowCount = 1;
            for (Group group : allGroups) {//插入数据
                param.clear();
                param.put("groupId", group.getId());

                List<User> userList = new ArrayList<>();
                switch (group.getStatus()) {
                    case 0:
                        break;
                    case 1:
                        List<GroupMember> groupMemberList = groupMemberService.getGroupMemberList(param);
                        for (GroupMember gm : groupMemberList) {
                            userList.add(gm.getUser());
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        List<GroupMemberHistory> groupMemberHistoryList = groupMemberHistoryService.getGroupMemberHistoryList(param);
                        for (GroupMemberHistory gm : groupMemberHistoryList) {
                            userList.add(gm.getUser());
                        }
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }

                //插入用户数据到Excel中
                for (int userDataIndex = 0;userDataIndex<userList.size();userDataIndex++) {
                    Row userDataRow = sheet.createRow(groupRowCount+userDataIndex);
                    Cell userIdentifyCell = userDataRow.createCell(1);
                    userIdentifyCell.setCellValue(userList.get(userDataIndex).getIdentify());
                    Cell userNickNameCell = userDataRow.createCell(2);
                    userNickNameCell.setCellValue(userList.get(userDataIndex).getNickName());
                    Cell userSexCell = userDataRow.createCell(3);
                    userSexCell.setCellValue(userList.get(userDataIndex).getSex().equals("M")?"男":"女");
                }

                //在用户前合并单元格，并插入队伍id
                CellRangeAddress cellRangeAddress = new CellRangeAddress(groupRowCount,groupRowCount+userList.size()-1,0,0);//合并单元格
                sheet.addMergedRegion(cellRangeAddress);
                Row groudIdRow = sheet.getRow(groupRowCount);
                Cell groudIdRowCell = groudIdRow.createCell(0);
                groudIdRowCell.setCellValue(group.getId());

                groupRowCount = groupRowCount +userList.size();
            }

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

    @Override
    public Object searchGroupMemberHistoryByGroupIds(Long[] ids) {
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        if (checkParaNULL(ids)){
            params.put("groupIds", ids);

            List<User> list = new ArrayList<>();
            List<GroupMemberHistory> groupMemberHistoryList = groupMemberHistoryService.getGroupMemberHistoryList(params);
            for (GroupMemberHistory gm : groupMemberHistoryList){
                list.add(gm.getUser());
            }
            resultMap.put("list", groupMemberHistoryList);
            //获取用户头像
            ossFileUtils.getUserHeadList(list,null);

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    /**
     * 每读一百行，拼接成字符串，插入数组
     * @param file
     * @return
     */
    private List<List<String>> getDataFromFile(CommonsMultipartFile file){
        String str = "";
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        List<List<String>> dataList = new ArrayList<>();

        try {
            br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            List<String> dataHaving100 = new ArrayList<String>();//改数组用来存放一百个数据
            int countRow = 0;
            while ((str = br.readLine()) != null) {
                dataHaving100.add(str);
                countRow++;
                if (countRow%200==0){//每两百条数据添加一次
                    dataList.add(dataHaving100);
                    dataHaving100 = new ArrayList<String>();
                }

            }
            if (dataHaving100.size()>0)
                dataList.add(dataHaving100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }


}
