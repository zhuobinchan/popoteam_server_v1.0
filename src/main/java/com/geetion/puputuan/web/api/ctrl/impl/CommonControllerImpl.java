package com.geetion.puputuan.web.api.ctrl.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.easemob.server.example.constant.HuanXinConstant;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanXinMessageException;
import com.easemob.server.example.exception.HuanXinUserException;
import com.easemob.server.example.service.HuanXinChatGroupService;
import com.easemob.server.example.service.HuanXinUserService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.oss.FileManager;
import com.geetion.generic.serverfile.oss.OssOption;
import com.geetion.generic.serverfile.service.FileService;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.constant.*;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.utils.UuidUtils;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.pojo.HuanXinMessageExtras;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.supervene.recommend.service.SuperveneRecommendService;
import com.geetion.puputuan.utils.*;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.CommonController;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jian on 16/3/15.
 */
@Controller
public class CommonControllerImpl extends BaseController implements CommonController {

    @Resource
    private UserService userService;

    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;

    @Resource
    private FileManager fileManager;
    @Resource(name = "geetionFileService")
    private FileService fileService;
    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource(name = "geetionShiroService")
    ShiroService shiroService;
    @Resource
    private AdminService adminService;
    @Resource
    private PhotoService photoService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private OssFileUtils ossFileUtils;
    @Resource
    private MessageService messageService;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMemberHistoryService groupMemberHistoryService;
    @Resource
    private CommonService commonService;
    @Resource
    private InterestService interestService;
    @Resource
    private InterestUserService interestUserService;
    @Resource
    private JobService jobService;
    @Resource
    private JobUserService jobUserService;
    @Resource
    private AdminLoginHisService adminLoginHisService;
    @Resource
    private BarService barService;

    private String prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/user/robot/";
    //================================= 地区模块 =======================================

