-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lakalt11_comment MODIFY comment MEDIUMTEXT;
ALTER TABLE tl_lakalt11_kaltura MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lakalt11_kaltura MODIFY reflect_instructions MEDIUMTEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;