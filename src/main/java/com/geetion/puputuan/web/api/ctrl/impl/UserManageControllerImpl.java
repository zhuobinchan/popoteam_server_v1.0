package com.geetion.puputuan.web.api.ctrl.impl;


import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.Role;
import com.geetion.generic.permission.service.RolePermissionRelativeService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.permission.service.UserRoleRelativeService;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.constant.JPushType;
import com.geetion.puputuan.common.constant.MessageType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.*;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.UserManageController;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class UserManageControllerImpl extends BaseController implements UserManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;
    @Resource
    private InterestService interestService;
    @Resource
    private JobService jobService;
    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private PhotoService photoService;
    @Resource
    private ComplainService complainService;
    @Resource
    private MessageService messageService;
    @Resource
    private CommonService commonService;
    @Resource
    private ActivityService activityService;
    @Resource
    private UserRoleRelativeService userRoleRelativeService;
    @Resource
    private RolePermissionRelativeService rolePermissionRelativeService;
    @Resource
    private UserBlackListService userBlackListService;

    @Override
    public Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                         @ModelAttribute User object, Date registerTimeBegin, Date registerTimeEnd, String age) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("registerTimeBegin", registerTimeBegin);
            param.put("registerTimeEnd", registerTimeEnd);
            param.put("age", age);

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<UserWithCount> list = userService.getParamWithCount(param);
                    //获得头像
                    ossFileUtils.getUserWithCountHeadList(list, null);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        UserDetailWithCount obj = userService.getWithDetailCountByUserId(id);
                        if (obj != null) {
                            //获得头像
                            obj.setHead(ossFileUtils.getPictures(obj.getHeadId(), null));
                        }

                        int dataFullNum = 0;
                        // 微信号
                        if (null != obj.getWechatId()){
                            dataFullNum ++;
                        }
                        // 头像
                        if (null != obj.getHead()){
                            dataFullNum ++;
                        }
                        // 职业
                        if (null != obj.getJobList() && 0 < obj.getJobList().size()){
                            dataFullNum ++;
                        }
                        // 兴趣
                        if (null != obj.getInterestList() && 0 < obj.getInterestList().size()){
                            dataFullNum ++;
                        }
                        // 个人签名
                        if (null != obj.getSign() && "".equals(obj.getSign())){
                            dataFullNum ++;
                        }
                        // 地址
                        if (null != obj.getProvinceId() && null != obj.getCityId()){
                            dataFullNum ++;
                        }

                        NumberFormat formatter = new DecimalFormat("0.00");
                        Double ratio = new Double(dataFullNum / 6D * 100);
                        // 设置用户资料完善百分比
                        obj.setDataFullRatio(formatter.format(ratio) + "%");
                        // 设置用户是否在队伍中
                        obj.setIsInGroup(commonService.userHaveInRunningGroup(obj.getUserId()).size() > 0 ? "是" : "否");

                        param.clear();
                        param.put("userId", obj.getUserId());
                        List<Integer> types = new ArrayList<>();
                        types.add(ActivityTypeAndStatus.BEGIN);
                        types.add(ActivityTypeAndStatus.SUCCESS);
                        param.put("types", types);
                        param.put("status", GroupTypeAndStatus.GROUP_TEAM);
                        //设置用户是否在约会中
                        obj.setIsInActivity(activityService.getActivityByUser(param) != null ? "是" : "否");

                        if (null == obj.getProvince()){
                            obj.setProvince("");
                        }
                        if (null == obj.getCity()){
                            obj.setCity("");
                        }

                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<UserWithCount> pagingForKeyword = userService.getParamWithCountPage(pageEntity);
                    List<UserWithCount> resultList = pagingForKeyword.getResultList();

