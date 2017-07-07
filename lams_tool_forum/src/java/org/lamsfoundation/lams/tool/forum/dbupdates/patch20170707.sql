-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4371 Add Forum admin page
CREATE TABLE tl_lafrum11_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(30),
  config_value VARCHAR(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

INSERT INTO tl_lafrum11_configuration (config_key, config_value) VALUES ('keepLearnerContent',	'false');

UPDATE lams_tool SET admin_url='tool/lafrum11/admin/start.do' WHERE tool_signature='lafrum11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
