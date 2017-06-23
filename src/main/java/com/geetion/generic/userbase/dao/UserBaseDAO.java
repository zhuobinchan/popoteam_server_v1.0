package com.geetion.generic.userbase.dao;

import com.geetion.generic.userbase.dao.base.BaseDAO;
import com.geetion.generic.userbase.pojo.UserBase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jian on 2015/6/17.
 */
@Repository("geetionUserBaseDAO")
public interface UserBaseDAO extends BaseDAO<UserBase, Long> {

    public UserBase selectByAccount(@Param("account") String account);

    public int updatePassword(@Param("account") String account, @Param("password") String password);

    public int updateWithoutPassword(UserBase userBase);

    public int deleteBatch(@Param("ids")Long[] ids);

    int freezeBatch(@Param("ids") List<Long> ids, @Param("freeze") boolean freeze, @Param("token") String token);

}