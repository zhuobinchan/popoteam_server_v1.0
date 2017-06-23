package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Admin;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin getAdminById(Long id);

    Admin getAdmin(Map param);

    List<Admin> getAdminList(Map param);

    PagingResult<Admin> getAdminPage(PageEntity pageEntity);

    boolean addAdmin(Admin object);

    boolean updateAdmin(Admin object);

    boolean removeAdmin(Long id);


    /**
     * 根据用户id查询管理员信息
     *
     * @param userId
     * @return
     */
    public Admin getAdminByUserId(Long userId);

}
