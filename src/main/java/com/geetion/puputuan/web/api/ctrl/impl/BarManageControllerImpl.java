package com.geetion.puputuan.web.api.ctrl.impl;

import com.geetion.generic.serverfile.model.File;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.service.BarManageService;
import com.geetion.puputuan.service.BarService;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.BarManageController;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */
@Controller
public class BarManageControllerImpl extends BaseController implements BarManageController {
    @Resource
    private BarService barService;

    @Resource
    private OssFileUtils ossFileUtils;

    @Resource
    private BarManageService barManageService;

    @Override
    public Object searchBar(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute Bar object) {
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));

            switch (methodType) {
                case 1:
                    List<Bar> list = barService.getBarList(param);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Bar obj = barService.getBarById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null)
                        pageEntity.setParam(param);
                    PagingResult<Bar> pagingForKeyword = barService.getBarPage(pageEntity);
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


    /**
     * 更新时需要注意的是两个图片的后缀名必须一致
     * @param file
     * @param smallFile
     * @param bar
     * @return
     */
    @Deprecated
    @Override
    public Object updateBar(@RequestParam(value = "file", required = false) CommonsMultipartFile file,@RequestParam(value = "smallFile",required = false) CommonsMultipartFile smallFile, @ModelAttribute Bar bar) {
        if (checkParaNULL(bar)) {
            if (null != file){
                File resultFile = ossFileUtils.uploadMengerFile(file, 0L,"bar");
                if(null != resultFile){
                    Bar index = barService.getBarById(bar.getId());//获取旧的对象
                    if (checkParaNULL(index.getUrl())){
                        String imageUrl = index.getUrl();
                        String fileName = index.getUrl().substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                        ossFileUtils.deleteFile("bar",fileName);//更新图片，并删除旧的图片

                        if (null!=smallFile){
                            String smallFileName = resultFile.getUrl().substring(resultFile.getUrl().lastIndexOf("/")+1,resultFile.getUrl().lastIndexOf("."))
                                    +"_A"+smallFile.getFileItem().getName().substring(smallFile.getFileItem().getName().lastIndexOf("."));
                            ossFileUtils.uploadMengerFile(smallFile,0L,"bar",smallFileName);
                            String deleteOldSmallFileName = fileName.substring(0,fileName.lastIndexOf("."))+"_A"+fileName.substring(fileName.lastIndexOf("."));
                            ossFileUtils.deleteFile("bar",deleteOldSmallFileName);
                        }

                    }
                    bar.setUrl(resultFile.getUrl());
                }else {
                    return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                }
            }
            boolean result = barService.updateBar(bar);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object  addBar(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@RequestParam(value = "smallFile",required = false) CommonsMultipartFile smallFile, @ModelAttribute Bar bar) {
        if (null != file){
            File resultFile = ossFileUtils.uploadMengerFile(file, 0L,"bar");
            //长传缩略图
            if (null!=smallFile){
                String smallFileName = resultFile.getUrl().substring(resultFile.getUrl().lastIndexOf("/")+1,resultFile.getUrl().lastIndexOf("."))+"_A";
                ossFileUtils.uploadMengerFile(smallFile,0L,"bar",smallFileName);
            }

            if(null != resultFile){
                bar.setUrl(resultFile.getUrl());
            }else {
                return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
            }
        }
        if(checkParaNULL(bar)){
            boolean result;
            result = barService.addBar(bar);
            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
        }

        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object setBarsStatus(Long[] ids, int status) {
        if (checkParaNULL(ids)) {
            for (Long id: ids) {
                barManageService.updateBarStatus(id,status);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object setStatusScreen(Long[] ids, Integer status, Integer groupHandle, Integer activityHandle) {
        if (checkParaNULL(ids,status,groupHandle,activityHandle)){
            for (Long id : ids) {

            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object searchOtherBars(Long[] ids , Integer status) {
        if (checkParaNULL(ids,status)){
            Map<String, Object> resultMap = new HashedMap();
            Map<String, Object> param = new HashedMap();
            param.put("ids",ids);
            param.put("status",status);
            List<Bar> otherBars = barService.getOtherBars(param);
            resultMap.put("otherBars",otherBars);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object setStatusScreenAndGroupChgOtherBar(Long[] barIds, Long otherBarId,Integer activityDissolve) {
        if (checkParaNULL(barIds,otherBarId,activityDissolve)){
            for (Long barId : barIds){
                if (activityDissolve==1){//自动清理约会
                    barManageService.chgActivityIsExpire(barId,0);
                }
                if (activityDissolve==2){//马上清理约会
                    barManageService.romoveActivity(barId);
                }
                if (activityDissolve==3){//不处理约会

                }
                barManageService.updateGroupBarId(barId,otherBarId);
                barManageService.updateBarStatus(barId,0);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object setStatusScreenAndGroupWithActivityDissolve(Long[] barIds, Integer activityDissolve) throws Exception {
        if (checkParaNULL(barIds,activityDissolve)){
            for (Long barId: barIds) {
//            约会处理
            if (activityDissolve==1){//自动清理约会
                barManageService.chgActivityIsExpire(barId,0);
            }
            if (activityDissolve==2){//马上清理约会
                barManageService.romoveActivity(barId);
            }
            if (activityDissolve==3){//不处理约会

            }
                barManageService.dissolutionGroupByBarId(barId);//处理队伍
                barManageService.updateBarStatus(barId,0);//更新活动类型的状态
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }
}
