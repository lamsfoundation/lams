SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- LDEV-4997 Migrate Etherpad to central as service

DROP TABLE tl_ladoku11_configuration;

UPDATE lams_tool SET admin_url = NULL WHERE tool_signature = 'ladoku11';

-- LDEV-5008 Rename doKumaran tool
UPDATE lams_learning_activity SET title = 'doKu' WHERE language_file = 'org.lamsfoundation.lams.tool.dokumaran.ApplicationResources';
UPDATE tl_ladoku11_dokumaran SET title = 'doKu' WHERE uid = 1;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
