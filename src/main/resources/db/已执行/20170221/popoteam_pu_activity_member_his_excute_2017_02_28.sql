
DROP TABLE IF EXISTS  `pu_activity_member_his`;
CREATE TABLE `pu_activity_member_his` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动表id',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='约会成员历史表';


