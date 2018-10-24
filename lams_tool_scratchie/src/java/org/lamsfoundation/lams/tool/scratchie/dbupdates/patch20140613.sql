-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3251 Create configurable grading scale for scratchie
CREATE TABLE tl_lascrt11_configuration (
   uid BIGINT NOT NULL auto_increment,
   config_key VARCHAR(30) UNIQUE,
   config_value VARCHAR(255),
   PRIMARY KEY (uid)
)ENGINE=InnoDB;
UPDATE lams_tool SET admin_url='tool/lascrt11/admin/start.do' WHERE tool_signature='lascrt11';

INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES
  ('isEnabledExtraPointOption',	'true');
INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES
  ('presetMarks', '4,2,1,0');


UPDATE lams_tool SET tool_version='20140613' WHERE tool_signature='lascrt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;