-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_labbb10_session DROP FOREIGN KEY fk_labbb10_bbb_session_to_bbb;
ALTER TABLE tl_labbb10_session ADD CONSTRAINT fk_labbb10_bbb_session_to_bbb FOREIGN KEY (`bbb_uid`)
REFERENCES `tl_labbb10_bbb` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_labbb10_user DROP FOREIGN KEY fk_labbb10_bbb_user_to_bbb_session;
ALTER TABLE tl_labbb10_user ADD CONSTRAINT fk_labbb10_bbb_user_to_bbb_session FOREIGN KEY (`bbb_session_uid`)
REFERENCES `tl_labbb10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;