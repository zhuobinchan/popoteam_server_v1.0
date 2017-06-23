CREATE TABLE `pu_activity_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '活动名称',
  `male_group_id` bigint(20) DEFAULT NULL COMMENT '男性群id',
  `female_group_id` bigint(20) DEFAULT NULL COMMENT '女性群id',
  `room_id` varchar(50) DEFAULT NULL COMMENT '环信群聊id',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `super_like` int(11) DEFAULT '0',
  `expire_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '约会失效时间',
  `expire_type` int(11) DEFAULT '0' COMMENT '失效类型\n0：系统解散\n1：主动解散',
  `is_expire` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='活动历史表';