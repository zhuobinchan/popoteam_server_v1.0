package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.AdminLoginHis;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdminLoginHisDAO extends BaseDAO<AdminLoginHis, Long> {

    List<AdminLoginHis> selectAdminLoginHisByParam(Map param);

    int deleteBatch(Map param);
}