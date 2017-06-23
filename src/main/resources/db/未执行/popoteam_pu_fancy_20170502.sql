CREATE TABLE pu_fancy
(
    id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
    name TEXT NOT NULL COMMENT '喜爱夜蒲类型',
    identify BIGINT(20) NOT NULL COMMENT '标识ID',
    type INT(11) DEFAULT '0' COMMENT '系统自建还是用户自建',
    status INT(11) DEFAULT '0' COMMENT '是否屏蔽',
    create_time DATETIME DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建时间'
);