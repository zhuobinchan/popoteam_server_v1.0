package com.geetion.puputuan.pojo;

import com.geetion.generic.serverfile.model.File;
import com.geetion.puputuan.model.Group;

import java.util.List;

public class GroupWithNumberList extends Group {


    //当前人数
    private Integer currentMember;

    //历史人数
    private Integer historyMember;

    //当前成员头像
    private List<File> headList;

    //签到人数
    private Integer signMember;


    public Integer getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(Integer currentMember) {
        this.currentMember = currentMember;
    }

    public Integer getHistoryMember() {
        return historyMember;
    }

    public void setHistoryMember(Integer historyMember) {
        this.historyMember = historyMember;
    }

    public List<File> getHeadList() {
        return headList;
    }

    public void setHeadList(List<File> headList) {
        this.headList = headList;
    }

    public Integer getSignMember() {
        return signMember;
    }

    public void setSignMember(Integer signMember) {
        this.signMember = signMember;
    }
}