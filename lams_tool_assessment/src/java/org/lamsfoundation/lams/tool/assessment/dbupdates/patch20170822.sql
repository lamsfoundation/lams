-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4410 Add unique index to prevent duplicate result copied from leader
ALTER TABLE tl_laasse10_assessment_result ADD UNIQUE KEY UQ_tl_laasse10_assessment_result_4 (assessment_uid, user_uid, finish_date);

UPDATE lams_tool SET tool_version='20170822' WHERE tool_signature='laasse10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;