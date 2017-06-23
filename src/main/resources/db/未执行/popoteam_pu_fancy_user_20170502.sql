CREATE TABLE pu_fancy_user
(
    id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
    user_id BIGINT(20) NOT NULL COMMENT '用户id',
    fancy_id BIGINT(20) NOT NULL COMMENT '喜爱夜蒲id',
    create_time DATETIME DEFAULT 'CURRENT_TIMESTAMP' NOT NULL COMMENT '创建时间',
    CONSTRAINT pu_fancy_user_geetion_user_base_id_fk FOREIGN KEY (user_id) REFERENCES geetion_user_base (id)
);
CREATE INDEX pu_fancy_user_geetion_user_base_id_fk ON pu_fancy_user (user_id);