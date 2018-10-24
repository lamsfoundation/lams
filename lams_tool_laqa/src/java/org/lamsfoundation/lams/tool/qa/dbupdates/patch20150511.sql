-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV- Set max and min rates limits
ALTER TABLE tl_laqa11_content ADD COLUMN minimum_rates integer DEFAULT 0; 
ALTER TABLE tl_laqa11_content ADD COLUMN maximum_rates integer DEFAULT 0;

ALTER TABLE tl_laqa11_que_content ADD COLUMN min_words_limit integer DEFAULT 0;

UPDATE lams_tool SET tool_version='20150511' WHERE tool_signature='laqa11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;