-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5002 Add question Etherpads

ALTER TABLE tl_laasse10_assessment ADD COLUMN question_etherpad_enabled TINYINT(1) DEFAULT 0 AFTER use_select_leader_tool_ouput;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
