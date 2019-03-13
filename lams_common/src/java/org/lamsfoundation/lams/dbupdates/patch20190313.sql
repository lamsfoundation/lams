-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4788 Remove reference to organisation in learning outcomes. They are all global now.

ALTER TABLE lams_outcome_scale DROP FOREIGN KEY FK_lams_outcome_scale_1,
						 	   DROP KEY code_2,
						 	   DROP COLUMN organisation_id,
						 	   DROP COLUMN content_folder_id;
						 
ALTER TABLE lams_outcome DROP FOREIGN KEY FK_lams_outcome_1,
						 DROP KEY code_2,
						 DROP COLUMN organisation_id,
						 DROP COLUMN content_folder_id;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;