package com.geetion.generic.userbase.service.impl;

import com.geetion.generic.userbase.dao.UserBaseDAO;
import com.geetion.generic.userbase.pojo.UserBase;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.generic.userbase.utils.PasswordHelper;
import com.geetion.generic.userbase.utils.mybatis.PageEntity;
import com.geetion.generic.userbase.utils.mybatis.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2015/6/17.
 */
@Service("geetionUserBaseService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class UserBaseServiceImpl implements UserBaseService {


    @Resource(name = "geetionUserBaseDAO")
    private UserBaseDAO userBaseDAO;

    @Resource(name = "geetionUserBasePasswordHelper")
    private PasswordHelper userBasePasswordHelper;

    @Override
    public boolean add(UserBase object) throws Exception {
        if (object != null && object.getPassword() != null) {
            //密码加密
            object.setPassword(userBasePasswordHelper.encryptPassword(object));
        }
        return userBaseDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return userBaseDAO.delete(id) > 0;
    }

    @Override
    public boolean update(UserBase object) {
        if (object.getPassword() != null) {
            object.setPassword(userBasePasswordHelper.encryptPassword(object));
        }
        return userBaseDAO.update(object) > 0;
    }

    @Override
    public boolean updateWithoutPassword(UserBase object) {
        //不更新密码
        return userBaseDAO.updateWithoutPassword(object) > 0;
    }

    @Override
    public boolean updatePassword(String account, String password) {
        //更新密前先加密
        UserBase userBase = new UserBase();
        userBase.setAccount(account);
        userBase.setPassword(password);
        userBase.setPassword(userBasePasswordHelper.encryptPassword(userBase));
//        userBase.setPassword(new SimpleHash("md5", userBase.getPassword(), ByteSource.Util.bytes(userBase.getAccount()), 2).toHex());
        return userBaseDAO.updatePassword(userBase.getAccount(), userBase.getPassword()) > 0;
        //前端app使用md5加密后再发送密码,所以这里密码不需要再另外加密了
//        return userBaseDAO.updatePassword(account,password) > 0;
    }

    @Override
    public UserBase getById(Long id) {
        return userBaseDAO.selectPk(id);
    }

    @Override
    public UserBase getByAccount(String account) {
        return userBaseDAO.selectByAccount(account);
    }

    @Override
    public UserBase getByToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        List<UserBase> list = userBaseDAO.selectParam(params);
        return (list != null && list.size() != 0) ? list.get(0) : null;
    }

    @Override
    public List<UserBase> getAll() {
        return userBaseDAO.select();
    }

    @Override
    public PagingResult<UserBase> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<UserBase> list = userBaseDAO.selectParam(pageEntity.getParams());
        PageInfo<UserBase> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<UserBase> getByParam(Map<String, Object> params) {
        return userBaseDAO.selectParam(params);
    }

    @Override
    public boolean deleteUserBaseBatch(Long[] ids) {
        return userBaseDAO.deleteBatch(ids) > 0;
    }

    @Override
    public boolean freezeUserBaseBatch(List<Long> ids, boolean freeze) {
        return userBaseDAO.freezeBatch(ids, freeze, null) == ids.size();
    }

    @Override
    public boolean freezeUserBaseBatch(List<Long> ids, boolean freeze, String token) {
        return userBaseDAO.freezeBatch(ids, freeze, token) == ids.size();
    }

    @Override
    public boolean checkOldPassword(String account, String password) {
        UserBase userBase = new UserBase();
        userBase.setAccount(account);
        userBase.setPassword(password);
        //得到加密后的密码
        String oldPassword = userBasePasswordHelper.encryptPassword(userBase);

        UserBase ub = userBaseDAO.selectByAccount(account);

        if (null != ub){
            if(ub.getPassword().equals(oldPassword)){
                return true;
            }
        }else{
            return false;
        }
        return false;
    }
}

