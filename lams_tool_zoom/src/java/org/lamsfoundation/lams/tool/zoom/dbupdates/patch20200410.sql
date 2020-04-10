SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;


-- LDEV-5006 Password protect Zoom meetings
ALTER TABLE tl_lazoom10_zoom ADD COLUMN meeting_password CHAR(6);


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;