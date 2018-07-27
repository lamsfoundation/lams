-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--  LDEV-3802 Update IMS CC Author and Monitor to Bootstrap
ALTER TABLE tl_laimsc11_commoncartridge_item DROP FOREIGN KEY FK_NEW_1279208528_F52D1F93EC0D3147;
ALTER TABLE tl_laimsc11_commoncartridge_item DROP COLUMN session_uid;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;