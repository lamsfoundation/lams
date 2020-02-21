SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190517.sql to patch20191205.sql
-- It should upgrade this tool to version 4.0

--LDEV-4821 remove obsolete hash field
ALTER TABLE tl_lamc11_que_content DROP COLUMN question_hash;

UPDATE lams_tool SET tool_version='20190517' WHERE tool_signature='lamc11';

--LDEV-4845 Bump version so content version filter kicks in

UPDATE lams_tool SET tool_version='20190809' WHERE tool_signature='lamc11';

--LDEV-4921 Allow question title hiding in learner

 CREATE TABLE `tl_lamc11_configuration` (
  `config_key` varchar(30),
  `config_value` varchar(255),
  PRIMARY KEY (`config_key`)
 );

INSERT INTO `tl_lamc11_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

UPDATE lams_tool SET admin_url = 'tool/lamc11/admin/start.do' WHERE tool_signature = 'lamc11';

-- LDEV-4951 Update tools version for LAMS 4.0 release
UPDATE lams_tool SET tool_version='20190219' WHERE tool_signature='lamc11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;