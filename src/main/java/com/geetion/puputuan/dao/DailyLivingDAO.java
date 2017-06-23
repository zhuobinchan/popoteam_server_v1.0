package com.geetion.puputuan.dao;

import com.geetion.puputuan.pojo.DailyLivingStatisData;
import com.geetion.puputuan.dao.base.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/3/28.
 */
@Repository
public interface DailyLivingDAO extends BaseDAO<DailyLivingStatisData,Long> {
    List<DailyLivingStatisData>  selectDailyLivingSum(Map map);
}
