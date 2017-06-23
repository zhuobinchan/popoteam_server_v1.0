package com.geetion.puputuan.web.api.ctrl.impl;


import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.serverfile.model.File;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.ConfigInfo;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.ManageController;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class ManageControllerImpl extends BaseController implements ManageController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private UserService userService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;
    @Resource
    private AnnouncementService announcementService;
    @Resource
    private AnnouncementDistrictService announcementDistrictService;
    @Resource
    private MessageService messageService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private ConsultationService consultationService;
    @Resource
    private InterestService interestService;
    @Resource
    private JobService jobService;
    @Resource
    private  FancyService fancyService;


    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource
    private OssFileUtils ossFileUtils;

    @Resource(name = "configInfo")
    private ConfigInfo configInfo;



    //============================================== 公告 ==============================================

    @Override
    public Object searchAnnouncement(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                                     @ModelAttribute Announcement object, Date sendTimeBegin, Date sendTimeEnd) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            if (checkParaNULL(sendTimeBegin)) {
                param.put("sendTimeBegin", sendTimeBegin);
            }
            if (checkParaNULL(sendTimeEnd)) {
                param.put("sendTimeEnd", sendTimeEnd);
            }
            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Announcement> list = announcementService.getAnnouncementList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Announcement obj = announcementService.getAnnouncementById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Announcement> pagingForKeyword = announcementService.getAnnouncementPage(pageEntity);
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
    public Object releaseAnnouncement(@ModelAttribute Announcement announcement,@RequestParam(value = "districts[]", required = false) Integer[] districts,@RequestParam(value = "types[]", required = false) Integer[] types, @RequestParam(value = "file", required = false) CommonsMultipartFile imageFile,String editorArea) {

        if (checkParaNULL(announcement, announcement.getTitle(), announcement.getContent())) {
            boolean isAll = false;
            if ((districts == null && types == null) || (districts != null && districts.length == 0 &&
                    types != null && types.length == 0)) {
                //不选择地区则默认全部所有省
                List<District> districtList = districtService.getProvinceByParam(null);
                districts = new Integer[districtList.size()];
                types = new Integer[districtList.size()];
                //将所有省添加进要推送的区域里
                for (int i = 0; i < districtList.size(); i++) {
                    districts[i] = districtList.get(i).getCode();
                    types[i] = AnnouncementType.DISTRICT_PROVINCE;
                }
                // 表示全国发布
                isAll = true;
            } else if (districts != null && types != null && districts.length != types.length) {
                //如果选择了地区，但是types 不对应，则提示参数错误
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
            if (null != imageFile){
                File file = ossFileUtils.uploadAnnounceImage(imageFile, 0L);
                if(null != file){
                    announcement.setImage(file.getUrl());
                }else {
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }
            }

            //将公告的状态标记为开放
            announcement.setType(AnnouncementType.OPEN);
            //设置发布者
            announcement.setUserId(shiroService.getLoginUserBase().getId());


            //如果url不为空，则以url为准，不生成html文件
            if(!checkParaNULL(announcement.getUrl())){
                //生成从富文本编辑框生成html文件
                String rootPath = "/src/view/announceView/";
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String fileName = df.format(new Date())+"_"+Calendar.getInstance().getTimeInMillis()+".html";
                String domain =configInfo.getServerDomain();//从配置文件获取相关配置
                createFile(rootPath,fileName,editorArea.replaceAll("http://","https://"),announcement.getTitle());
                String url = domain + rootPath + fileName;

                announcement.setUrl(url);
            }
            //添加公告
            if (announcementService.addAnnouncement(announcement)) {

                //推送的公告地区
                List<AnnouncementDistrict> districtList = new ArrayList<>();
                //为每一个要推送的地区的用户新增对应的消息
                List<Notice> noticeList = new ArrayList<>();
                //要推动的地区的用户列表
                List<User> userList = new ArrayList<>();
                //要推送的地区名称
                List<String> districtNameList = new ArrayList<>();
                for (int i = 0; i < districts.length; i++) {
                    //添加推送地区及查询该地区的用户
                    addAnnouncementType(districtList, districtNameList, userList, announcement, districts[i], types[i]);

                }

                if(isAll){
                    userList.clear();
                    List<User> userList1 = userService.getUserList(null);
                    List<User> userNullList = new ArrayList<>();
//                    for(User user : userList1){
//                        if(null == user.getProvince() && null == user.getCity() && null == user.getArea()){
//                            userList.add(user);
//                        }
//                    }
                    userList.addAll(userList1);
                }
                //为每一个要推送的用户设置消息
                addAnnouncementMessage(noticeList, userList, announcement);

                //批量插入推送的地区
                announcementDistrictService.addAnnouncementDistrictBatch(districtList);
                //批量插入消息列表的公告显示
                if (noticeList.size() != 0) {
                    noticeService.addNoticeBatch(noticeList);
                }

                //TODO 添加JPush推送
                JPushUtils.sendJPushByTags("系统公告", announcement.getTitle(), JPushType.ANNOUNCEMENT,
                        districtNameList.toArray(new String[districtNameList.size()]));

                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object uploadUserToRelease(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes, @ModelAttribute Announcement announcement, String userType, @RequestParam(value = "imageFile" , required = false) CommonsMultipartFile imageFile,String editorArea) {
        if(!files[0].getOriginalFilename().endsWith("txt")) {
            return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
        }
        String str = "";
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        List<String> userList = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(files[0].getInputStream()));
            while ((str = br.readLine()) != null) {
                userList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != imageFile){
            File file = ossFileUtils.uploadAnnounceImage(imageFile, 0L);
            if(null != file){
                announcement.setImage(file.getUrl());
            }else {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }

        //将公告的状态标记为开放
        announcement.setType(AnnouncementType.OPEN);
        //设置发布者
        announcement.setUserId(shiroService.getLoginUserBase().getId());

        List<User> toReleaseUser = new ArrayList<>();
        List<Notice> noticeList = new ArrayList<>();
        List<String> accountList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();

        //如果url不为空，则以url为准，不生成html文件
        if(!checkParaNULL(announcement.getUrl())) {
            //生成从富文本编辑框生成html文件
            String rootPath = "/src/view/announceView/";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String fileName = df.format(new Date()) + "_" + Calendar.getInstance().getTimeInMillis() + ".html";
            String domain = configInfo.getServerDomain();//从配置文件获取相关配置
            createFile(rootPath, fileName, editorArea.replaceAll("http://","https://"), announcement.getTitle());
            String url = domain + rootPath + fileName;

            announcement.setUrl(url);
        }

        if (announcementService.addAnnouncement(announcement)){
            Map<String, Object> params = new HashMap<>();
            if ("1".equals(userType)){
                for(String s : userList){
                    params.clear();
                    params.put("identify", s);
                    User user = userService.getUser(params);
                    if(null == user){
                        sb.append(s).append(",");
                        continue;
                    }
                    toReleaseUser.add(user);
                    accountList.add(user.getUserBase().getAccount());
                }
            }else {
                for(String s : userList){
                    params.clear();
                    params.put("phone", "+86" + s);
                    User user = userService.getUser(params);
                    if(null == user){
                        sb.append(s).append(",");
                        continue;
                    }
                    toReleaseUser.add(user);
                    accountList.add(user.getUserBase().getAccount());
                }
            }
            addAnnouncementMessage(noticeList, toReleaseUser, announcement);
            int perNum = 5;
            int remainder = 0;
            if (noticeList.size() != 0) {
                for(int i = 0; i < noticeList.size() / perNum ; i++){
                    noticeService.addNoticeBatch(noticeList.subList(i * perNum, (i + 1) * perNum ));
                }
                remainder = noticeList.size() % perNum;
                if (remainder > 0){
                    noticeService.addNoticeBatch(noticeList.subList(noticeList.size() - remainder, noticeList.size()));
                }

            }
            Map<String, Object> resultMap = new HashMap<>();
            //TODO 添加JPush推送
            JPushUtils.sendJPushByAccount("系统公告", announcement.getTitle(), JPushType.ANNOUNCEMENT,
                    accountList.toArray(new String[accountList.size()]));

            resultMap.put("fail", sb.toString());
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object openAnnouncementOrNot(Long[] announcementIds, Boolean isOpen) {
        if (checkParaNULL(announcementIds, isOpen) && announcementIds.length > 0) {
            for (Long announcementId : announcementIds) {

                //更新公告状态
                Announcement announcement = announcementService.getAnnouncementById(announcementId);
                announcement.setType(isOpen ? AnnouncementType.OPEN : AnnouncementType.CLOSE);
                announcementService.updateAnnouncement(announcement);

                //更新每个用户的消息
                Notice notice = new Notice();
                notice.setAnnouncementId(announcementId);
                notice.setStatus(isOpen ? MessageType.CAN_READ : MessageType.CANNOT_READ);
                noticeService.updateNoticeByParam(notice);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public void exportUserById(Long announcementId, HttpServletRequest request, HttpServletResponse response) {
        Map param = new HashMap();
        param.put("announcementId",announcementId);
        List<Notice> notices = noticeService.getNoticeList(param);
        List<User> users = new ArrayList<>();
        for (Notice notice:notices) {
            users.add(userService.getByUserId(notice.getUserId()));
        }
        List<String[]> headNames = new ArrayList<String[]>();
        List<String[]> fieldNames = new ArrayList<String[]>();
        headNames.add(new String[] { "昵称", "性别", "Id","手机号码"});
        fieldNames.add(new String[] { "nickName", "sex", "identify","phone"});

        LinkedHashMap<String, List> map = new LinkedHashMap<>();
        map.put("公告所发的用户信息",users);
        try {
            OutputStream baos = response.getOutputStream();
            ExcelUtil.ExportSetInfo setInfo = new ExcelUtil.ExportSetInfo();
            setInfo.setObjsMap(map);
            setInfo.setFieldNames(fieldNames);
            setInfo.setHeadNames(headNames);
            setInfo.setOut(baos);
            // 将需要导出的数据输出到baos
            ExcelUtil.export2Excel(setInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        logger.debug("文件生成...");
    }

    @Override
    public void uploadEditorAreaImage(@RequestParam(value = "image",required = false) CommonsMultipartFile imageFile,HttpServletRequest request, HttpServletResponse response) {
        try {
            if (null != imageFile){
                File file = ossFileUtils.uploadAnnounceImage(imageFile, 0L);
                response.getWriter().write(file.getUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//============================================== 咨询 ==============================================

    @Override
    public Object searchConsultation(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                                     @ModelAttribute Consultation object, Date sendTimeBegin, Date sendTimeEnd,String style) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            if (checkParaNULL(sendTimeBegin)) {
                param.put("sendTimeBegin", sendTimeBegin);
            }
            if (checkParaNULL(sendTimeEnd)) {
                param.put("sendTimeEnd", sendTimeEnd);
            }
            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Consultation> list = consultationService.getConsultationList(param);
                    //设置图片的链接
                    for (Consultation consultation : list) {
                        if (consultation.getImageId() != null) {
                            consultation.setImage(ossFileUtils.getPictures(consultation.getImageId(), style));
                        }
                    }
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Consultation obj = consultationService.getConsultationById(id);
                        //设置图片的链接
                        if (obj != null && obj.getImageId() != null) {
                            obj.setImage(ossFileUtils.getPictures(obj.getImageId(), style));
                        }
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Consultation> pagingForKeyword = consultationService.getConsultationPage(pageEntity);
                    //设置图片的链接
                    if (checkParaNULL(pagingForKeyword, pagingForKeyword.getResultList())) {
                        for (Consultation consultation : pagingForKeyword.getResultList()) {
//                            System.out.println("\n " + consultation.toString());
                            if (consultation.getImageId() != null) {
                                consultation.setImage(ossFileUtils.getPictures(consultation.getImageId(), style));
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
    public Object releaseConsultation(@ModelAttribute Consultation consultation) {
        if (checkParaNULL(consultation, consultation.getTitle(), consultation.getUrl(), consultation.getImageId())) {
            //将资讯的状态标记为开放
            consultation.setType(ConsultationType.OPEN);
            //添加资讯
            boolean result = consultationService.addConsultation(consultation);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updateConsultation(@ModelAttribute Consultation consultation) {
        if (checkParaNULL(consultation, consultation.getId())) {
            //将资讯的状态标记为开放
            consultation.setType(ConsultationType.OPEN);
            //编辑资讯
            boolean result = consultationService.updateConsultation(consultation);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object openConsultationOrNot(Long[] consultationIds, Boolean isOpen) {
        if (checkParaNULL(consultationIds, isOpen) && consultationIds.length > 0) {
            try {
                //更新每一条状态
                for (Long consultationId : consultationIds) {
                    Consultation consultation = consultationService.getConsultationById(consultationId);
                    consultation.setType(isOpen ? ConsultationType.OPEN : ConsultationType.COLSE);
                    consultationService.updateConsultation(consultation);
                }
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            } catch (Exception e) {
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    //============================================== 兴趣标签 ==============================================


    @Override
    public Object searchInterest(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute Interest object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Interest> list = interestService.getInterestList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Interest obj = interestService.getInterestById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Interest> pagingForKeyword = interestService.getInterestPage(pageEntity);
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
    public Object addInterestLabel(String name, Integer status) {
        if (checkParaNULL(name, status)) {

            Map<String, Object> param = new HashMap<>();
            param.put("name", name);
            param.put("status", status);
            Interest interestFromDB = interestService.getInterest(param);

            boolean result;
            if (interestFromDB != null) {
                //如果该兴趣在数据库已存在，则更新该兴趣
                interestFromDB.setType(InterestTypeAndStatus.SYSTEM_TYPE);
                interestFromDB.setStatus(status);
                result = interestService.updateInterest(interestFromDB);
            } else {
                //如果该兴趣在数据库不存在，则新建一个
                interestFromDB = new Interest();
                interestFromDB.setName(name);
                interestFromDB.setIdentify(application.getBASE_INTERSET_IDENTIFY());
                interestFromDB.setType(InterestTypeAndStatus.SYSTEM_TYPE);
                interestFromDB.setStatus(status);
                //添加兴趣信息
                result = interestService.addInterest(interestFromDB);
            }

            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object openInterestLabelOrNot(Long[] interestIds, Boolean isOpen) {
        if (checkParaNULL(interestIds, isOpen) && interestIds.length > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("ids", interestIds);
            param.put("status", isOpen ? InterestTypeAndStatus.OPEN_STATUS : InterestTypeAndStatus.CLOSE_STATUS);
            //批量更新
            boolean result = interestService.updateInterestBatch(param);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object updateInterestLabel(Long id, String name, Integer status) {
        if (checkParaNULL(id, name, status)) {
            Interest interest = interestService.getInterestById(id);
            if (interest != null) {
                interest.setName(name);
                interest.setStatus(status);
                boolean result = interestService.updateInterest(interest);
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    //============================================== 职业标签 ==============================================


    @Override
    public Object searchJob(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute Job object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Job> list = jobService.getJobList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Job obj = jobService.getJobById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Job> pagingForKeyword = jobService.getJobPage(pageEntity);
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
    public Object addJobLabel(String name, Integer status) {
        if (checkParaNULL(name, status)) {

            Map<String, Object> param = new HashMap<>();
            param.put("name", name);
            param.put("status", status);
            Job jobFromDB = jobService.getJob(param);

            boolean result;
            if (jobFromDB != null) {
                //如果该职业在数据库已存在，则更新该职业
                jobFromDB.setType(JobTypeAndStatus.SYSTEM_TYPE);
                jobFromDB.setStatus(status);
                result = jobService.updateJob(jobFromDB);
            } else {
                //如果该职业在数据库不存在，则新建一个
                jobFromDB = new Job();
                jobFromDB.setName(name);
                jobFromDB.setIdentify(application.getBASE_JOB_IDENTIFY());
                jobFromDB.setType(JobTypeAndStatus.SYSTEM_TYPE);
                jobFromDB.setStatus(status);
                //添加职业信息
                result = jobService.addJob(jobFromDB);
            }

            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object openJobLabelOrNot(Long[] jobIds, Boolean isOpen) {
        if (checkParaNULL(jobIds, isOpen) && jobIds.length > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("ids", jobIds);
            param.put("status", isOpen ? JobTypeAndStatus.OPEN_STATUS : JobTypeAndStatus.CLOSE_STATUS);
            //批量更新
            boolean result = jobService.updateJobBatch(param);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    @Override
    public Object updateJobLabel(Long id, String name, Integer status) {
        if (checkParaNULL(id, name, status)) {
            Job job = jobService.getJobById(id);
            if (job != null) {
                job.setName(name);
                job.setStatus(status);
                boolean result = jobService.updateJob(job);
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    /**
     * 添加公告消息
     *
     * @param noticeList  公告消息列表
     * @param userList     用户列表
     * @param announcement 公告
     */
    public void addAnnouncementMessage(List<Notice> noticeList, List<User> userList, Announcement announcement) {
        //将原先发布公告添加至message表，改成notice表 --modify at 20160905
        for (User user : userList) {
            Notice notice = new Notice();
            //设置消息的接受者
            notice.setUserId(user.getUserId());
            //设置消息的接受者
            notice.setType(MessageType.ANNOUNCEMENT);
            //设置消息的接受者
            notice.setIsRead(false);
            //设置消息的接受者
            notice.setAnnouncementId(announcement.getId());
            //设置消息的接受者
            notice.setContent(announcement.getTitle());
            //消息额外参数
            MessageParam messageParam = new MessageParam();
            messageParam.setAnnouncementContent(announcement.getContent());
            messageParam.setAnnouncementUrl(announcement.getUrl());
            messageParam.setAnnouncementImageUrl(announcement.getImage());
            messageParam.toJSONString();
            notice.setParam(messageParam.toJSONString());
            //添加消息
            noticeList.add(notice);
        }

    }

    /**
     * 添加公告推送地区，已经查询每一个地区的用户
     *
     * @param districtList 推送地区列表
     * @param userList     用户列表
     * @param announcement 公告
     * @param district     地区
     * @param type         类型，标记这个地区是省，市还是区
     */
    public void addAnnouncementType(List<AnnouncementDistrict> districtList, List<String> districtNameList, List<User> userList,
                                    Announcement announcement, Integer district, Integer type) {

        //推送地区对象
        AnnouncementDistrict announcementDistrict = new AnnouncementDistrict();
        announcementDistrict.setAnnouncementId(announcement.getId());

        Map<String, Object> param = new HashMap<>();

        List<User> list = new ArrayList<>();
        if (type == AnnouncementType.DISTRICT_PROVINCE) {
            //查询在该省的用户
            param.put("provinceId", district);
            //添加推送的省
            announcementDistrict.setProvinceId(district);
            announcementDistrict.setType(AnnouncementType.DISTRICT_PROVINCE);
            list = userService.getUserList(param);
            if (list != null && list.size() != 0) {
                //添加地区名字
                districtNameList.add(list.get(0).getProvince());
            }

        } else if (type == AnnouncementType.DISTRICT_CITY) {
            //查询在该市的用户
            param.put("cityId", district);
            //添加推送的市
            announcementDistrict.setCityId(district);
            announcementDistrict.setType(AnnouncementType.DISTRICT_CITY);
            list = userService.getUserList(param);
            if (list != null && list.size() != 0) {
                //添加地区名字
                districtNameList.add(list.get(0).getCity());
            }

        } else {
            //查询在该区的用户
            param.put("areaId", district);
            //添加推送的区
            announcementDistrict.setAreaId(district);
            announcementDistrict.setType(AnnouncementType.DISTRICT_AREA);
            list = userService.getUserList(param);
            if (list != null && list.size() != 0) {
                //添加地区名字
                districtNameList.add(list.get(0).getArea());
            }
        }

        //添加用户
        userList.addAll(list);
        //添加推送地区
        districtList.add(announcementDistrict);
    }


    /**
     * 添加公告推送地区，已经查询每一个地区的用户
     *
     * @param userList     用户列表
     */
    public void addAnnouncementTypeNull(List<User> userList) {

        //推送地区对象
        Map<String, Object> param = new HashMap<>();

        List<User> list = new ArrayList<>();

        list = userService.getUserList(param);

        //添加用户
        userList.addAll(list);

    }

    /**
     *
     * @param rootPath 根目录的路径，域名后的路径
     * @param fileName 文件名
     * @param content 内容
     * @param title html文件的<title></title>
     * @return 是否成功生成文件
     */
    private boolean createFile(String rootPath,String fileName,String content,String title){
        if(checkParaNULL(rootPath,fileName,content,title)){
            String basePath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(rootPath);
            StringBuffer announceHtmlMould = null;
            try {
                //读取公告的html模板
                java.io.File announceHtmlMouldFile = new java.io.File(basePath,"announceHtmlMould.txt");
                BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
                if (announceHtmlMouldFile.isFile()&&announceHtmlMouldFile.exists()){
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(announceHtmlMouldFile),"UTF-8");
                    br = new BufferedReader(reader);
                    announceHtmlMould = new StringBuffer();
                    String str = null;
                    while((str=br.readLine())!=null){
                        announceHtmlMould.append(str);
                    }
                    br.close();
                    reader.close();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //生成对应的文件夹
            java.io.File basePathFile = new java.io.File(basePath);
            if(!basePathFile.exists()){
                basePathFile.mkdirs();
            }
            //生成文件
            java.io.File contentHtml = new java.io.File(basePathFile,fileName);
            FileOutputStream outputStream = null;
            try {
                if(!contentHtml.exists()){
                    contentHtml.createNewFile();
                }
                if (checkParaNULL(announceHtmlMould)){
                    //将模板替换掉对应的位置上
                    outputStream = new FileOutputStream(contentHtml);
                    String announceHtml = announceHtmlMould.toString().replace("{title}",title);
                    announceHtml = announceHtml.replace("{content}",content);

                    outputStream.write(announceHtml.getBytes("UTF-8"));
                    outputStream.close();
                    logger.info("文件生成成功！！！！");
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }


    //============================================== 喜爱夜蒲标签 ==============================================

    @Override
    public Object searchFancy(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute Fancy object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Fancy> list = fancyService.getFancyList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Fancy obj = fancyService.getFancyById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Fancy> pagingForKeyword = fancyService.getFancyPage(pageEntity);
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
    public Object addFancyLabel(String name, Integer status) {
        if (checkParaNULL(name, status)) {

            Map<String, Object> param = new HashMap<>();
            param.put("name", name);
            param.put("status", status);
            Fancy fancyFromDB = fancyService.getFancy(param);

            boolean result;
            if (fancyFromDB != null) {
                //如果该职业在数据库已存在，则更新该职业
                fancyFromDB.setType(FancyTypeAndStatus.SYSTEM_TYPE);
                fancyFromDB.setStatus(status);
                result = fancyService.updateFancy(fancyFromDB);
            } else {
                //如果该职业在数据库不存在，则新建一个
                fancyFromDB = new Fancy();
                fancyFromDB.setName(name);
                fancyFromDB.setIdentify(application.getBASE_FANCY_IDENTIFY());
                fancyFromDB.setType(FancyTypeAndStatus.SYSTEM_TYPE);
                fancyFromDB.setStatus(status);
                //添加职业信息
                result = fancyService.addFancy(fancyFromDB);
            }

            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object openFancyLabelOrNot(Long[] fancyIds, Boolean isOpen) {
        if (checkParaNULL(fancyIds, isOpen) && fancyIds.length > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("ids", fancyIds);
            param.put("status", isOpen ? FancyTypeAndStatus.OPEN_STATUS : FancyTypeAndStatus.CLOSE_STATUS);
            //批量更新
            boolean result = fancyService.updateFancyBatch(param);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    @Override
    public Object updateFancyLabel(Long id, String name, Integer status) {
        if (checkParaNULL(id, name, status)) {
            Fancy fancy = fancyService.getFancyById(id);
            if (fancy != null) {
                fancy.setName(name);
                fancy.setStatus(status);
                boolean result = fancyService.updateFancy(fancy);
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }



}
