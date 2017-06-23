package com.geetion.generic.sms.service.base;


import com.geetion.generic.sms.utils.mybatis.PageEntity;
import com.geetion.generic.sms.utils.mybatis.PagingResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by virgilyan on 12/10/14.
 */
public interface BaseServiceInterface<T> {
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

    /**
     * 根据创建时间查询
     *
     * @param create_date
     * @return
     */
    public List<T> getByCreateDate(Date create_date);
}
