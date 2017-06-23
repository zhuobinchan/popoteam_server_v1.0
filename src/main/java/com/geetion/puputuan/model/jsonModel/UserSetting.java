package com.geetion.puputuan.model.jsonModel;

import com.geetion.puputuan.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Simon on 2016/8/3.
 */
@Table(name = "pu_setting")
public class UserSetting extends BaseModel {

    @Id
    @Column
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column
    private Long userId;

    @Column
    private Integer ntyBySound;

    @Column
    private Integer ntyByVibration;

    @Column
    private Integer isSpeakerOn;

    @Column
    private Integer showFriendsToStranger;

    @Column
    private Integer shieldPhoneOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNtyBySound() {
        return ntyBySound;
    }

    public void setNtyBySound(Integer ntyBySound) {
        this.ntyBySound = ntyBySound;
    }

    public Integer getNtyByVibration() {
        return ntyByVibration;
    }

    public void setNtyByVibration(Integer ntyByVibration) {
        this.ntyByVibration = ntyByVibration;
    }

    public Integer getIsSpeakerOn() {
        return isSpeakerOn;
    }

    public void setIsSpeakerOn(Integer isSpeakerOn) {
        this.isSpeakerOn = isSpeakerOn;
    }

    public Integer getShowFriendsToStranger() {
        return showFriendsToStranger;
    }

    public void setShowFriendsToStranger(Integer showFriendsToStranger) {
        this.showFriendsToStranger = showFriendsToStranger;
    }

    public Integer getShieldPhoneOn() {
        return shieldPhoneOn;
    }

    public void setShieldPhoneOn(Integer shieldPhoneOn) {
        this.shieldPhoneOn = shieldPhoneOn;
    }
}
