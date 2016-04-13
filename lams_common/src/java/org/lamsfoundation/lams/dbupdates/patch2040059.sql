-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2949 Remove obsolete column
ALTER TABLE lams_user DROP COLUMN chat_id;

COMMIT;
SET AUTOCOMMIT = 1;