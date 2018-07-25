-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3523 Type used to classify a learning design into a grouping understood by a user. Can be used by integrations and contents determined by the integration.
ALTER TABLE lams_learning_design ADD COLUMN design_type VARCHAR(255);

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
