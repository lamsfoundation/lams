-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3326 Option to include everyone results after survey responses are sent
ALTER TABLE tl_lasurv11_survey ADD COLUMN show_other_users_answers TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lasurv11_user ADD COLUMN response_finalized TINYINT(1) NOT NULL DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;