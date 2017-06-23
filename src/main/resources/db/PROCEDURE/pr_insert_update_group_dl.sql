CREATE PROCEDURE `pr_insert_update_group_dl`(
		IN `groupId` bigint
)
BEGIN
	#Routine body goes here...
	declare gi BIGINT(20);
	if groupId is not null then


		SELECT groupId INTO gi FROM pu_group_daily_living WHERE
		DATE_FORMAT(pu_group_daily_living.create_time,'%Y-%m-%d') = DATE_FORMAT(CURDATE(),'%Y-%m-%d')
		AND pu_group_daily_living.group_id = groupId LIMIT 1;

		IF gi is not null THEN
				UPDATE pu_group_daily_living SET pu_group_daily_living.create_time = SYSDATE() WHERE
				DATE_FORMAT(pu_group_daily_living.create_time,'%Y-%m-%d') = DATE_FORMAT(CURDATE(),'%Y-%m-%d')
				AND pu_group_daily_living.group_id = groupId;
		ELSE
				INSERT INTO pu_group_daily_living (pu_group_daily_living.group_id) VALUES (groupId);
		END IF;


	end IF;
END