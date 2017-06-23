package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.oss.FileManager;
import com.geetion.generic.serverfile.oss.OssOption;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.service.AdvertisementService;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.AdvMengerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/3/22.
 */
@Controller
public class AdvMengerControllerImpl extends BaseController implements AdvMengerController {

    @Resource
    private AdvertisementService advertisementService;

    @Resource
    private OssFileUtils ossFileUtils;

    @Deprecated
    @Override
    public Object searchAdvertisement(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute Advertisement object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Advertisement> list = advertisementService.getAdvertisementList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Advertisement obj = advertisementService.getAdvertisementById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Advertisement> pagingForKeyword = advertisementService.getAdvertisementPage(pageEntity);
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

    @Deprecated
    @Override
    public Object deleteAdvertisement(Long[] ids) {
        if (checkParaNULL(ids)) {
            for (Long id: ids) {
                Advertisement advertisement = advertisementService.getAdvertisementById(id);
                if (checkParaNULL(advertisement.getImageUrl())){
                    String imageUrl = advertisement.getImageUrl();
                    String fileName = advertisement.getImageUrl().substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                    ossFileUtils.deleteFile("advertisement",fileName);

                }
                advertisementService.removeAdvertisement(id);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    @Override
    public Object updateAdvertisement(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@ModelAttribute Advertisement advertisement) {
        if (checkParaNULL(advertisement)) {
            if (null != file){
                File resultFile = ossFileUtils.uploadMengerFile(file, 0L,"advertisement");
                if(null != resultFile){
                    Advertisement index = advertisementService.getAdvertisementById(advertisement.getId());//获取旧的对象
                    if (checkParaNULL(index.getImageUrl())){
                        String imageUrl = index.getImageUrl();
                        String fileName = index.getImageUrl().substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                        ossFileUtils.deleteFile("advertisement",fileName);//更新图片，并删除旧的图片
                    }
                    advertisement.setImageUrl(resultFile.getUrl());
                }else {
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }
            }
            boolean result = advertisementService.updateAdvertisement(advertisement);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


    @Override
    public Object addAdvertisement(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@ModelAttribute Advertisement advertisement) {
        if (null != file){
            File resultFile = ossFileUtils.uploadMengerFile(file, 0L,"advertisement");
            if(null != resultFile){
                advertisement.setImageUrl(resultFile.getUrl());
            }else {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }
        if(checkParaNULL(advertisement)){
            boolean result;
            result = advertisementService.addAdvertisement(advertisement);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object setAdvertisementsStatus(Long[] ids, int status) {
        if (checkParaNULL(ids)) {
            for (Long id: ids) {
                Advertisement advertisement = advertisementService.getAdvertisementById(id);
                advertisement.setStatus(status);
                advertisementService.updateAdvertisement(advertisement);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
}
