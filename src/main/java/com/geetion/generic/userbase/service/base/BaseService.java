package com.geetion.generic.userbase.service.base;

import com.geetion.generic.userbase.utils.mybatis.PageEntity;
import com.geetion.generic.userbase.utils.mybatis.PagingResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by virgilyan on 12/19/14.
 */
public abstract class BaseService<T> implements BaseServiceInterface<T> {

    @Override
    public boolean add(T object) {
        return false;
    }

    @Override
    public boolean remove(long id) {
        return false;
    }

    @Override
    public boolean update(T object) {
        return false;
    }

    @Override
    public T getById(long id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public PagingResult<T> getByPagination(PageEntity pageEntity) {
        return null;
    }

    @Override
    public List<T> getByParam(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<T> getByCreateDate(Date create_date) {
        return null;
    }
}
