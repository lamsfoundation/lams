-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lasbmt11_content MODIFY instruction MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_content MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_report MODIFY comments MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_submission_details MODIFY fileDescription MEDIUMTEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;