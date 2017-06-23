package com.geetion.generic.serverfile.service;

import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.utils.mybatis.PageEntity;
import com.geetion.generic.serverfile.utils.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by jian on 2016/1/4.
 */
public interface FileService {

    /**
     * 添加
     *
     * @param object
     * @return
     */
    public boolean addFile(File object);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    boolean addFileBatch(List<File> list);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public boolean removeFile(Long id);

    /**
     * 修改
     *
     * @param object
     * @return id
     */
    public boolean updateFile(File object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public File getFileById(Long id);

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     */
    public PagingResult<File> getFileByPagination(PageEntity pageEntity);

    /**
     * 条件查询
     *
     * @param params
     */
    public List<File> getFileByParam(Map<String, Object> params);


    /**
     * 根据id批量查询文件
     *
     * @param fileIds
     */
    public List<File> getFileBatch(Long[] fileIds);

    /**
     * 根据url批量获取文件
     * @param urls
     * @return
     */
    List<File> getFileBatchByUrl(List<String> urls);


}
