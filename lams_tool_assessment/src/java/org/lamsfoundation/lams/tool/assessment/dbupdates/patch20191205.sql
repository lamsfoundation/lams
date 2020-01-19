-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4921 Allow question title hiding in learner

 CREATE TABLE `tl_laasse10_configuration` (
  `config_key` varchar(30),
  `config_value` varchar(255),
  PRIMARY KEY (`config_key`)
 );

INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

UPDATE lams_tool SET admin_url = 'tool/laasse10/admin/start.do' WHERE tool_signature = 'laasse10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
