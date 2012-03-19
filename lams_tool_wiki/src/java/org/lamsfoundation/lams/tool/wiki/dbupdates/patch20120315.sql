-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE tl_lawiki10_wiki_page ADD COLUMN deleted bit DEFAULT 0 AFTER editable;

SET FOREIGN_KEY_CHECKS=1;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;