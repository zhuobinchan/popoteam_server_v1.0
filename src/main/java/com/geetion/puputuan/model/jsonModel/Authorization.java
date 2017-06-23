package com.geetion.puputuan.model.jsonModel;

import com.geetion.puputuan.model.base.BaseModel;

/**
 * Created by admin on 2016/7/29.
 */
public class Authorization extends BaseModel {

    private String account;

    private String token;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
