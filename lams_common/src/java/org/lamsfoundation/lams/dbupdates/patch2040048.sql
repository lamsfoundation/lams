-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3606 Add comment's posted date
ALTER TABLE lams_rating_comment ADD COLUMN posted_date datetime;
UPDATE lams_rating_comment SET posted_date=NOW();

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;