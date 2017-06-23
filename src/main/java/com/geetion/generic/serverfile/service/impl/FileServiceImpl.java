package com.geetion.generic.serverfile.service.impl;

import com.geetion.generic.serverfile.dao.FileDAO;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.service.FileService;
import com.geetion.generic.serverfile.utils.mybatis.PageEntity;
import com.geetion.generic.serverfile.utils.mybatis.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2016/1/4.
 */
@Service("geetionFileService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class FileServiceImpl implements FileService {

    @Resource(name = "geetionFileDAO")
    private FileDAO fileDAO;

    @Override
    public boolean addFile(File object) {
        return fileDAO.insert(object) > 0;
    }

    @Override
    public boolean addFileBatch(List<File> list) {
        return fileDAO.insertBatch(list) == list.size();
    }

    @Override
    public boolean removeFile(Long id) {
        return fileDAO.delete(id) > 0;
    }

    @Override
    public boolean updateFile(File object) {
        return fileDAO.update(object) > 0;
    }

    @Override
    public File getFileById(Long id) {
        return fileDAO.selectById(id);
    }

    @Override
    public PagingResult<File> getFileByPagination(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<File> list = fileDAO.selectParam(pageEntity.getParams());
        PageInfo<File> pager = new PageInfo(list);
        return new PagingResult<>(pager.getPageNum(), pager.getTotal(), pager.getPages(), pager.getList());

    }

    @Override
    public List<File> getFileByParam(Map<String, Object> params) {
        return fileDAO.selectParam(params);
    }

    @Override
    public List<File> getFileBatch(Long[] fileIds) {
        return fileDAO.selectBatch(fileIds);
    }

    @Override
    public List<File> getFileBatchByUrl(List<String> urls) {
        return fileDAO.selectBatchByUrls(urls);
    }
}
