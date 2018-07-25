-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3292 These indexes will help Hibernate.
CREATE INDEX idx_user_user_id ON tl_larsrc11_user(user_id);
CREATE INDEX idx_item_log_session_id ON tl_larsrc11_item_log(session_id); 

----------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;