-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-4097 Change lams_ext_user_userid_map's tc_gradebook_id to text
ALTER TABLE lams_ext_user_userid_map CHANGE COLUMN tc_gradebook_id tc_gradebook_id TEXT;

COMMIT;
SET AUTOCOMMIT = 1;
