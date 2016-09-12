-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3292 Add indexes, as prompted by a client
ALTER TABLE tl_lafrum11_forum_user ADD INDEX idx_user_id (user_id);
ALTER TABLE lams_notebook_entry ADD INDEX idx_create_date (create_date);

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
