BEGIN

	#Routine body goes here...

	declare ui BIGINT(20);  
	if userId is not null then
		
		
		SELECT userId INTO ui FROM pu_user_daily_living WHERE
		DATE_FORMAT(pu_user_daily_living.create_time,'%Y-%m-%d') = DATE_FORMAT(CURDATE(),'%Y-%m-%d')
		AND pu_user_daily_living.user_id = userId LIMIT 1;

		IF ui is not null THEN
				UPDATE pu_user_daily_living SET pu_user_daily_living.create_time = SYSDATE()
				WHERE DATE_FORMAT(pu_user_daily_living.create_time,'%Y-%m-%d') = DATE_FORMAT(CURDATE(),'%Y-%m-%d')
				AND pu_user_daily_living.user_id = userId;
		ELSE
				INSERT INTO pu_user_daily_living (pu_user_daily_living.user_id) VALUES (userId);
		END IF;


	end IF;
END