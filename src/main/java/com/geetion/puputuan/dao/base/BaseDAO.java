package com.geetion.puputuan.dao.base;

import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 只创建对表的增删改操作
 */
public interface BaseDAO<T, Pk extends Serializable> extends
        BaseInsertMapper<T>,
        BaseUpdateMapper<T>,
        BaseDeleteMapper<T>,
        MySqlMapper<T> {

        T selectByPrimaryKey(Pk id);

        List<T> selectParam(Map param);

        T selectOne(Map param);

}
