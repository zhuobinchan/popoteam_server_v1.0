package com.geetion.puputuan.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by guodikai on 2016/12/19.
 */
@Component("configInfo")
public class ConfigInfo {

    @Value("${sms.check.open}")
    private boolean smsOpen;

    @Value("${oss.server.file}")
    private boolean isServer;

    @Value("${user.account.prex}")
    private String accountPrex;

    @Value("${server.domain}")
    private String serverDomain;



    public boolean isSmsOpen() {
        return smsOpen;
    }

    public void setSmsOpen(boolean smsOpen) {
        this.smsOpen = smsOpen;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public String getAccountPrex() {
        return accountPrex;
    }

    public void setAccountPrex(String accountPrex) {
        this.accountPrex = accountPrex;
    }

    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }
}
