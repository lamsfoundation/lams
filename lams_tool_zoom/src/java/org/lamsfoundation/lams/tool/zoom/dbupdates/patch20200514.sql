SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;


-- LDEV-5026 Always password protect Zoom meetings
ALTER TABLE tl_lazoom10_zoom DROP COLUMN enable_meeting_password;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;