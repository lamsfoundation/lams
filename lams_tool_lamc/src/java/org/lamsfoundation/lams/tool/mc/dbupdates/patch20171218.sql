-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4490 Fix NULL ConfidenceLevel columns creating exceptions
UPDATE tl_lamc11_usr_attempt SET confidence_level = 0 WHERE confidence_level IS NULL;

ALTER TABLE tl_lamc11_usr_attempt
CHANGE COLUMN confidence_level confidence_level INT(11) NOT NULL DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;