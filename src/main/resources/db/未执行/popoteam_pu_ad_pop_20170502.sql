CREATE TABLE pu_ad_pop
(
    id BIGINT(11) PRIMARY KEY NOT NULL,
    ad_id INT(11) COMMENT '对应广告/活动id',
    text VARCHAR(20) COMMENT '按钮描述',
    status INT(11),
    create_time DATETIME DEFAULT 'CURRENT_TIMESTAMP',
    CONSTRAINT pu_ad_pop_pu_advertisement_id_fk FOREIGN KEY (ad_id) REFERENCES pu_advertisement (id)
);
CREATE INDEX pu_ad_pop_pu_advertisement_id_fk ON pu_ad_pop (ad_id);