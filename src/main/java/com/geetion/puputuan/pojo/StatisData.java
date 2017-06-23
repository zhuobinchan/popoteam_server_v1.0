package com.geetion.puputuan.pojo;

/**
 * Created by guodikai on 2016/11/11.
 */
public class StatisData {
    private String name;
    private Integer total;
    private String ratio;
    private Integer maleTotal;
    private Integer femaleTotal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public Integer getMaleTotal() {
        return maleTotal;
    }

    public void setMaleTotal(Integer maleTotal) {
        this.maleTotal = maleTotal;
    }

    public Integer getFemaleTotal() {
        return femaleTotal;
    }

    public void setFemaleTotal(Integer femaleTotal) {
        this.femaleTotal = femaleTotal;
    }
}
