-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_ladaco10_users DROP FOREIGN KEY UserToDaco;
ALTER TABLE tl_ladaco10_users ADD CONSTRAINT UserToDaco FOREIGN KEY (content_uid)
REFERENCES tl_ladaco10_contents (uid) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;