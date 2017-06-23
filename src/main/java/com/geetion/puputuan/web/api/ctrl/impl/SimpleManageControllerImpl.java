package com.geetion.puputuan.web.api.ctrl.impl;

import com.alibaba.fastjson.JSON;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Advertisement;
import com.geetion.puputuan.model.Bar;
import com.geetion.puputuan.service.SimpleManageService;
import com.geetion.puputuan.utils.BeanMapUtil;
import com.geetion.puputuan.utils.MapRemoveNullUtil;
import com.geetion.puputuan.utils.ResultUtils;
import com.geetion.puputuan.web.api.base.BaseController;
import com.geetion.puputuan.web.api.ctrl.SimpleManageController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/5/3.
 */
@Controller
public class SimpleManageControllerImpl extends BaseController implements SimpleManageController {
    @Resource
    private SimpleManageService simpleManageService;

    @Override
    public Object search(Integer methodType, Long id, String daoKey, String paramsJson, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(daoKey, methodType)) {

            Map<String, String> searchParams = new HashMap<>();
            if (checkParaNULL(paramsJson)) {
                searchParams = jsonToMap(paramsJson);
                MapRemoveNullUtil.removeNullEntry(searchParams);
            }
            Map<String, Object> resultMap = new HashMap<>();

            simpleManageService.initObjectDAO(BeanMapUtil.getDaoSimpleName(daoKey));
            switch (methodType) {
                case 1:
                    List<Object> list = simpleManageService.getObjectList(searchParams);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Object obj = simpleManageService.getObjectById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    pageEntity.setParam(searchParams);
                    PagingResult<Object> pagingForKeyword = simpleManageService.getObjectPage(pageEntity);
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
    public Object complexSearch(Integer methodType, Long id,String daoKey, String methodKey, String paramsJson, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(daoKey, methodKey)) {
            Map<String, String> searchParams = new HashMap<>();
            if (checkParaNULL(paramsJson)) {
                searchParams = jsonToMap(paramsJson);
                MapRemoveNullUtil.removeNullEntry(searchParams);
            }

            Map<String, Object> resultMap = new HashMap<>();

            simpleManageService.initObjectDAO(BeanMapUtil.getDaoSimpleName(daoKey));
            switch (methodType) {
                case 1:
                    List<Object> list = simpleManageService.invokeSearchMethod(methodKey,searchParams);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Object obj = simpleManageService.getObjectById(id);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    pageEntity.setParam(searchParams);
                    PagingResult<Object> pagingForKeyword = simpleManageService.invokeSearchMethodPage(methodKey,pageEntity);
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
    public Object setStatus(String daoKey,Long[] ids, Integer status) {
        if (checkParaNULL(ids,status)){
            simpleManageService.initObjectDAO(BeanMapUtil.getDaoSimpleName(daoKey));
            for (Long id: ids) {
                simpleManageService.setObjectStatus(id,status);
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    /**
     * 通过传进来的转成Map，成功返回true，失败为false
     * @param json
     * @return
     */
    private Map jsonToMap(String json){
        try {
            return JSON.parseObject(json,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
