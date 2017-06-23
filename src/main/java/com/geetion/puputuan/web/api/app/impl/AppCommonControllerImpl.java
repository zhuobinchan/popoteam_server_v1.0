package com.geetion.puputuan.web.api.app.impl;

import com.aliyun.oss.OSSClient;
import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.oss.FileManager;
import com.geetion.generic.serverfile.oss.OssOption;
import com.geetion.generic.serverfile.oss.OssPermissionManager;
import com.geetion.generic.serverfile.service.FileService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.app.AppCommonController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class AppCommonControllerImpl extends BaseController implements AppCommonController {


    @Resource(name = "geetionShiroService")
    ShiroService shiroService;
    @Resource(name = "geetionDistrictService")
    private DistrictService districtService;
    @Resource
    private FileManager fileManager;
    @Resource(name = "geetionFileService")
    private FileService fileService;
    @Resource(name = "puputuanApplication")
    private Application application;

    @Resource
    private OssPermissionManager ossPermissionManager;
    @Resource
    private OssFileUtils ossFileUtils;

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

    //================================文件图片上下传==============================================

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
                    String cutUrl = fileUrl.substring(52);
//                    String url = ossPermissionManager.cipherUrl(application.getOssAccessToken().getAccessKeyId(),
//                            application.getOssAccessToken().getAccessKeySecret(),
//                            application.getOssAccessToken().getSecurityToken(),
//                            OssOption.DEFAULT_BUCKET,
//                            cutUrl + (style == null ? "" : "@" + style),
//                            true);
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
            ossFileUtils.getPicturesUrl(fileId, style);
            return "redirect:" + ossFileUtils.getPicturesUrl(fileId, style);
        }
        return "";
    }

    //================================文件图片上下传==============================================
}
