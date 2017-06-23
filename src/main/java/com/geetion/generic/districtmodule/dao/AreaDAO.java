package com.geetion.generic.districtmodule.dao;

import com.geetion.generic.districtmodule.pojo.District;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("geetionDisAreaDAO")
public interface AreaDAO {
    int delete(Long id);

    int insert(District record);

    District selectById(Long id);

    List<District> selectParam(Map param);

    int update(District record);
}