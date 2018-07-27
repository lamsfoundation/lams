-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3491 Mark designs as removed without deleting the record
ALTER TABLE lams_learning_design ADD COLUMN removed TINYINT(1) NOT NULL DEFAULT 0;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
