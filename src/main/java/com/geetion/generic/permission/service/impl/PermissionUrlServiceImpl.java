package com.geetion.generic.permission.service.impl;

import com.geetion.generic.permission.dao.PermissionUrlDAO;
import com.geetion.generic.permission.pojo.Permission;
import com.geetion.generic.permission.pojo.PermissionUrl;
import com.geetion.generic.permission.service.PermissionUrlService;
import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
@Service("geetionPermissionUrlService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class PermissionUrlServiceImpl implements PermissionUrlService {


    @Resource(name = "geetionPermissionUrlDAO")
    private PermissionUrlDAO permissionUrlDAO;

    @Override
    public boolean add(PermissionUrl object) {
//        int count = 0;
//        try{
//            count = geetionPermissionUrlDAO.insert(object);
//        }catch (SQLException e){
//            if("".indexOf(e.getMessage()) != 0){
//
//            }
//        }
        return permissionUrlDAO.insert(object) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return permissionUrlDAO.delete(id) > 0;
    }

    @Override
    public boolean update(PermissionUrl object) {
        return permissionUrlDAO.update(object) > 0;
    }

    @Override
    public PermissionUrl getById(Long id) {
        return permissionUrlDAO.selectPk(id);
    }

    @Override
    public List<PermissionUrl> getAll() {
        return permissionUrlDAO.select();
    }

    @Override
    public PagingResult<PermissionUrl> getByPagination(PageEntity pageEntity) {

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<PermissionUrl> list = permissionUrlDAO.selectParam(pageEntity.getParams());
        PageInfo<PermissionUrl> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<PermissionUrl> getByParam(Map<String, Object> params) {
        return permissionUrlDAO.selectParam(params);
    }


    @Override
    public List<PermissionUrl> getAllUrlNeedPermission() {
        return permissionUrlDAO.selectAllUrlNeedPermission();
    }



    @Override
    public Map<String, String> putUrlPermissionToMap() {

        //获取所有的 路径-权限 关系
        List<PermissionUrl> list = permissionUrlDAO.selectAllUrlNeedPermission();
        Map<String, String> urlPermissionMap = new HashMap<>();
        if (list != null && list.size() != 0) {

            //遍历，将url和permission组装成map
            for (PermissionUrl permissionUrl : list) {
                String key = permissionUrl.getUrl();
                String value = null;
                if(permissionUrl.getPermission() != null) {
                    value = permissionUrl.getPermission().getPermission();
                }else {
                    continue;
                }
                if (urlPermissionMap.get(permissionUrl.getUrl()) == null) {
                    urlPermissionMap.put(key, value);
                } else {
                    urlPermissionMap.put(key, urlPermissionMap.get(key) + "," + value);
                }
            }
        }
        return urlPermissionMap;
    }

    @Override
    public boolean addBatch(List<PermissionUrl> list) {
        return permissionUrlDAO.insertBatch(list) > 0;
    }

    @Override
    public List<PermissionUrl> getPermissionUrlsByPermissionList(List<Permission> list) {
        return permissionUrlDAO.selectUrlByPermissionList(list);
    }
}

