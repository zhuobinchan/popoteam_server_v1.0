package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/5/3.
 */
public interface SimpleManageService {

    /**
     * 初始化Dao信息，需要调用的Dao信息
     *
     * @param daoName
     */
    void initObjectDAO(String daoName);

    /**
     * 基础方法，通过id获取 对象
     * @param id
     * @return
     */
    Object getObjectById(Long id);

    /**
     * 基础方法，通过筛选参数 获取单个对象
     * @param param
     * @return
     */
    Object getObject(Map param);

    /**
     *基础方法， 通过筛选参数 获取对象列表 不分页
      * @param param
     * @return
     */
    List<Object> getObjectList(Map param);

    /**
     *通过筛选参数 获取对象列表 分页
     * @param pageEntity
     * @return
     */
    PagingResult<Object> getObjectPage(PageEntity pageEntity);

    /**
     *基础方法，添加对象
     * @param object
     * @return
     */
    boolean addObject(Object object);

    /**
     *基础方法，更新对象
     * @param object
     * @return
     */
    boolean updateObject(Object object);

    /**
     *基础方法，删除对象
     * @param id
     * @return
     */
    boolean removeObject(Long id);

    /**
     *动态调用方法
     * @param methodName 方法名
     * @param parameterTypes 方法参数类型
     * @param args 要调用的参数
     * @return
     */
    Object invokeMethodByName(String methodName, Class[] parameterTypes,Object... args);

    /**
     *动态调用，特殊的查询方法
     * @param methodName 方法名
     * @param searchMap 筛选的参数
     * @return
     */
    List<Object> invokeSearchMethod(String methodName,Map searchMap);

    /**
     *动态调用，特殊的查询方法  分页
     * @param methodName 方法名
     * @param pageEntity 分页工具类
     * @return
     */
    PagingResult<Object> invokeSearchMethodPage(String methodName,PageEntity pageEntity);

    /**
     *设置对象状态，简单的更新对象
     * @param id
     * @param status
     * @return
     */
    boolean setObjectStatus(Long id,Integer status);
}
