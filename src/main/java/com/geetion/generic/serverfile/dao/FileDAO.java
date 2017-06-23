package com.geetion.generic.serverfile.dao;

import com.geetion.generic.serverfile.dao.base.BaseDAO;
import com.geetion.generic.serverfile.model.File;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("geetionFileDAO")
public interface FileDAO extends BaseDAO<File,Long> {

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<File> list);


    /**
     * 批量查询
     *
     * @param fileIds
     * @return
     */
    public List<File> selectBatch(Long[] fileIds);

    /**
     * 根据url批量查询
     * @param urls
     * @return
     */
    List<File> selectBatchByUrls(List<String> urls);
}