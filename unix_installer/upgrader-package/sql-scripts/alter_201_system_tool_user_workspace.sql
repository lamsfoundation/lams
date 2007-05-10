-- Script to be run for LAMS 2.0.1 release, on LAMS 2.0 tables.
-- Change the url for the learner progress screen for grouping.
-- Add a flag to the user table to indicate that the user needs to change their password

UPDATE lams_system_tool 
SET learner_progress_url = "learning/grouping.do?method=viewGrouping&mode=teacher"
WHERE tool_display_name = "Grouping";

ALTER TABLE lams_user
MODIFY COLUMN login varchar(255);

ALTER TABLE lams_user
ADD COLUMN change_password TINYINT DEFAULT 0;

ALTER TABLE lams_workspace_folder
MODIFY COLUMN name VARCHAR(255) NOT NULL;

UPDATE lams_user 
SET change_password = 0
WHERE change_password is null;

COMMIT;
