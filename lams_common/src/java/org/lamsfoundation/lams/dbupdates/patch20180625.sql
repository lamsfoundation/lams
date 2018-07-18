-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4603 Delete preview lessons with peer review activity doesn't work
ALTER TABLE lams_rating DROP FOREIGN KEY FK_lams_rating_3;
ALTER TABLE lams_rating_comment DROP FOREIGN KEY FK_lams_rating_comment_3;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;