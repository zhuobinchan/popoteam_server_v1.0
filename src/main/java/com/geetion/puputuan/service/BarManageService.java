package com.geetion.puputuan.service;

/**
 * Created by chenzhuobin on 2017/4/12.
 */
public interface BarManageService {
    boolean updateBarStatus(Long barId,Integer status);
    boolean updateGroupBarId(Long barId,Long otherBarId);

    void chgActivityIsExpire(Long barId,Integer isExpire);

    void romoveActivity(Long barId);

    void dissolutionGroupByBarId(Long barId);
}
