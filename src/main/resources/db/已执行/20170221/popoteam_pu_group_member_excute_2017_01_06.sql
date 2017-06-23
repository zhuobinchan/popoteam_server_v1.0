CREATE UNIQUE INDEX pu_group_member_user_status_uindex ON popoteam.pu_group_member (user_id, status);

ALTER TABLE popoteam.pu_group_member DROP INDEX uq_group_member_ref_group_user;
