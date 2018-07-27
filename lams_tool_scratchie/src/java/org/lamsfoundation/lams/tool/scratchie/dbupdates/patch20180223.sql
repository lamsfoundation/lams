-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3224 Ability to change, add, remove questions even after student have reached it
ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFC309ED320;
ALTER TABLE tl_lascrt11_user DROP COLUMN scratchie_uid, DROP INDEX FK_NEW_610529188_30113BFC309ED320;

UPDATE lams_tool SET tool_version='20180223' WHERE tool_signature='lascrt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;