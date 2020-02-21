SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190510.sql to patch20191205.sql
-- It should upgrade this tool to version 4.0

-- LDEV-4817 Delete existing blank burning questions
DELETE FROM tl_lascrt11_burning_question WHERE question IS NULL OR TRIM(question) = '';

--LDEV-4845 Bump version so content version filter kicks in
UPDATE lams_tool SET tool_version='20190809' WHERE tool_signature='lascrt11';

--LDEV-4921 Allow question title hiding in learner
INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

-- LDEV-4951 Update tools version for LAMS 4.0 release
UPDATE lams_tool SET tool_version='20190219' WHERE tool_signature='lascrt11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;