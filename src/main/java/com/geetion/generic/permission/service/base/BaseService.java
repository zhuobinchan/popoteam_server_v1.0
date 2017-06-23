package com.geetion.generic.permission.service.base;

import com.geetion.generic.permission.utils.mybatis.PageEntity;
import com.geetion.generic.permission.utils.mybatis.PagingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by virgilyan on 12/10/14.
 */
public interface BaseService<T> {
    /**
     * 添加
     *
     * @param object
     * @return
     */
    public boolean add(T object);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public boolean remove(long id);

    /**
     * 修改
     *
     * @param object
     * @return id
     */
    public boolean update(T object);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public T getById(long id);

    /**
     * 查询全部
     *
     * @return
     */
    public List<T> getAll();

    /**
     * 分页查询全部数据
     *
     * @param pageEntity
     */
    public PagingResult<T> getByPagination(PageEntity pageEntity);

    /**
     * 条件查询
     *
     * @param params
     */
    public List<T> getByParam(Map<String, Object> params);

}
