package com.geetion.generic.districtmodule.service;

import com.geetion.generic.districtmodule.pojo.District;

import java.util.List;
import java.util.Map;

/**
 * Created by yoan on 16/1/5.
 */
public interface DistrictService {

    /**
     * 根据参数查询省数据
     * 参数为:code, name, parent(省默认为空)
     *
     * @param param 参数
     * @return
     */
    List<District> getProvinceByParam(Map param);

    /**
     * 根据参数查询市数据
     * 参数为:code, name, parent
     *
     * @param param 参数
     * @return
     */
    List<District> getCityByParam(Map param);

    /**
     * 根据参数查询区数据
     * 参数为:code, name, parent
     *
     * @param param 参数
     * @return
     */
    List<District> getAreaByParam(Map param);

    /**
     * 根据类型查询数据
     * 1:省,2:市,3:区
     *
     * @param type 类型
     * @return
     */
    List<District> getDistrictByType(int type);

    /**
     * 根据类型和父级的code查询数据
     * 1:省,2:市,3:区
     *
     * @param type 类型
     * @param parentCode 父级code
     * @return
     */
    List<District> getDistrictByTypeAndParent(int type, Integer parentCode);

    /**
     * 根据父级code查询市
     * 用于联动
     *
     * @param parentCode
     * @return
     */
    List<District> getCityByParent(Integer parentCode);

    /**
     * 根据父级code查询区
     * 用于联动
     *
     * @param parentCode
     * @return
     */
    List<District> getAreaByParent(Integer parentCode);
}
