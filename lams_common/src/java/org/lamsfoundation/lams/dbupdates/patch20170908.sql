-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- Add column for weighted outputs

ALTER TABLE lams_activity_evaluation ADD COLUMN weight TINYINT DEFAULT NULL,
									 DROP PRIMARY KEY,
									 ADD PRIMARY KEY (activity_id),
									 DROP COLUMN activity_evaluation_id;

DELETE FROM lams_configuration WHERE config_key LIKE '%ClientVersion';

UPDATE lams_configuration SET config_value = '3.0.1' WHERE config_key = 'ServerVersionNumber';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
