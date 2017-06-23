package com.geetion.generic.serverfile.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by virgilyan on 12/16/14.
 */

public interface BaseDAO<T,PK extends Serializable> {
    /**
     * 新增实体
     * @param entity
     * @return 影响记录条数
     */
    int insert(T entity);

    /**
     * 修改一个实体对象（UPDATE一条记录）
     * @param entity 实体对象
     * @return 修改的对象个数，正常情况=1
     */
    int update(T entity);

    /**
     * 按主键删除记录
     * @param primaryKey 主键对象
     * @return 删除的对象个数，正常情况=1
     */
    int delete(PK primaryKey);

    /**
     * 查询整表总记录数
     * @return 整表总记录数
     */
    int count(Map param);

    /**
     * 按主键取记录
     * @param primaryKey 主键值
     * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
     */
    T selectById(PK primaryKey);

    /**
     * 按条件查询记录
     * @param param 查询条件参数，包括WHERE条件、分页条件、排序条件
     * @return 符合条件记录的实体对象的List
     */
    List<T> selectParam(Map param);
}
