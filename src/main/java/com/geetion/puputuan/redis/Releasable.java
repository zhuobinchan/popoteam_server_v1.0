package com.geetion.puputuan.redis;

/**
 * Created by guodikai on 2017/1/6.
 */
public interface Releasable {
    /**
     * 释放持有的所有资源
     */
    void release();
}
