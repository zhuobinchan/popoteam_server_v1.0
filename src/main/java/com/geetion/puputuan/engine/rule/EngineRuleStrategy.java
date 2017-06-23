package com.geetion.puputuan.engine.rule;

import java.util.Date;

/**
 * 匹配引擎接口
 */
public interface EngineRuleStrategy {

   boolean matchGroup(Long groupId, Long userId, Date time);

   boolean rematchGroup(Long groupId, Long userId, Date time);
}
