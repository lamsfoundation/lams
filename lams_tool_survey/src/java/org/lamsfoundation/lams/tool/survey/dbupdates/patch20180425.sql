-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lasurv11_question MODIFY description MEDIUMTEXT;
ALTER TABLE tl_lasurv11_answer MODIFY answer_text MEDIUMTEXT;
ALTER TABLE tl_lasurv11_option MODIFY description MEDIUMTEXT;
ALTER TABLE tl_lasurv11_survey MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lasurv11_survey MODIFY reflect_instructions MEDIUMTEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;