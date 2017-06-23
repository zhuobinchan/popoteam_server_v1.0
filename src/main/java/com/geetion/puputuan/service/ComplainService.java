package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Complain;
import com.geetion.puputuan.pojo.ComplainWithPhotoAndUser;

import java.util.List;
import java.util.Map;

public interface ComplainService {

    Complain getComplainById(Long id);

    Complain getComplain(Map param);

    List<Complain> getComplainList(Map param);

    PagingResult<Complain> getComplainPage(PageEntity pageEntity);

    boolean addComplain(Complain object);

    boolean updateComplain(Complain object);

    boolean removeComplain(Long id);

    /**
     * 查询被投诉的相片的发布者 -- 不分页
     * @param param
     * @return
     */
    public List<ComplainWithPhotoAndUser> getPhotoUserList(Map param);

    /**
     * 查询被投诉的相片的发布者 -- 分页
     * @param pageEntity
     * @return
     */
    public PagingResult<ComplainWithPhotoAndUser> getPhotoUserPage(PageEntity pageEntity);

    /**
     * 查询投诉相册的用户列表
     *
     * @param param
     * @return
     */
    public List<Complain> getComplainUserList(Map param);

}
