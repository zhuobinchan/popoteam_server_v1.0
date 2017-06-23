package com.geetion.puputuan.model.jsonModel;

import com.geetion.puputuan.model.base.BaseModel;

/**
 * Created by admin on 2016/7/29.
 */
public class ImUser extends BaseModel {

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "ImUser{" +
                "userName= " + userName +
                ", password= " + password +
                "}";
    }
}
