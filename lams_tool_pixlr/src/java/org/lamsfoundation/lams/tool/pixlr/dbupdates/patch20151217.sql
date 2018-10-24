-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lapixl10_session DROP FOREIGN KEY FKE5C05E7FCB8ADA74;
ALTER TABLE tl_lapixl10_session ADD CONSTRAINT FKE5C05E7FCB8ADA74 FOREIGN KEY (`pixlr_uid`)
REFERENCES `tl_lapixl10_pixlr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lapixl10_user DROP FOREIGN KEY FK9A39C08236E23005;
ALTER TABLE tl_lapixl10_user ADD CONSTRAINT FK9A39C08236E23005 FOREIGN KEY (`pixlr_session_uid`)
REFERENCES `tl_lapixl10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;