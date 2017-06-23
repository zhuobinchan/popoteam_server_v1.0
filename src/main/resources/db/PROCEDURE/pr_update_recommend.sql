CREATE PROCEDURE `pr_update_recommend`(
		IN `groupId` bigint
)
BEGIN
	#Routine body goes here...
	if groupId is not null then
		DELETE FROM pu_user_recommend
          WHERE user_id in (
            SELECT user_id FROM pu_group_member WHERE group_id = groupId
          );

		insert into pu_recommend_history (recommend_id, main_group_id, match_group_id, create_time)
			select id , main_group_id, match_group_id, create_time
			from pu_recommend
			WHERE main_group_id = groupId;

		DELETE FROM pu_recommend WHERE main_group_id = groupId;

	end IF;
END