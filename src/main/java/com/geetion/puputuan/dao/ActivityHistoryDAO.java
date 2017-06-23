package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.ActivityHistory;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ActivityDetailWithCount;
import com.geetion.puputuan.pojo.ActivityStatisData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityHistoryDAO extends BaseDAO<ActivityHistory, Long> {

    /**
     *  将对应的list中约会信息，插入到history表中
     * @param list
     * @return
     */
    int insertIntoFromActivity(List<Activity> list);
}