--pu_activity

ALTER TABLE popoteam.pu_activity MODIFY name TEXT NOT NULL COMMENT ''活动名称'';

--修改字段male_group_id 为 group_a_id、male_group_id 为 group_b_id
DROP INDEX fk_activity_ref_female_group_idx ON popoteam.pu_activity;
DROP INDEX pu_activity_male_group_id_female_group_id_uindex ON popoteam.pu_activity;
ALTER TABLE popoteam.pu_activity DROP FOREIGN KEY fk_activity_ref_female_group;
DROP INDEX pu_activity_male_group_id_female_group_id_uindex ON popoteam.pu_activity;
DROP INDEX IDX_FEMALE_GROUP_ID_MALE_GROUP_ID ON popoteam.pu_activity;
ALTER TABLE popoteam.pu_activity CHANGE female_group_id group_b_id BIGINT(20) COMMENT '女性群id';
CREATE INDEX fk_activity_ref_female_group_idx ON popoteam.pu_activity (group_b_id);
DROP INDEX fk_activity_ref_male_group_idx ON popoteam.pu_activity;
ALTER TABLE popoteam.pu_activity DROP FOREIGN KEY fk_activity_ref_male_group;
ALTER TABLE popoteam.pu_activity CHANGE male_group_id group_a_id BIGINT(20) COMMENT '男性群id';
CREATE INDEX fk_activity_ref_male_group_idx ON popoteam.pu_activity (group_a_id);
CREATE INDEX IDX_FEMALE_GROUP_ID_MALE_GROUP_ID ON popoteam.pu_activity (group_b_id, group_a_id);
CREATE UNIQUE INDEX pu_activity_male_group_id_female_group_id_uindex ON popoteam.pu_activity (group_a_id, group_b_id);
ALTER TABLE popoteam.pu_activity
ADD CONSTRAINT fk_activity_ref_female_group
FOREIGN KEY (group_b_id) REFERENCES pu_group (id);
ALTER TABLE popoteam.pu_activity
ADD CONSTRAINT fk_activity_ref_male_group
FOREIGN KEY (group_a_id) REFERENCES pu_group (id);
ALTER TABLE popoteam.pu_activity ADD CONSTRAINT pu_activity_male_group_id_female_group_id_uindex UNIQUE (group_a_id, group_b_id);


--pu_activity_history
ALTER TABLE popoteam.pu_activity_history MODIFY name TEXT NOT NULL COMMENT ''活动名称'';

ALTER TABLE pu_activity_history CHANGE male_group_id group_a_id BIGINT(20) COMMENT '群组a id';
ALTER TABLE pu_activity_history CHANGE female_group_id group_b_id BIGINT(20) COMMENT '群组b id';