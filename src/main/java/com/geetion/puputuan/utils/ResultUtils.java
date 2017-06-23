package com.geetion.puputuan.utils;

import com.geetion.puputuan.common.mybatis.PagingResult;

import java.util.Map;

/**
 * Created by guodikai on 2016/11/1.
 */
public class ResultUtils {

    public static void setResultMap(Map resultMap, PagingResult pagingResult){
        if(null != pagingResult){
            resultMap.put("list", pagingResult.getResultList());
            resultMap.put("totalPage", pagingResult.getTotalPage());
            resultMap.put("totalSize", pagingResult.getTotalSize());
            resultMap.put("currentPage", pagingResult.getCurrentPage());
        }
    }
}
