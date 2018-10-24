-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LKC-40 prevent Massive load of DB when accessing tool with 20+ users
ALTER TABLE tl_lascrt11_user ADD INDEX userIdIndex (user_id);
ALTER TABLE tl_lascrt11_session ADD INDEX sessionIdIndex (session_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;