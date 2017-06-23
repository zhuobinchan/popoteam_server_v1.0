package com.geetion.puputuan.model.jsonModel;


import com.geetion.puputuan.model.User;

/**
 * Created by guodikai on 2016/9/18.
 */
public class JSONGroupMember {

    private JSONUser user;

    private boolean isLeader;

    private Integer status;

    public JSONUser getUser() {
        return user;
    }

    public void setUser(JSONUser user) {
        this.user = user;
    }

    public boolean isIsLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean leader) {
        isLeader = leader;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
