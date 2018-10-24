-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--     LDEV-3688 Ability to remove/delete files from sbmt files

ALTER TABLE tl_lasbmt11_submission_details 
ADD COLUMN `removed` TINYINT(1) NOT NULL DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;