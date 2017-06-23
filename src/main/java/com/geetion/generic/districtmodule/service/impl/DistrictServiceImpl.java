package com.geetion.generic.districtmodule.service.impl;

import com.geetion.generic.districtmodule.consts.DistrictType;
import com.geetion.generic.districtmodule.dao.AreaDAO;
import com.geetion.generic.districtmodule.dao.CityDAO;
import com.geetion.generic.districtmodule.dao.ProvinceDAO;
import com.geetion.generic.districtmodule.pojo.District;
import com.geetion.generic.districtmodule.service.DistrictService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yoan on 16/1/5.
 */
@Service("geetionDistrictService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class DistrictServiceImpl implements DistrictService {

    @Resource(name = "geetionDisProvinceDAO")
    private ProvinceDAO provinceDAO;

    @Resource(name = "geetionDisCityDAO")
    private CityDAO cityDAO;

    @Resource(name = "geetionDisAreaDAO")
    private AreaDAO areaDAO;

    @Override
    public List<District> getProvinceByParam(Map param) {
        return provinceDAO.selectParam(param);
    }

    @Override
    public List<District> getCityByParam(Map param) {
        return cityDAO.selectParam(param);
    }

    @Override
    public List<District> getAreaByParam(Map param) {
        return areaDAO.selectParam(param);
    }

    @Override
    public List<District> getDistrictByType(int type) {
        return getDistrictByTypeAndParent(type, null);
    }

    @Override
    public List<District> getDistrictByTypeAndParent(int type, Integer parentCode) {
        switch (type){
            case DistrictType.PROVINCE:
                //省数据由于没有父级,因此返回所有省数据
                return getProvinceByParam(new HashMap());
            case DistrictType.CITY:
                return getCityByParent(parentCode);
            case DistrictType.AREA:
                return getAreaByParent(parentCode);
            default:
                throw new IllegalArgumentException("无效的地区类型");
        }
    }

    @Override
    public List<District> getCityByParent(Integer parentCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("parent", parentCode);
        return getCityByParam(param);
    }

    @Override
    public List<District> getAreaByParent(Integer parentCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("parent", parentCode);
        return getAreaByParam(param);
    }
}
