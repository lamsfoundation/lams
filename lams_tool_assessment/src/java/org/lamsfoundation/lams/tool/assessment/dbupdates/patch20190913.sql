-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4875 Add VSA question type to Assessment and Scratchie
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN activity_uuid_providing_vsa_answers INT(11);
								 	
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;