-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3094 In Gradebook fix computing overall marks when using Groups
UPDATE lams_gradebook_user_lesson AS gless,
	(SELECT tses.lesson_id, gact.user_id, SUM(gact.mark) AS mark
	FROM lams_gradebook_user_activity AS gact
	JOIN (SELECT DISTINCT lesson_id, activity_id FROM lams_tool_session) AS tses
	USING (activity_id)
	GROUP BY tses.lesson_id, gact.user_id) AS agg
SET gless.mark = agg.mark
WHERE gless.lesson_id = agg.lesson_id AND gless.user_id = agg.user_id; 

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
