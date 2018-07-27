-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3265 Ability to set questions required to be answered
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN answer_required TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-3263 Add support for renaming field names on sequence import
UPDATE lams_tool SET tool_version='20140707' WHERE tool_signature='laasse10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;