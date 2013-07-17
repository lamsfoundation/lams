-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3078 Wiki tool should start uploading images to www/secure/runtime/ folder
ALTER TABLE tl_lawiki10_session DROP COLUMN content_folder_id;

SET FOREIGN_KEY_CHECKS=1;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;