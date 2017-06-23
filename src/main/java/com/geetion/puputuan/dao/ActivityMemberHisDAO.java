package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.ActivityMember;
import com.geetion.puputuan.model.ActivityMemberHis;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityMemberHisDAO extends BaseDAO<ActivityMemberHis, Long> {

    /**
     *  将对应的list中约会信息，插入到history表中
     * @param list
     * @return
     */
    int insertIntoFromActivityMember(List<Activity> list);
}