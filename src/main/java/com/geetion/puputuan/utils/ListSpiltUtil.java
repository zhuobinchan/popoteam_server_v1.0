package com.geetion.puputuan.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhuobin on 2017/4/14.
 */
public class ListSpiltUtil {
    final static private Integer listLength = 200;

    public static List<List<Long>> listSpilt(List<Long> list){
        if (null!=list && list.size()>0){
            List<List<Long>> resultList = new ArrayList<>();
            Integer index = 0;
            while (true){
                if ((index+listLength-1)<list.size()) {
                    resultList.add(list.subList(index, index + listLength));
                    index+=listLength;
                }else {
                    if (index<=list.size()-1){
                        resultList.add(list.subList(index,list.size()));
                    }
                    break;
                }

            }
            return resultList;
        }
        return null;
    }
}
