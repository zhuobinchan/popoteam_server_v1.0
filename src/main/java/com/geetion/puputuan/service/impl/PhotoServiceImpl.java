package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.PhotoDAO;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.pojo.PhotoWithLikeCount;
import com.geetion.puputuan.service.PhotoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class PhotoServiceImpl implements PhotoService {

    @Resource
    private PhotoDAO photoDAO;

    @Override
    public Photo getPhotoById(Long id) {
        return photoDAO.selectByPrimaryKey(id);
    }

    @Override
    public Photo getPhoto(Map param) {
        return photoDAO.selectOne(param);
    }

    @Override
    public List<Photo> getPhotoList(Map param) {
        return photoDAO.selectParam(param);
    }

    @Override
    public PagingResult<Photo> getPhotoPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Photo> list = getPhotoList(pageEntity.getParam());
        PageInfo<Photo> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addPhoto(Photo object) {
        return photoDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updatePhoto(Photo object) {
        return photoDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removePhoto(Long id) {
        return photoDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public List<PhotoWithLikeCount> getPhotoWithLikeCountList(Map param) {
        return photoDAO.selectParamWithLikeCount(param);
    }

    @Override
    public PagingResult<PhotoWithLikeCount> getPhotoWithLikeCountPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<PhotoWithLikeCount> list = getPhotoWithLikeCountList(pageEntity.getParam());
        PageInfo<PhotoWithLikeCount> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }
}
