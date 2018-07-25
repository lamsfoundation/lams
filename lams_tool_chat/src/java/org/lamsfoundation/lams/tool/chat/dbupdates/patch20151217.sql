-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DC2AF61E05;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DC2AF61E05 FOREIGN KEY (`to_user_uid`)
REFERENCES `tl_lachat11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DC9C8469FC;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DC9C8469FC FOREIGN KEY (`chat_session_uid`)
REFERENCES `tl_lachat11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DCCF3BF9B6;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DCCF3BF9B6 FOREIGN KEY (`from_user_uid`)
REFERENCES `tl_lachat11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lachat11_session DROP FOREIGN KEY FK96E446B1A3926E3;
ALTER TABLE tl_lachat11_session ADD CONSTRAINT FK96E446B1A3926E3 FOREIGN KEY (`chat_uid`)
REFERENCES `tl_lachat11_chat` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lachat11_user DROP FOREIGN KEY FK4EB82169C8469FC;
ALTER TABLE tl_lachat11_user ADD CONSTRAINT FK4EB82169C8469FC FOREIGN KEY (`chat_session_uid`)
REFERENCES `tl_lachat11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;