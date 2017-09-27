-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4428 Start supporting cy and id languages in CKEditor
UPDATE lams_supported_locale SET fckeditor_code='cy' WHERE locale_id='15';
UPDATE lams_supported_locale SET fckeditor_code='id' WHERE locale_id='31';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;