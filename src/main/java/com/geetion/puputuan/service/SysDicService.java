package com.geetion.puputuan.service;

import com.geetion.puputuan.model.SysDic;

public interface SysDicService {

    SysDic getSysDicByKey(String key);
    void updateSysDic(SysDic sysDic);
}
