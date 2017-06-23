package com.geetion.generic.sms.utils.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virgilyan on 12/16/14.
 */
public class PagingResult<T> {
    //当前页
    private int currentPage;
    //总共记录条数
    private long totalSize;
    //总共页数
    private int totalPage;
    //结果集
    private List<T> resultList = new ArrayList<T>();

    public PagingResult(){}

    public PagingResult(int currentPage, long totalSize, int totalPage, List<T> resultList){
        this.currentPage = currentPage;
        this.totalSize = totalSize;
        this.totalPage = totalPage;
        this.resultList = resultList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
