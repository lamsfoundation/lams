-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- LKC-15 adding a constraint to the scratchie user table so no same user_id and session_id can be repetead 
ALTER TABLE tl_lascrt11_user ADD UNIQUE INDEX(user_id, session_uid);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;