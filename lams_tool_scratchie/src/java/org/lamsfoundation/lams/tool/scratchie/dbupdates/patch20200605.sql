-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5032 Allow hiding learner names in confidence levels in Scratchie

ALTER TABLE tl_lascrt11_scratchie ADD COLUMN confidence_levels_anonymous TINYINT DEFAULT 0 AFTER confidence_levels_activity_uiid;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
