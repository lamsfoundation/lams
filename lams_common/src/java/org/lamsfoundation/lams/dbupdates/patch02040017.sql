-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3070 Allow single activity lesson creation on redesigned index page
ALTER TABLE lams_organisation ADD COLUMN enable_single_activity_lessons TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_learner_gradebook; 

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
