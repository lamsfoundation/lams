-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4819 Add configuration setting for disabling Learning Outcome quick add by authors
INSERT INTO lams_configuration VALUES
('LearningOutcomeQuickAddEnable', 'true', 'config.learning.outcome.add.enable', 'config.header.features', 'BOOLEAN', 1);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;