    @Override
    public Object getDistrict(Integer code, Integer type) {
        if (type != null) {
            List<District> list = districtService.getDistrictByTypeAndParent(type, code);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", list);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    //================================== 图片模块 ======================================

    @Override
    public Object upload(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes) {
        try {

            Map<String, Object> map = new HashMap<>();
            String prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/";//默认URL
            String prefixFile = "";//
            String domain = "";//项目文件夹
            String folder = "";//项目子文件夹

            Long userId = shiroService.getLoginUserBase().getId();
            prefixUrl = prefixUrl + "user/" + userId + "/";
            prefixFile = "user-";
            domain = OssOption.DEFAULT_DOMAIN;
            folder = OssOption.DEFAULT_USER + userId + "/";

            //  Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (checkParaNULL(files, sizes) && files.length > 0 && sizes.length > 0 && files.length == sizes.length) {
                System.out.println("\nsize:  " + files.length + "\n");
                List<Long> successIdList = new ArrayList<>();
                List<String> failIdList = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    // 转发文件到oss,获取url
                    // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
                    String fileName = prefixFile + System.currentTimeMillis() + "-" + files[i].getOriginalFilename();
                    boolean isSuccess = fileManager.uploadFile(domain, folder, files[i], fileName);
                    //上传失败要返回对应失败的文件信息，让重新传
                    if (isSuccess) {
                        //上传OSS成功,塞进数据库
                        String url = prefixUrl + fileName;
                        File pojoFile = new File();
                        pojoFile.setUrl(url);
                        pojoFile.setSize(sizes[i]);
                        if (fileService.addFile(pojoFile)) {
                            //入库成功
                            successIdList.add(pojoFile.getId());
                        } else {
                            //入库失败
                            failIdList.add(files[i].getOriginalFilename());
                        }
                    } else {
                        failIdList.add(files[i].getOriginalFilename());
                    }
                }
                map.put("succeed", successIdList);
                map.put("failed", failIdList);

                if ((successIdList.size() + failIdList.size()) == files.length) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, map);
                }
            } else {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return sendResult(ResultCode.CODE_700.code, ResultCode.CODE_700.msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object download(Long[] fileIds) {
        if (fileIds != null && fileIds.length != 0) {
            Map<String, Object> result = new HashMap<>();
            //批量获取file信息
            List<File> list = fileService.getFileBatch(fileIds);
            List<File> fileResult = new ArrayList<>();
            if (list != null) {
                //循环遍历加密file的url
                for (int index = 0; index < list.size(); index++) {
                    String fileUrl = list.get(index).getUrl();
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf("-") + 1, fileUrl.length());
                    String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1, fileUrl.length());
                    //  System.out.println("\n\n截取的url是:  " + suffix + "\n\n");
                    if (fileUrl != null && !fileUrl.isEmpty()) {
                        OSSClient client = new OSSClient(OssOption.ENDPOINT,
                                application.getOssAccessToken().getAccessKeyId(),
                                application.getOssAccessToken().getAccessKeySecret(),
                                application.getOssAccessToken().getSecurityToken());
                        // 设置URL过期时间为1小时
                        Date expiration = new Date(new Date().getTime() + 60 * 60 * 1000);
                        String cutUrl = fileUrl.substring(52);
                        System.out.println("\n截取的url是:  " + cutUrl + "\n");
                        // 生成URL
                        URL finalUrl = client.generatePresignedUrl("bucket-puputuan", cutUrl, expiration);
                        File pojoFile = new File();
                        pojoFile.setId(list.get(index).getId());
                        pojoFile.setUrl(finalUrl.toString());
                        pojoFile.setSuffix(suffix);
                        pojoFile.setName(fileName);
                        pojoFile.setSize(list.get(index).getSize());
                        pojoFile.setCreateTime(list.get(index).getCreateTime());
                        fileResult.add(pojoFile);
                    }
                }
                result.put("list", fileResult);
                if (result != null && result.size() != 0) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, result);
                } else {
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }
            } else {
                //传错了文件id
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }
        //传少了参数
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);

    }

    @Override
    public Object getPictures(Long[] fileIds, String style) {
        if (fileIds != null && fileIds.length != 0) {
            Map<String, Object> result = new HashMap<>();
            List<File> fileResult = new ArrayList<>();
            //批量获取file信息
            List<File> list = fileService.getFileBatch(fileIds);
            if (list != null) {
                for (int index = 0; index < list.size(); index++) {
                    String fileUrl = list.get(index).getUrl();
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf("-") + 1, fileUrl.length());
                    String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1, fileUrl.length());
                    String url = fileUrl + (style == null ? "" : "@" + style);
                    File pojoFile = new File();
                    pojoFile.setId(list.get(index).getId());
                    pojoFile.setUrl(url);
                    pojoFile.setSuffix(suffix);
                    pojoFile.setName(fileName);
                    pojoFile.setSize(list.get(index).getSize());
                    pojoFile.setCreateTime(list.get(index).getCreateTime());
                    fileResult.add(pojoFile);
                }
                result.put("list", fileResult);
                if (result != null && result.size() != 0) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, result);
                } else {
                    return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                }
            } else {
                //传错了文件id
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    @Override
    public String redirectPicture(Long fileId, String style) {

        if (checkParaNULL(fileId)) {
            File file = fileService.getFileById(fileId);
            if (file == null) {
                return "";
            }
            String fileUrl = file.getUrl();
            String url = fileUrl + (style == null ? "" : "@" + style);

            return "redirect:" + url;
        }
        return "";
    }


    //================================== 其他 ======================================


    @Override
    public ModelAndView login() {
        try {
            if (shiroService.getLoginUserBase() != null) {
                return sendResult("redirect:/", null);
            }
        } catch (UnknownAccountException e) {
            return sendResult(ResultCode.CODE_200.code, "login.jsp", ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_200.code, "login.jsp", ResultCode.CODE_200.msg, null);
    }

    @Override
    public ModelAndView login(String account, String password) {
        if (checkParaNULL(account, password)) {
            Map<String, Object> resultMap = new HashMap<>();
            try {
                UserBase userBase = (UserBase) shiroService.login(account, password, "ctrl");
                if (userBase != null) {
                    //将用户基本信息存进session
                    shiroService.setLoginUserBase(userBase);
                    Admin admin = adminService.getAdminByUserId(userBase.getId());
                    if (admin != null) {
                        //将管理员信息存进session
                        shiroService.setLoginUser(admin);
                        addLoginHis(admin);
                        resultMap.put("object", admin);
                        return sendResult("redirect:/", resultMap);
                    }
                    shiroService.logout();
                    return sendResult(ResultCode.CODE_700.code, "login.jsp", ResultCode.CODE_700.msg, null);
                }
                return sendResult(ResultCode.CODE_700.code, "login.jsp", ResultCode.CODE_700.msg, null);
            } catch (UnknownAccountException uae) {
                return sendResult(ResultCode.CODE_700.code, "login.jsp", ResultCode.CODE_700.msg, null);
            } catch (IncorrectCredentialsException ice) {
                ice.printStackTrace();
                return sendResult(ResultCode.CODE_701.code, "login.jsp", ResultCode.CODE_701.msg, null);
            } catch (LockedAccountException lae) {
                return sendResult(ResultCode.CODE_706.code, "login.jsp", ResultCode.CODE_706.msg, null);
            } catch (IllegalArgumentException iae) {
                return sendResult(ResultCode.CODE_703.code, "login.jsp", ResultCode.CODE_703.msg, null);
            } catch (ExcessiveAttemptsException e) {
                return sendResult(ResultCode.CODE_710.code, "login.jsp", ResultCode.CODE_710.msg, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, "login.jsp", ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, "login.jsp", ResultCode.CODE_401.msg, null);
    }

    /**
     * 记录管理员登录历史
     * @param admin
     */
    private void addLoginHis(Admin admin){

        AdminLoginHis adminLoginHis = new AdminLoginHis();
        adminLoginHis.setUserId(admin.getUserId());
        adminLoginHis.setNickName(admin.getNickName());
        adminLoginHis.setCreateTime(new Date());

        adminLoginHisService.addAdminLoginHis(adminLoginHis);
    }
    @Override
    public ModelAndView logout() {
        if (shiroService.getLoginUserBase() != null && shiroService.getLoginUser() != null) {
            shiroService.logout();
        }
        return sendResult("redirect:/login", null);
    }

    @Override
    @Transactional(timeout=600)
    public Object uploadRobotConf(@RequestParam("uploadfiles") CommonsMultipartFile[] files, Long[] sizes) {

        if (checkParaNULL(files, sizes) && files.length > 0 && sizes.length > 0 && files.length == sizes.length) {

            if(!files[0].getOriginalFilename().endsWith("xls")) {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }

            HSSFWorkbook wb = null;
            try {
                wb = new HSSFWorkbook(files[0].getInputStream());
                // logger.debug(wb.getNumberOfSheets());
                HSSFSheet sheet = wb.getSheetAt(0);
                logger.debug("sheet name = "+wb.getSheetName(0));

                Map<String, String> map = getAreaAndBar(wb.getSheetAt(1));

                String phone = null;
                String nickName = null;
                String birthday = null;
                String sex = null;
                String interestOne = null;
                String interestTwo = null;
                String job = null;
                String sign = null;
                String photoUrl = null;
                String picStrings = null;
                List<User> usersFemale = new ArrayList<>();
                List<User> usersMale = new ArrayList<>();
                User user = null;
                DecimalFormat df = new DecimalFormat("0");
                for(int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++){
                    HSSFRow row = sheet.getRow(i);
                    Iterator cells = row.cellIterator();
                    while(cells.hasNext()){
                        HSSFCell cell = (HSSFCell) cells.next();
                        switch (cell.getColumnIndex()){
                            case 0:
                                // 手机号码
//                                phone = cell.getStringCellValue();
                                phone = df.format(cell.getNumericCellValue());
                                break;
                            case 1:
                                // 昵称
                                nickName = cell.getStringCellValue();
                                break;
                            case 2:
                                // 生日
                                birthday = cell.getStringCellValue();
                                break;
                            case 3:
                                // 性别
                                sex = cell.getStringCellValue();
                                break;
                            case 4:
                                // 兴趣1
                                interestOne = cell.getStringCellValue();
                                break;
                            case 5:
                                // 兴趣2
                                interestTwo = cell.getStringCellValue();
                                break;
                            case 6:
                                // 职业
                                job = cell.getStringCellValue();
                                break;
                            case 7:
                                // 个人说明
                                sign = cell.getStringCellValue();
                                break;
                            case 8:
                                photoUrl = cell.getStringCellValue();
                                break;
                            case 9:
                                picStrings = cell.getStringCellValue();
                        }
                    }
                    if(null != nickName && !nickName.equals("")){
                        user = this.generateUser(phone, nickName, birthday, sex, interestOne, interestTwo, job, sign, photoUrl, picStrings);
                        if(null != user){
                            if(SexType.FEMALE.equals(sex)){
                                usersFemale.add(user);
                            }else{
                                usersMale.add(user);
                            }
                        }
                    }

                }

                generateFriendAndGroup(usersFemale, map);
                generateFriendAndGroup(usersMale, map);

            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception ex){
                ex.printStackTrace();
            }

        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    /**
     *  取得sheet中的地区信息跟约会类型
     * @param sheet
     * @return
     */
    private Map<String, String> getAreaAndBar(HSSFSheet sheet){

        Map<String, String> map = new HashMap<>();

        for(int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++){
            HSSFRow row = sheet.getRow(i);
            Iterator cells = row.cellIterator();
            while(cells.hasNext()){
                HSSFCell cell = (HSSFCell) cells.next();
                switch (cell.getColumnIndex()){
                    case 0:
                        map.put("province", cell.getStringCellValue());
                        break;
                    case 1:
                        map.put("provinceId", cell.getStringCellValue());
                        break;
                    case 2:
                        map.put("city", cell.getStringCellValue());
                        break;
                    case 3:
                        map.put("cityId", cell.getStringCellValue());
                        break;
                    case 4:
                        map.put("area", cell.getStringCellValue());
                        break;
                    case 5:
                        map.put("areaId", cell.getStringCellValue());
                        break;
                    case 6:
                        map.put("barId", cell.getStringCellValue());
                        break;
                }
            }
        }

        return map;
    }

    /**
     * 自动生成用户
     * @param phone
     * @param nickName
     * @param birthday
     * @param sex
     * @return
     */
    private User generateUser(String phone, String nickName, String birthday, String sex,
                              String interestOne, String interestTwo, String job, String sign, String photoUrl,String picStrings){
        Map<String, Object> param = new HashMap<>();
        param.put("phone", "+86" + phone);
        User user = userService.getUser(param);
        if (user == null) {
            try {
                UserBase userBase = new UserBase();
                userBase.setAccount("popoteam_user_" + System.currentTimeMillis());
                userBase.setToken(UuidUtils.generateUuid());
                // 默认密码
                userBase.setPassword("e10adc3949ba59abbe56e057f20f883e");
                // 添加用户基本信息
                userBaseService.add(userBase);

                List<String> interestStrings = new ArrayList<>();
                if(checkParaNULL(interestOne)){
                    interestStrings.add(interestOne);
                }
                if(checkParaNULL(interestTwo)){
                    interestStrings.add(interestTwo);
                }
                // 添加兴趣
                this.generateInterest(userBase.getId(), interestStrings);

                // 添加职业
                if(checkParaNULL(job)){
                    this.generateJob(userBase.getId(), job);
                }

                // 添加用户头像
                Photo photo = this.generateAvatar(photoUrl, picStrings, userBase.getId());

                user = new User();
                user.setUserId(userBase.getId());
                user.setPhone("+86" + phone);
                user.setIdentify(application.getBASE_USER_IDENTIFY() + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                user.setBirthday(sdf.parse(birthday));
                user.setConstellation(getConstellation(user.getBirthday()));
                //设置性别只能是 M 或 F
                user.setSex(sex.equals(SexType.MALE) ? SexType.MALE : SexType.FEMALE);
                user.setNickName(nickName);
                user.setNickNameChr(PinyinHelper.convertToPinyinString(nickName, "", PinyinFormat.WITHOUT_TONE));
                user.setHeadId(photo.getImageId());
                user.setUserBase(userBase);

                List <Photo> album = new ArrayList<>();
                album.add(photo);
                user.setAlbum(album);

                //添加用户详细信息
                userService.addUser(user);

                /**  注册环信 注册成功之后再向数据库插入数据 */
                //查询用户是否存在
                boolean isUserExist = HuanXinUserService.userExistOrNot(userBase.getAccount());
                if (!isUserExist) {
                    //如果环信用户不存在，则新建一个
                    HuanXinUserService.createNewIMUser(userBase.getAccount(), user.getNickName());
                }

                return user;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (HuanXinUserException e) {
                e.printStackTrace();
            } catch (PinyinException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加用户默认头像
     * @param photoUrl
     * @param userId
     * @return
     */
    private Photo generateAvatar(String photoUrl,String picStrings, Long userId){
        //默认头像位置
        String url = prefixUrl + photoUrl + "/";
//        String prefixFile = "user-";
//        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹
//        String folder = OssOption.DEFAULT_USER + userId + "/";//项目子文件夹
//        System.getProperty("user.dir");
//        File file = new File();
//        // 转发文件到oss,获取url
//        // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
//        String fileName = prefixFile + System.currentTimeMillis() + "-" + file.getName();
//        OssManager manager = new OssManager();
//        OSSClient client = manager.getOssClient();
//
////        boolean isSuccess = fileManager.uploadFile(domain, folder, file, fileName);
////        if (!isSuccess) {
////            return null;
////        }
//        File pojoFile = new File();
//        pojoFile.setUrl(url);
//        //pojoFile.setSize("");
//        fileService.addFile(pojoFile);
//
//        //插入相册表,设置为头像
//        Photo photo = new Photo();
//        //默认头像Id
//        photo.setImageId(pojoFile.getId());
//        photo.setUserId(userId);
//        photo.setIsAvatar(true);
//        photoService.addPhoto(photo);

        Photo photo = new Photo();
        if(checkParaNULL(picStrings)){
            String[] picList = picStrings.split(",");

            for(int i=0; i < picList.length; i++){
                File picFile = new File();
                picFile.setUrl(url + picList[i]);
                //pojoFile.setSize("");
                fileService.addFile(picFile);

                //插入相册表,设置为头像
                Photo pic = new Photo();
                //默认头像Id
                pic.setImageId(picFile.getId());
                pic.setUserId(userId);
                if(0 == i){
                    pic.setIsAvatar(true);
                    photo = pic;
                }else{
                    pic.setIsAvatar(false);
                }

                photoService.addPhoto(pic);
            }
        }
        return photo;
    }

    /**
     * 添加用户兴趣
     * @param userId
     * @param interestStrings
     */
    private void generateInterest(Long userId, List<String> interestStrings){
        HashMap<String, Object> param = new HashMap<>();
        for (String interestName : interestStrings) {
            param.clear();
            param.put("name", interestName);
            Interest interestFromDB = interestService.getInterest(param);
            InterestUser interestUser = new InterestUser();
            interestUser.setUserId(userId);
            if (interestFromDB != null) {
                //step3.1：如果该兴趣在数据库已存在，使用该兴趣的id
                interestUser.setInterestId(interestFromDB.getId());
                interestUserService.addInterestUser(interestUser);
            }

        }
    }

    /**
     * 添加用户职业
     * @param userId
     * @param job
     */
    private void generateJob(Long userId, String job){
        Map<String, Object> param = new HashMap<>();
        param.put("name", job);
        Job jobFromDB = jobService.getJob(param);
        JobUser jobUser = new JobUser();
        jobUser.setUserId(userId);
        if (jobFromDB != null) {
            //step2.1：如果该职业在数据库已存在，使用该职业的id
            jobUser.setJobId(jobFromDB.getId());
            jobUserService.addJobUser(jobUser);
        }


    }

    /**
     * 自动生成互为好友关系。
     * 默认3个人为好友，第一个人添加另外2个人为好友。
     * 当人数只有3、2个人的时候，也一样。
     * 当人数为1人时，不处理
     * @param users
     * @param map
     */
    private void generateFriendAndGroup(List<User> users, Map<String, String> map){

        int perNum = 2;
        int circleNumForF = users.size() / perNum ;
        int circleNumForM = users.size() % perNum;


        for(User user : users){
            generateFriendAndMessage(user, users);
        }
        int index = 0;
        for(int i = 0; i < circleNumForF; i++){
            generateGroup(users.get(index), users.subList(index + 1, index + perNum), map);
            index = index + perNum;
        }

        if(circleNumForM >= 2){
            generateGroup(users.get(index), users.subList(index + 1, users.size()), map);
        }
    }

    private void generateFriendAndMessage(User user, List<User> friends){

        List<FriendApply> friendApplyList = new ArrayList<>();
        for(User friend : friends){

            if(user.getId().equals(friend.getId())){
                continue;
            }

            FriendApply friendApply = new FriendApply();
            //userId 为好友id
            friendApply.setUserId(user.getUserId());
            friendApply.setFriendId(friend.getUserId());
            friendApply.setType(FriendApplyType.AGREE);
            //添加好友申请
            friendApplyService.addFriendApply(friendApply);
            //发送通知消息
            addFriendMessage(friend.getUserId(), "已添加好友",MessageType.FRIEND_APPLY_RESULT, friendApply.getId(), user);
            friendApplyList.add(friendApply);
        }
        addFriendRelationShip(friendApplyList);
    }

    /**
     * 添加消息进数据库
     *
     * @param userId        要接受消息的用户userId
     * @param content       消息内容
     * @param friendApplyId 好友申请的id
     * @param user          用户对象
     * @return 是否添加成功
     */
    private boolean addFriendMessage(Long userId, String content, int type, Long friendApplyId, User user) {

        Message message = new Message();
        //要接受消息的用户
        message.setUserId(userId);
        message.setContent(content);
        message.setType(type);
        message.setIsRead(true);
        //消息额外参数
        MessageParam messageParam = new MessageParam();
        messageParam.setFriendApplyId(friendApplyId);
        messageParam.setAnnouncementContent(null);
        ossFileUtils.getUserHead(user, null);
        messageParam.setUserBase(ConvertBeanUtils.convertDBModelToJSONModel(user));

        message.setParam(messageParam.toJSONString());
        //添加消息
        return messageService.addMessage(message);
    }

    /**
     * 添加好友关系
     * @param friendApplyList
     */
    public void addFriendRelationShip(List<FriendApply> friendApplyList){

        List<FriendRelationship> list = new ArrayList<>();

        for(FriendApply friendApply : friendApplyList){
            FriendRelationship friendRelationship1 = new FriendRelationship();
            friendRelationship1.setUserId(friendApply.getUserId());
            friendRelationship1.setFriendId(friendApply.getFriendId());
            //表示好友的来源是通过搜索Id加的
            friendRelationship1.setType(FriendType.FROM_SEARCH);
            list.add(friendRelationship1);

            FriendRelationship friendRelationship2 = new FriendRelationship();
            friendRelationship2.setUserId(friendApply.getFriendId());
            friendRelationship2.setFriendId(friendApply.getUserId());
            //表示好友的来源是通过搜索Id加的
            friendRelationship2.setType(FriendType.FROM_SEARCH);
            list.add(friendRelationship2);

        }
        if(list.size() > 0){
            friendRelationshipService.addFriendRelationshipBatch(list);
        }
    }

    public void generateGroup(User user, List<User> groupMembers, Map<String, String> map){
        List<GroupMember> groupMemberList = new ArrayList<>();

        Group group = new Group();
        group.setToken(UuidUtils.generateUuid());
        group.setName(user.getNickName() + "的蒲团");
        group.setType(user.getSex().equals(SexType.MALE) ? GroupTypeAndStatus.GROUP_MALE : GroupTypeAndStatus.GROUP_FEMALE);
        group.setStatus(GroupTypeAndStatus.GROUP_MATCHING);

        group.setProvinceId(Integer.valueOf(map.get("provinceId")));
        group.setProvince(map.get("province"));

        group.setCityId(Integer.valueOf(map.get("cityId")));
        group.setCity(map.get("city"));

        group.setAreaId(Integer.valueOf(map.get("areaId")));
        group.setArea(map.get("area"));

        group.setBarId(Long.valueOf(map.get("barId")));

        if(null != map.get("sex")){
            group.setRecommendSex(Integer.valueOf(map.get("sex")));
        }

        if (groupService.addGroup(group)) {
            //添加队长进群
            GroupMember leadMember = addGroup(group.getId(), user.getUserId(),
                    GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
            groupMemberList.add(leadMember);

            List<String> members = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            for(User player : groupMembers){
                //获得每一个用户的账户作为环信的用户名
                members.add(player.getUserBase().getAccount() + "");
                // 拉队友进群
//                            GroupMember playMember = addGroup(group.getId(), player.getUserId(),
//                                    GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE);
                GroupMember playMember = addGroup(group.getId(), player.getUserId(),
                        GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_PLAYER, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
                groupMemberList.add(playMember);
                sb.append(EmojiCharacterUtil.emojiRecovery2(player.getNickName()) + "、");
            }

            //创建环信聊天群，如果成功则获得创建的环信群聊id
            String roomId = null;
            try {
                roomId = HuanXinChatGroupService.createChatGroup(user.getNickName() + "的蒲团", "蒲团群聊", false,
                        4L, false, user.getUserBase().getAccount() + "", (String[])members.toArray(new String[members.size()]));
                group.setRoomId(roomId);
                //保存环信的聊天id进数据库
                groupService.updateGroup(group);

                HuanXinMessageExtras huanXinMessageExtras = new HuanXinMessageExtras();
                huanXinMessageExtras.setAccount(user.getUserBase().getAccount());
                huanXinMessageExtras.setHeadId(user.getHeadId());
                huanXinMessageExtras.setUserId(user.getUserId());
                huanXinMessageExtras.setNickName(user.getNickName());
                huanXinMessageExtras.setGroupId(group.getId());
                huanXinMessageExtras.setRoomId(roomId);
                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER,
                        user.getNickName() + " 邀请您加入群", HuanXinSendMessageType.INVITE, huanXinMessageExtras, (String[])members.toArray(new String[members.size()]));
                //发送环信消息 推送开始匹配状态给队员
//                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_USERS, HuanXinConstant.SYSUSER, "队伍开始匹配",
//                        HuanXinSendMessageType.START_MATCHING, null,
//                        commonService.getGroupMemberAccountOrToken(group.getId(), true, CommonService.USER_ACCOUNT));

                Bar bar = barService.getBarById(group.getBarId());

                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        "约会：" + bar.getName() + " (" + group.getArea() + ")\n", HuanXinSendMessageType.ADD_OR_QUIT,
                        null, group.getRoomId());

                HuanXinSendMessageUtils.sendMessage(HuanXinConstant.MESSAGE_CHATGROUPS, HuanXinConstant.SYSUSER,
                        user.getNickName() + "  邀请  " + sb.substring(0, sb.length() - 1) + "  加入队伍\n", HuanXinSendMessageType.ADD_OR_QUIT,
                        null, group.getRoomId());

            } catch (HuanXinChatGroupException e) {
                e.printStackTrace();
            } catch (HuanXinMessageException e) {
                e.printStackTrace();
            }
            // 调用匹配线程池，执行匹配引擎
        }
    }

    private GroupMember addGroup(Long groupId, Long userId, Integer type, Integer status) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMember.setType(type);
        groupMember.setStatus(status);

        GroupMemberHistory groupMemberHistory = new GroupMemberHistory();
        groupMemberHistory.setGroupId(groupId);
        groupMemberHistory.setUserId(userId);
        groupMemberHistory.setType(type);
        groupMemberHistory.setStatus(status);

        return (addGroupMember(groupId, userId, type, status) && addGroupMemberHistory(groupId, userId, type, status)) == true ? groupMember : null;
    }

    /**
     * 添加队友进群
     *
     * @param groupId 团队群id
     * @param userId  用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */

    public boolean addGroupMember(Long groupId, Long userId, Integer type, Integer status) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMember.setType(type);
        groupMember.setStatus(status);
        return groupMemberService.addGroupMember(groupMember);
    }

    /**
     * 添加队友进群历史记录表
     *
     * @param groupId 团队群id
     * @param userId  用户id
     * @param type    类型：队长或队员
     * @param status  状态：邀请，加入，踢出
     * @return 是否添加成功
     */
    public boolean addGroupMemberHistory(Long groupId, Long userId, Integer type, Integer status) {
        GroupMemberHistory groupMemberHistory = new GroupMemberHistory();
        groupMemberHistory.setGroupId(groupId);
        groupMemberHistory.setUserId(userId);
        groupMemberHistory.setType(type);
        groupMemberHistory.setStatus(status);
        return groupMemberHistoryService.addGroupMemberHistory(groupMemberHistory);
    }


    public String getConstellation(Date birthday) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] starArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};

        int[] beginDay = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日--开始
        int[] endDay = {19, 18, 20, 20, 20, 21, 22, 22, 22, 22, 21, 21};  // 两个星座分割日--结束

        // 所查询日期在分割日之后，索引+1，否则不变
        if (day > endDay[month]) {
            month = month + 1;
        }
        //当超过12月份的，则回到1月份
        if (month >= 0 && month <= 11) {
            return starArr[month];
        }
        // 返回索引指向的星座string
        return starArr[0];
    }

    public Map<String, Object> uploadRobotConf(HttpServletRequest request, HttpServletResponse res) {
        System.out.println("in");
        //接收参数
        int id= Integer.parseInt(request.getParameter("id"));
        System.out.println("id=="+id);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("result", "error");

        //解析器解析request的上下文
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        //先判断request中是否包涵multipart类型的数据，
        if(multipartResolver.isMultipart(request)){
            //再将request中的数据转化成multipart类型的数据
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator iter = multiRequest.getFileNames();
            while(iter.hasNext()){
                //这里的name为fileItem的alias属性值，相当于form表单中name
                String name=(String)iter.next();
                System.out.println("name:"+name);
                //根据name值拿取文件
                MultipartFile file = multiRequest.getFile(name);
                if(file != null){
                    String fileName;
                    fileName = file.getOriginalFilename();
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
                            fileName.lastIndexOf(".") + 4);
                }
                HSSFWorkbook wb = null;
                try {
                    wb = new HSSFWorkbook(multiRequest.getInputStream());
                    // logger.debug(wb.getNumberOfSheets());
                    HSSFSheet sheet = wb.getSheetAt(0);
                    logger.debug("sheet name = "+wb.getSheetName(0));
                    for(int i = sheet.getFirstRowNum();i<=sheet.getLastRowNum();i++){
                        HSSFRow row = sheet.getRow(i);
                        Iterator cells = row.cellIterator();
                        while(cells.hasNext()){
                            HSSFCell cell = (HSSFCell) cells.next();
                            logger.debug(cell.getStringCellValue());
                        }
                    }
                    logger.debug("last row = "+sheet.getLastRowNum());

                    wb.getNumberOfSheets();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {

            return resMap;
        }
        resMap.put("result", "success");
        return resMap;
    }

    @Override
    public ModelAndView index() {
        return sendResult(ResultCode.CODE_200.code, "index.html", ResultCode.CODE_200.msg, null);
    }

    private SuperveneRecommendService superveneRecommendService = new SuperveneRecommendService();

    @Override
    public Object test(String teamId) {
//        superveneRecommendService.doRecommend(Long.parseLong(teamId));

        //查询 roomId 对应的环信群组
//        ResponseWrapper responseWrapper = HuanXinChatGroupService.getChatGroupDetails("201272918380904892");
//        if (responseWrapper.getResponseStatus() != null && responseWrapper.getResponseStatus() == 200) {
//            //responseStatus 等于200表示群组存在
//            //执行删除环信群成员功能（）
//            System.out.println("\n\n\nremoveSingleUserFromChatGroup " + HuanXinChatGroupService.removeSingleUserFromChatGroup("201272918380904892", "1"));
//        } else {
//            //responseStatus 等于404表示群组不存在，则群成员也不存在
//            System.out.println("\n\n\n"+(responseWrapper.getResponseStatus() != null && responseWrapper.getResponseStatus() == 404));
//        }

        JPushUtils.sendGroupJPush("新的JPush测试", "新的JPush测试", 0, null, null, "1");


        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    @Override
    public Object clearGroupsHuanXin(@RequestParam("uploadfiles")CommonsMultipartFile[] files) {

        Map<String, Object> resultMap = new HashMap<>();
        List<JSONObject> dataList = new ArrayList<>();
        String cursor = null;

        if(checkParaNULL(files)){
//            Object chatGroups = HuanXinChatGroupService.getChatGroups(1000L, cursor);
//            ObjectNode responseBody = (ObjectNode) chatGroups.getResponseBody();
//            ObjectNode responseBody = new ObjectNode(JsonNodeFactory.instance);
//            ArrayNode dataList = (ArrayNode)responseBody.get("data");

            do {
                Object jsonBody = HuanXinChatGroupService.getChatGroups(1000L, cursor);

                if (jsonBody == null) {
                    break;
                }

                JSONObject jsonObject = JSON.parseObject((String)jsonBody);
                cursor = jsonObject.getString("cursor");
                JSONArray data = jsonObject.getJSONArray("data");

                if (data != null && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        dataList.add(data.getJSONObject(i));
                    }
                } else {
                    break;
                }
            } while (cursor != null && !cursor.equals(""));

            CommonsMultipartFile file = files[0];
            Map<String, String> validRoomMap = new HashMap<>();
            try {

                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String str = null;
                while((str = bufferedReader.readLine()) != null)
                {
                    validRoomMap.put(str,str);
                }

                //close
                bufferedReader.close();

                int notToDelete = 0;
                int toDelete = 0;
                for(int i = 0; i < dataList.size(); i++){

                    String groupName = dataList.get(i).get("groupname").toString().replace("\"","");
                    String groupId = dataList.get(i).get("groupid").toString().replace("\"","");
                    logger.info("group name: " + groupName + " and id: " + groupId);

                    if(validRoomMap.containsKey(groupId)){
                        logger.info("room id not to delete ");
                        notToDelete ++;
                    }else {
                        logger.info("room id to delete " + groupId);
                        toDelete ++;

                        HuanXinChatGroupService.deleteChatGroup(groupId);
                    }

                }
                logger.info("count of  to delete " + toDelete);
                logger.info("count of not to delete " + notToDelete);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (HuanXinChatGroupException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public Object clearUsersHuanXin(@RequestParam("uploadfiles") CommonsMultipartFile[] files, String cursor) {
        Map<String, Object> resultMap = new HashMap<>();
        CommonsMultipartFile file = files[0];
        Map map = this.readHuanXinFile(file);

        Object imUserBatch = HuanXinUserService.getIMUserBatch(1000L, null);
        if (imUserBatch == null) {
            return sendResult(ResultCode.CODE_600.code, ResultCode.CODE_600.msg, null);
        }

        JSONObject jsonObject = JSON.parseObject((String)imUserBatch);
        cursor = jsonObject.getString("cursor");
        JSONArray entities = jsonObject.getJSONArray("entities");
        if (entities != null && entities.size() > 0) {
            resultMap.put("cursor", cursor);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }

        // 默认过滤系统用户
        map.put("SysUser", "SysUser");
        int notToDelete = 0;
        int toDelete = 0;
        for(int i = 0; i < entities.size(); i++){
            if( null == entities.getJSONObject(i).get("username")){
                notToDelete ++;
                logger.info("userName is null");
                continue;
            }

            String nickName = "";
            if( null != entities.getJSONObject(i).get("nickname")){
                nickName = entities.getJSONObject(i).get("nickname").toString().replace("\"","");
            }

            String userName = entities.getJSONObject(i).get("username").toString().replace("\"","");
            logger.info("nickName : " + nickName + " and userName: " + userName);

            if(map.containsKey(userName)){
                logger.info("user not to delete ");
                notToDelete ++;
            }else {
                logger.info("user to delete ");
                try {
                    HuanXinUserService.deleteIMUserByUserName(userName);
//                    System.out.println(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toDelete ++;
            }
        }
        logger.info("count of  to delete " + toDelete);
        logger.info("count of not to delete " + notToDelete);
        resultMap.put("cursor", cursor);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object uploadFriendFile(@RequestParam("friendFile") CommonsMultipartFile[] files, Long[] sizes, Integer userType, Integer actionType) {


        List<List<User>> userFromFile = this.getUserFromFile(files[0], userType);

        switch (actionType){
            case 1:
                for(List<User> ul : userFromFile){
                    if(ul.size() >= 2){

                        User user = ul.get(0);
                        if(null == user){
                            continue;
                        }
                        List<User> userList1 = ul.subList(1, ul.size());
                        Map<String, Object> param = new HashMap<>();
                        for(User f : userList1){
                            if(null == f){
                                continue;
                            }
                            param.clear();
                            param.put("userId", user.getUserId());
                            param.put("friendId", f.getUserId());
                            param.put("type", FriendApplyType.SENDING);
                            // 查询当前用户是否发过好友申请
                            FriendApply friendApply = friendApplyService.getFriendApply(param);
                            if (friendApply != null) {
                                param.clear();
                                param.put("userId", f.getUserId());
                                param.put("content", user.getNickName() + "申请添加好友");

                                Message message = messageService.getMessage(param);
                                if (message != null) {
                                    message.setCreateTime(new Date());
                                    message.setIsRead(false);
                                    messageService.updateMessage(message);
                                }else{
                                    //当好友申请消息被删除时，需要添加新的申请消息
                                    //发送通知消息
                                    addFriendMessage(f.getUserId(), user.getNickName() + "申请添加好友",
                                            MessageType.FRIEND_APPLY, friendApply.getId(), user);

                                }
                            } else {
                                friendApply = new FriendApply();
                                //userId 为好友id
                                friendApply.setUserId(user.getUserId());
                                friendApply.setFriendId(f.getUserId());

                                //添加好友申请
                                friendApplyService.addFriendApply(friendApply);

                                //发送通知消息
                                addFriendMessage(f.getUserId(), user.getNickName() + "申请添加好友",
                                        MessageType.FRIEND_APPLY, friendApply.getId(), user);
                            }

                            JPushUtils.sendFriendApplyJPush("好友申请", user.getNickName() + "申请添加好友",
                                    JPushType.FRIEND_APPLY, friendApply.getId(), f.getUserBase().getAccount());
                        }

                    }
                }
                break;
            case 2:
                for(List<User> ul : userFromFile){
                    if(ul.size() >= 2){
                        // 直接生成好友关系
                        this.generateFriendAndMessage(ul.get(0), ul.subList(1, ul.size()));
                    }
                }

                break;
        }

        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    @Override
    public Object getBarList() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        params.put("status", 1);
        List<Bar> barList = barService.getBarList(params);
        resultMap.put("list", barList);
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
    }

    @Override
    public Object uploadGroupFile(@RequestParam("groupFile") CommonsMultipartFile[] files, Long[] sizes, Integer userType, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Long barId, String sex) {
        List<List<User>> userFromFile = this.getUserFromFile(files[0], userType);
        Map<String, String> map = new HashMap<>();
        map.put("provinceId", provinceId.toString());
        map.put("province", province);
        map.put("cityId", cityId.toString());
        map.put("city", city);
        map.put("areaId", areaId.toString());
        map.put("area", area);
        map.put("barId", barId.toString());
        map.put("sex", sex);
        for(List<User> ul : userFromFile){
            if(ul.size() >= 2){
                this.generateGroup(ul.get(0), ul.subList(1, ul.size()), map);
            }
        }
        return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
    }

    private List<List<User>> getUserFromFile(CommonsMultipartFile file, Integer userType){
        String str = "";
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        List<String> userList = new ArrayList<>();

        try {
            br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            while ((str = br.readLine()) != null) {
                userList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<User>> userFromFile = new ArrayList<>();
        for(String ur : userList){
            List<User> userToAdd = new ArrayList<>();
            String[] split = ur.split(",");

            for (int i = 0; i < split.length; i++){
                switch (userType){
                    case 1:
                        userToAdd.add(userService.getUserByIdentify(split[i]));
                        break;
                    case 2:
                        userToAdd.add(userService.getUserByPhone(split[i]));
                        break;
                }
            }
            userFromFile.add(userFromFile.size(), userToAdd);
        }
        return userFromFile;
    }

    private Map readHuanXinFile(CommonsMultipartFile file) {
        Map<String, String> validRoomMap = new HashMap<>();
        try {
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                validRoomMap.put(str, str);
            }
            //close
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return validRoomMap;
    }
}
