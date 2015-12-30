-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lawook10_session DROP FOREIGN KEY FK_WOOKIE_SESSION;
ALTER TABLE tl_lawook10_session ADD CONSTRAINT FK_WOOKIE_SESSION FOREIGN KEY (`wookie_uid`)
REFERENCES `tl_lawook10_wookie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawook10_user DROP FOREIGN KEY FK_WOOKIE_USER;
ALTER TABLE tl_lawook10_user ADD CONSTRAINT FK_WOOKIE_USER FOREIGN KEY (`wookie_session_uid`)
REFERENCES `tl_lawook10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;