-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_latask10_tasklist MODIFY COLUMN reflect_instructions text;

----------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;
