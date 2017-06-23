package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.AdminLoginHis;
import com.geetion.puputuan.pojo.GroupStatisData;

import java.util.List;
import java.util.Map;

public interface AdminLoginHisService {

    boolean addAdminLoginHis(AdminLoginHis object);

    List<AdminLoginHis> getAdminLoginHis(Map param);

    PagingResult<AdminLoginHis> getAdminLoginHisPage(PageEntity pageEntity);

    void deleteAdminLoginBatch(List<Long> userIdList);
}
