package com.geetion.puputuan.supervene.recommend.model;

import com.geetion.puputuan.model.base.BaseModel;

/**
 * Created by mac on 16/4/1.
 */
public class MainTeam extends BaseModel {
    private long mainID; //主队id
    private long areaID; //区域id
    private long cityID; //城市id
    private int type; //男女类别

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAreaID() {
        return areaID;
    }

    public void setAreaID(long areaID) {
        this.areaID = areaID;
    }

    public long getMainID() {
        return mainID;
    }

    public void setMainID(long mainID) {
        this.mainID = mainID;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }
}
