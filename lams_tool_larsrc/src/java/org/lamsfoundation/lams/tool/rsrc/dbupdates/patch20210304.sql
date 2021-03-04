-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5186 Merge two "notify on submit" column into one
UPDATE tl_larsrc11_resource SET assigment_submit_notify = 1 WHERE file_upload_notify = 1;

ALTER TABLE tl_larsrc11_resource DROP COLUMN file_upload_notify;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
