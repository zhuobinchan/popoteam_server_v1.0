package com.geetion.puputuan.engine.handlernew;

import java.util.Date;

/**
 * Created by guodikai on 2017/1/9.
 */
public interface Matcher {

    /**
     * 匹配队伍
     */
    boolean matchGroup(Long groupId, Long userId, Date time);

    /**
     * 执行下一个处理者
     * @param groupId
     * @return
     */
    boolean nextMatchGroup(Long groupId, Long userId, Date time);

}
