-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--  LDEV-4665 Remove Cognitive Skills Wizard
DROP TABLE tl_laqa11_wizard_category;
DROP TABLE tl_laqa11_wizard_cognitive_skill;
DROP TABLE tl_laqa11_wizard_question;
DROP TABLE tl_laqa11_configuration;
UPDATE lams_tool SET admin_url= NULL WHERE tool_signature = 'laqa11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;