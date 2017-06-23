package com.geetion.puputuan.common.mybatis;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by virgilyan on 12/16/14.
 */
public class PageEntity {
    private Integer page; //目前是第几页
    private Integer size; //每页大小
    @JSONField(serialize = false)
    private Map param; //传入的参数
    @JSONField(serialize = false)
    private String orderColumn;
    @JSONField(serialize = false)
    private String orderTurn = "ASC";

    public PageEntity(){
        init();
    }

    public void init(){
        this.page = 1;
        this.size = 15;
        param = new HashMap();
    }

    public String getOrderColumn() {
        return this.orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderTurn() {
        return this.orderTurn;
    }

    public void setOrderTurn(String orderTurn) {
        this.orderTurn = orderTurn;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Map getParam() {
        return param;
    }

    public void setParam(Map param) {
        this.param = param;
    }
}
