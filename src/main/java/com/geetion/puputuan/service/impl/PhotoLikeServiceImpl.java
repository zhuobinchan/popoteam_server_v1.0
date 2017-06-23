package com.geetion.puputuan.service.impl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.PhotoLikeDAO;
import com.geetion.puputuan.model.PhotoLike;
import com.geetion.puputuan.service.PhotoLikeService;
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

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class PhotoLikeServiceImpl implements PhotoLikeService {

    @Resource
    private PhotoLikeDAO photoLikeDAO;

    @Override
    public PhotoLike getPhotoLikeById(Long id) {
        return photoLikeDAO.selectByPrimaryKey(id);
    }

    @Override
    public PhotoLike getPhotoLike(Map param) {
        return photoLikeDAO.selectOne(param);
    }

    @Override
    public List<PhotoLike> getPhotoLikeList(Map param) {
        return photoLikeDAO.selectParam(param);
    }

    @Override
    public int countPhotoLikeByPhotoId(Long id) {
        return photoLikeDAO.countPhotoByPhotoId(id);
    }

    @Override
    public boolean userIsLikePhoto(Map param) {
        return photoLikeDAO.userIsLke(param) > 0;
    }

    @Override
    public PagingResult<PhotoLike> getPhotoLikePage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<PhotoLike> list = getPhotoLikeList(pageEntity.getParam());
        PageInfo<PhotoLike> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addPhotoLike(PhotoLike object) {
        return photoLikeDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updatePhotoLike(PhotoLike object) {
        return photoLikeDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean removePhotoLike(Long id) {
        return photoLikeDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public List<HashMap<String, Long>> getUserPhotoLikeIn(Map param) {
        return photoLikeDAO.selectUserPhotoLikeIn(param);
    }

    @Override
    public List<HashMap<String, Long>> getEachOtherPhotoLike(Map param) {
        return photoLikeDAO.selectEachOtherPhotoLike(param);
    }
}
