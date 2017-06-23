package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.serverfile.model.File;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.AppVersion;
import com.geetion.puputuan.service.AppVersionService;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.AppVersionManageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/3/31.
 */
@Controller
public class AppVersionManageControllerImpl extends BaseController implements AppVersionManageController {
    @Resource
    private AppVersionService appVersionService;

    @Resource
    private OssFileUtils ossFileUtils;


    @Override
    public Object searchAppVersion(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute AppVersion object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<AppVersion> list = appVersionService.getAppVersionList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        AppVersion obj = appVersionService.getAppVersionById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<AppVersion> pagingForKeyword = appVersionService.getAppVersionPage(pageEntity);
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
    public Object addAppVersion(@RequestParam(value = "file", required = false) CommonsMultipartFile file, @ModelAttribute AppVersion appVersion) {
        if (null != file){
            File resultFile = ossFileUtils.uploadMengerFile(file, 0L,"appVersion");
            if(null != resultFile){
                appVersion.setUrl(resultFile.getUrl());
            }else {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }
        if(checkParaNULL(appVersion)){
            boolean result;
            result = appVersionService.addAppVersion(appVersion);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object deleteAppVersion(Long[] ids) {
        if (checkParaNULL(ids)) {
            for (Long id: ids) {
                AppVersion appVersion = appVersionService.getAppVersionById(id);
                if (checkParaNULL(appVersion.getUrl())){
                    String url = appVersion.getUrl();
                    String fileName = appVersion.getUrl().substring(url.lastIndexOf("/") + 1, url.length());
                    ossFileUtils.deleteFile("appVersion",fileName);

                }
                appVersionService.removeAppVersion(id);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
}
