-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4609 Bind archived activity marks in batches
DELETE FROM lams_gradebook_user_activity_archive;
DELETE FROM lams_gradebook_user_lesson_archive;

ALTER TABLE lams_gradebook_user_lesson_archive ADD COLUMN archive_date DATETIME;
ALTER TABLE lams_gradebook_user_activity_archive ADD COLUMN archive_date DATETIME;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;