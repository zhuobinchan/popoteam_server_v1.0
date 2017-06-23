package com.geetion.puputuan.engine;

import java.util.Date;

/**
 * Created by guodikai on 2017/1/9.
 */
public interface MatchProducer {
    void match(Long groupId, Long userId, Date time);
    void reMatch(Long groupId, Long userId, Date time);
}