//                    for(UserWithCount u : resultList){
//                        u.setAge(getAge(u.getBirthday()));
//                    }
                    //获得头像
                    ossFileUtils.getUserWithCountHeadPage(pagingForKeyword, null);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User object, Date registerTimeBegin, Date registerTimeEnd, String age) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        param.put("registerTimeBegin", registerTimeBegin);
        param.put("registerTimeEnd", registerTimeEnd);
        param.put("age", age);

        param.putAll(pojoToMap(object));



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
            headNames.add(new String[] { "昵称", "性别", "年龄", "Id","手机号码", "省","市","注册日期","邀请数","7天内邀请次数","最近一次发起邀请时间","被邀请数","7天内被邀请次数","最近一次被邀请时间","好友数","7天内好友新增量","最近一次新增好友时间"});
            fieldNames.add(new String[] { "nickName", "sex", "age", "identify","phone", "province","city","createTime","invitedTimes","invitedTimesInWeek","invitedLatelyDate","beInvitedTimes","beInvitedTimesInWeek","beInvitedLatelyDate","friendsNumber","friendsNumberInWeek","friendsNewestDate"});
            List<UserWithCount> paramWithCount = userService.getParamWithCount(param);
            map.put("用户信息", paramWithCount);


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
        finally
        {
            session.setAttribute("state", "open");
        }
        logger.debug("文件生成...");
    }

    @Override
    public Object searchFriend(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                               @ModelAttribute FriendRelationship object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<UserWithCount> list = friendRelationshipService.getFriendDetailList(param);
                    //获得头像
                    ossFileUtils.getUserWithCountHeadList(list, null);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        UserDetailWithCount obj = userService.getWithDetailCountByUserId(id);
                        if (obj != null) {
                            //获得头像
                            obj.setHead(ossFileUtils.getPictures(obj.getHeadId(), null));
                        }
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<UserWithCount> pagingForKeyword = friendRelationshipService.getFriendDetailPage(pageEntity);
                    List<UserWithCount> resultList = pagingForKeyword.getResultList();

                    for(UserWithCount u : resultList){
                        u.setAge(getAge(u.getBirthday()));
                    }
                    //获得头像
                    ossFileUtils.getUserWithCountHeadPage(pagingForKeyword, null);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    private int getAge(Date birthday){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("YYYY-MM-DD");
        String currentTime = formatDate.format(calendar.getTime());
        Date today = null;
        try {
            today = formatDate.parse(currentTime);
            return today.getYear() - birthday.getYear();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public Object searchPhoto(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                              @ModelAttribute Photo object, String style) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<PhotoWithLikeCount> list = photoService.getPhotoWithLikeCountList(param);
                    //获得头像
                    ossFileUtils.getPhotoWithLikeCountImageList(list, style);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        param.clear();
                        param.put("id", id);
                        List<PhotoWithLikeCount> list1 = photoService.getPhotoWithLikeCountList(param);
                        if (list1 != null && list1.size() != 0) {
                            PhotoWithLikeCount obj = list1.get(0);
                            //获得头像
                            obj.setImage(ossFileUtils.getPictures(obj.getImageId(), style));
                            resultMap.put("object", obj);
                        } else {
                            return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                        }
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<PhotoWithLikeCount> pagingForKeyword = photoService.getPhotoWithLikeCountPage(pageEntity);
                    //获得头像
                    ossFileUtils.getPhotoWithLikeCountImagePage(pagingForKeyword, style);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object freezeUserOrNot(Long[] userIds, Boolean freeze) {
        if (checkParaNULL(userIds, freeze) && userIds.length > 0) {

            //解冻或冻结用户
            boolean result = userBaseService.freezeUserBaseBatch(Arrays.asList(userIds), freeze);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object searchAllUserCount() {
        Map<String, Object> resultMap = new HashMap<>();
        CountUserRelated countUserRelated = userService.getAllUserRelatedCount();
        resultMap.put("object", countUserRelated);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

    }

    @Override
    public Object searchComplain(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                                 @ModelAttribute User object, Date releaseTimeBegin,
                                 Date releaseTimeEnd, Integer complainTimesBegin,
                                 Integer complainTimesEnd, String age, String style) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            if (!checkParaNULL(object.getSex())) {
                object.setSex(null);
            }
            if (!checkParaNULL(object.getIdentify())) {
                object.setIdentify(null);
            }
            if (!checkParaNULL(object.getNickName())) {
                object.setNickName(null);
            }

            param.put("releaseTimeBegin", releaseTimeBegin);
            param.put("releaseTimeEnd", releaseTimeEnd);
            param.put("age", age);
            param.put("complainTimesBegin", complainTimesBegin);
            param.put("complainTimesEnd", complainTimesEnd);

            param.putAll(pojoToMap(object));

            //查询未删除的相册
//            param.put("isDelete", PhotoType.NOT_DELETE);

            switch (methodType) {
                case 1:
                    List<ComplainWithPhotoAndUser> list = complainService.getPhotoUserList(param);
                    //获得头像
                    if (list != null && list.size() != 0) {
                        for (ComplainWithPhotoAndUser complainWithPhotoAndUser : list) {
                            if (complainWithPhotoAndUser.getPhotoUser() != null &&
                                    complainWithPhotoAndUser.getPhotoUser().getHeadId() != null) {
                                complainWithPhotoAndUser.getPhotoUser().setHead(
                                        ossFileUtils.getPictures(complainWithPhotoAndUser.getPhotoUser().getHeadId(), style));
                            }
                        }
                    }
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {

                        param.put("id", id);
                        //查询相册
                        List<PhotoWithLikeCount> photoWithLikeCountList = photoService.getPhotoWithLikeCountList(param);
                        resultMap.put("photo", photoWithLikeCountList != null && photoWithLikeCountList.size() != 0 ?
                                photoWithLikeCountList.get(0) : null);

                        //查询投诉该相册的用户
                        param.clear();
                        param.put("photoId", id);
                        List<Complain> complainList = complainService.getComplainUserList(param);
                        //获得头像
                        if (complainList != null && complainList.size() != 0) {
                            for (Complain complain : complainList) {
                                if (complain.getUser() != null && complain.getUser().getHeadId() != null) {
                                    complain.getUser().setHead(ossFileUtils.getPictures(complain.getUser().getHeadId(), style));
                                }
                            }
                        }
                        resultMap.put("complainList", complainList);

                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<ComplainWithPhotoAndUser> pagingForKeyword = complainService.getPhotoUserPage(pageEntity);
                    //获得头像
                    if (pagingForKeyword != null && pagingForKeyword.getResultList() != null
                            && pagingForKeyword.getResultList().size() != 0) {
                        for (ComplainWithPhotoAndUser complainWithPhotoAndUser : pagingForKeyword.getResultList()) {
                            if (complainWithPhotoAndUser.getPhotoUser() != null
                                    && complainWithPhotoAndUser.getPhotoUser().getHeadId() != null) {
                                complainWithPhotoAndUser.getPhotoUser().setHead(
                                        ossFileUtils.getPictures(complainWithPhotoAndUser.getPhotoUser().getHeadId(), style));
                            }
                        }
                    }
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object deletePhoto(Long photoId) {
        if (checkParaNULL(photoId)) {
            //这里做逻辑删除，不做物理删除
            Photo photo = new Photo();
            photo.setId(photoId);
//            photo.setIsDelete(true);

            //获取File对象
            ossFileUtils.getPhoto(photo, null);
            //删除文件,photo表的数据通过外键约束,在删除file表数据时自动删除
            ossFileUtils.deleteFile(shiroService.getLoginUserBase().getId(), photo.getImage());
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object warnUser(Long photoId, Long userId) {
        if (checkParaNULL(photoId, userId)) {
            //查询被投诉的相片
            Photo photo = photoService.getPhotoById(photoId);
            //查询被投诉的用户
            User user = userService.getByUserId(userId);

            //格式化时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

            String content = "您在 " + simpleDateFormat.format(photo.getCreateTime()) +
                    " 发的照片，涉嫌违规，请及时处理，否则将会对您的账号进行封号处理";
            Message message = new Message();
            message.setUserId(userId);
            message.setContent(content);
            message.setStatus(MessageType.CAN_READ);
            message.setIsRead(false);
            message.setAnnouncementId(null);
            MessageParam messageParam = new MessageParam();
            message.setParam(messageParam.toJSONString());

            if (messageService.addMessage(message)) {
                //TODO 添加JPush推送
                JPushUtils.sendGroupJPush("警告信息", content, JPushType.WARN, null, null, user.getUserBase().getAccount());
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchUserStatisData(Integer methodType, Integer type, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.put("type", type);

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<UserStatisData> pagingForKeyword = userService.getUserStaticDataByParamPage(pageEntity);
                    ResultUtils.setResultMap(resultMap, pagingForKeyword);

                    break;
                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    public void exportExcel(HttpServletRequest request, HttpServletResponse response, Integer type)
    {
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
            headNames.add(new String[] { "日期", "用户量", "微信绑定量", "男性用户量", "男性占比","女性用户量","女性占比","18岁以下","18-29岁","30-45岁","45岁以上" });
            List<String[]> fieldNames = new ArrayList<String[]>();
            fieldNames.add(new String[] { "date", "userTotal", "wechatTotal", "maleTotal", "maleRatio","femaleTotal","femaleRatio","ageOneTotal","ageTwoTotal","ageThreeTotal","ageFourTotal"});

            List<UserStatisData> userStatisData = userService.getUserStaticDataByParam(param);
            LinkedHashMap<String, List> map = new LinkedHashMap<>();
            map.put("基本统计", userStatisData);

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
    public Object searchUserInterestStatisData(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<StatisData> pagingForKeyword = userService.getUserInterestStaticDataByParamPage(pageEntity);
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
    public Object searchUserJobStatisData(Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            switch (methodType) {
                case 1:

                    break;
                case 2:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<StatisData> pagingForKeyword = userService.getUserJobStaticDataByParamPage(pageEntity);
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
    public void exportMonthData(HttpServletRequest request, HttpServletResponse response, Integer exportType) {

        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = null;
        try
        {

            OutputStream baos = response.getOutputStream();
            List<String[]> fieldNames = new ArrayList<String[]>();
            fieldNames.add(new String[] { "name", "total", "ratio", "maleTotal", "femaleTotal"});

            List<String[]> headNames = new ArrayList<String[]>();
            LinkedHashMap<String, List> map = new LinkedHashMap<>();
            List<StatisData> statisData = new ArrayList<>();
            if (1 == exportType){
                headNames.add(new String[] { "兴趣", "总量", "占用户总量比例", "男性总量", "女性总量"});
                statisData = userService.getUserInterestStaticDataByParam(null);
                map.put("兴趣统计", statisData);
            } else {
                headNames.add(new String[] { "职业", "总量", "占用户总量比例", "男性总量", "女性总量"});
                statisData = userService.getUserJobStaticDataByParam(null);
                map.put("职业统计", statisData);
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
    public Object searcnUserSumStatisData() {
        UserStatisData userSumStatisData = userService.getUserSumStatisData();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userSumStatisData", userSumStatisData);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searcnUserPermission() {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> data = new ArrayList<>();
        Long userId = shiroService.getLoginUserBase().getId();
        List<Role> roleList = userRoleRelativeService.getRoleByUserId(userId);
        List<Permission> permissionList = new ArrayList<>();

        Map<String, Permission> permissionMap = new HashMap<>();

        roleList.forEach((role) -> {
            List<Permission> allPermissionBelongRole = rolePermissionRelativeService.getAllPermissionBelongRole(role);
            allPermissionBelongRole.forEach((permission) -> {
                permissionMap.put(permission.getPermission(), permission);
            });
        });

        permissionMap.forEach((key, value) -> {
            data.add(value.getPermission());
        });

        resultMap.put("data", data);

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object searcnUserSumForCharts(Integer type) {

        if (checkParaNULL(type)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            UserStatisData userSumForCharts = userService.getUserSumForCharts(param);
            resultMap.put("data", userSumForCharts);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object searcnUserSumForBar(Integer type, Date beginTime, Date endTime) {
        if (checkParaNULL(type, beginTime, endTime)) {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            param.put("beginTime", beginTime);
            param.put("endTime", endTime);
            List<UserStatisData> userSumForCharts = userService.getUserSumForBar(param);

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

            for(UserStatisData u :userSumForCharts){
                if(bars.containsKey(u.getDate())){
                    bars.put(u.getDate(), u.getUserTotal());
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
    public void exportByFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("userFile") CommonsMultipartFile file, Integer userType) {
        response.setContentType("application/vnd.ms-excel");
        if(null!=file){
            List<List<String>> userDatas = getDataFromFile(file);
            Map<String, Object> param = new HashMap<>();
            param.put("userType",userType);
            try
            {

                OutputStream baos = response.getOutputStream();
                List<String[]> headNames = new ArrayList<String[]>();
                List<String[]> fieldNames = new ArrayList<String[]>();
                headNames.add(new String[] { "昵称", "性别", "年龄", "Id","手机号码", "省","市","注册日期","邀请数","7天内邀请次数","最近一次发起邀请时间","被邀请数","7天内被邀请次数","最近一次被邀请时间","好友数","7天内好友新增量","最近一次新增好友时间"});
                fieldNames.add(new String[] { "nickName", "sex", "age", "identify","phone", "province","city","createTime","invitedTimes","invitedTimesInWeek","invitedLatelyDate","beInvitedTimes","beInvitedTimesInWeek","beInvitedLatelyDate","friendsNumber","friendsNumberInWeek","friendsNewestDate"});


                List<UserWithCount> allUserDateWithCounts = new ArrayList<UserWithCount>();
                for (List<String> userData: userDatas) {
                    param.put("userData",userData);
                    List<UserWithCount> userDateWithCounts = userService.getUserListWithCountByIdsOrPhones(param);
                    allUserDateWithCounts.addAll(userDateWithCounts);
                }
                

                LinkedHashMap<String, List> map = new LinkedHashMap<>();
                map.put("基本统计", allUserDateWithCounts);

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
    public Object searchUserBlackList(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<UserBlackList> list =userBlackListService.getUserBlackListList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        UserBlackList obj = userBlackListService.getUserBlackListById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null){
                        String identify = (String) param.get("identify");
                        if (checkParaNULL(identify)){
                            String[] identifies = identify.split(",");
                            param.put("identifies",identifies);
                            param.remove("identify");
                        }
                        pageEntity.setParam(param);
                    }
                    PagingResult<UserBlackList> pagingForKeyword = userBlackListService.getUserBlackListPage(pageEntity);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object setUserBlackListStatus(Long[] ids, Integer status) {
        if (checkParaNULL(ids,status)) {
            for (Long id: ids) {
                UserBlackList userBlackList = userBlackListService.getUserBlackListById(id);
                userBlackList.setStatus(status);
                userBlackListService.updateUserBlackList(userBlackList);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object addUserBlackLists(String[] identifiesOrPhones, Integer type) {
        if (checkParaNULL(identifiesOrPhones,type)) {
            Map<String,Object> map = new HashedMap();
            if(type==1)map.put("identifies",identifiesOrPhones);
            if(type==2)map.put("phones",identifiesOrPhones);
            if (userBlackListService.removeBlackLists(map))
                userBlackListService.addUserBlackLists(map);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
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
