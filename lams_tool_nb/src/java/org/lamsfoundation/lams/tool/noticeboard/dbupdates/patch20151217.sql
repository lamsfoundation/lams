-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lanb11_session DROP FOREIGN KEY FK_tl_lanb11_session_1;
ALTER TABLE tl_lanb11_session ADD CONSTRAINT FK_tl_lanb11_session_1 FOREIGN KEY (`nb_content_uid`)
REFERENCES `tl_lanb11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lanb11_user DROP FOREIGN KEY FK_tl_lanb11_user_1;
ALTER TABLE tl_lanb11_user ADD CONSTRAINT FK_tl_lanb11_user_1 FOREIGN KEY (`nb_session_uid`)
REFERENCES `tl_lanb11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;