-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5250 Delete Pedagogical Planner table

DROP TABLE lams_planner_activity_metadata;
DROP TABLE lams_planner_recent_learning_designs;

ALTER TABLE lams_tool DROP COLUMN pedagogical_planner_url;
ALTER TABLE lams_system_tool DROP COLUMN pedagogical_planner_url;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
