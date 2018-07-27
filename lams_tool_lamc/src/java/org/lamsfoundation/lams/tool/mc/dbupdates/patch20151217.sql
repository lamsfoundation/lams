-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lamc11_options_content DROP FOREIGN KEY FK_tl_lamc11_options_content_1;
ALTER TABLE tl_lamc11_options_content ADD CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (`mc_que_content_id`)
REFERENCES `tl_lamc11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_que_content DROP FOREIGN KEY FK_tl_lamc11_que_content_1;
ALTER TABLE tl_lamc11_que_content ADD CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (`mc_content_id`)
REFERENCES `tl_lamc11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_que_usr DROP FOREIGN KEY FK_tl_lamc11_que_usr_1;
ALTER TABLE tl_lamc11_que_usr ADD CONSTRAINT FK_tl_lamc11_que_usr_1 FOREIGN KEY (`mc_session_id`)
REFERENCES `tl_lamc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_session DROP FOREIGN KEY FK_lamc11_session1;
ALTER TABLE tl_lamc11_session ADD CONSTRAINT FK_lamc11_session1 FOREIGN KEY (`mc_group_leader_uid`)
REFERENCES `tl_lamc11_que_usr` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_session DROP FOREIGN KEY FK_tl_lamc_session_1;
ALTER TABLE tl_lamc11_session ADD CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (`mc_content_id`)
REFERENCES `tl_lamc11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_2;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_2 FOREIGN KEY (`mc_que_content_id`)
REFERENCES `tl_lamc11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_3 FOREIGN KEY (`mc_que_option_id`)
REFERENCES `tl_lamc11_options_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_4;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_4 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_lamc11_que_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;