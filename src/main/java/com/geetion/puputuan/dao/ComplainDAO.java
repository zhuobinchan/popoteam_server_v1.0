package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Complain;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ComplainWithPhotoAndUser;
import com.geetion.puputuan.pojo.UserWithCount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ComplainDAO extends BaseDAO<Complain, Long> {

    /**
     * 查询被投诉的相片的发布者
     *
     * @param param
     * @return
     */
    public List<ComplainWithPhotoAndUser> selecParamtWithPhotoUser(Map param);

    /**
     * 查询投诉相册的用户列表
     *
     * @param param
     * @return
     */
    public List<Complain> selectParamWithComplainUser(Map param);
}