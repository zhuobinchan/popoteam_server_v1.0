package com.geetion.puputuan.pojo;

import com.geetion.puputuan.model.GroupMember;

public class GroupMemberWithSign extends GroupMember {


    //是否签到过
    private Boolean haveSign;

    public Boolean getHaveSign() {
        return haveSign;
    }

    public void setHaveSign(Boolean haveSign) {
        this.haveSign = haveSign;
    }
}