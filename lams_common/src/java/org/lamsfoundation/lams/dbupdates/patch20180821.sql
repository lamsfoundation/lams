-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4645 Add columns for default lesson settings
ALTER TABLE lams_ext_server_org_map
	ADD COLUMN learner_presence_avail TINYINT(1) DEFAULT 0,
	ADD COLUMN learner_im_avail TINYINT(1) DEFAULT 0,
	ADD COLUMN live_edit_enabled TINYINT(1) DEFAULT 1,
	ADD COLUMN enable_lesson_notifications TINYINT(1) DEFAULT 1,
	ADD COLUMN force_restart TINYINT(1) DEFAULT 0,
	ADD COLUMN allow_restart TINYINT(1) DEFAULT 0,
	ADD COLUMN gradebook_on_complete TINYINT(1) DEFAULT 1;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;