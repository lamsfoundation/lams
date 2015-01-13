-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3410 Add feature allowing disable export portfolio and live edit 
ALTER TABLE lams_organisation ADD COLUMN enable_live_edit TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_single_activity_lessons;
ALTER TABLE lams_organisation ADD COLUMN enable_export_portfolio TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_live_edit;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
