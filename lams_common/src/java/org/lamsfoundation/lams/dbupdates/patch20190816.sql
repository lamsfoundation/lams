-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4827 Add configuration settings for Question Bank
INSERT INTO lams_configuration VALUES
('QbMergeEnable',	'true',	'config.qb.merge.enable',	'config.header.qb', 'BOOLEAN', 1);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;