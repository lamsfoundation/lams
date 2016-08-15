-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_laqa11_que_content DROP FOREIGN KEY FK_tl_laqa11_que_content_1;
ALTER TABLE tl_laqa11_que_content ADD CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (`qa_content_id`)
REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laqa11_que_usr DROP FOREIGN KEY FK_tl_laqa11_que_usr_1;
ALTER TABLE tl_laqa11_que_usr ADD CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (`qa_session_id`)
REFERENCES `tl_laqa11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laqa11_session DROP FOREIGN KEY FK_laqa11_session1;
ALTER TABLE tl_laqa11_session ADD CONSTRAINT FK_laqa11_session1 FOREIGN KEY (`qa_group_leader_uid`)
REFERENCES `tl_laqa11_que_usr` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_session DROP FOREIGN KEY FK_tl_laqa11_session_1;
ALTER TABLE tl_laqa11_session ADD CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (`qa_content_id`)
REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laqa11_usr_resp MODIFY COLUMN qa_que_content_id BIGINT(20) DEFAULT NULL;
ALTER TABLE tl_laqa11_usr_resp DROP FOREIGN KEY FK_tl_laqa11_usr_resp_2;
ALTER TABLE tl_laqa11_usr_resp ADD CONSTRAINT FK_tl_laqa11_usr_resp_2 FOREIGN KEY (`qa_que_content_id`)
REFERENCES `tl_laqa11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_usr_resp DROP FOREIGN KEY FK_tl_laqa11_usr_resp_3;
ALTER TABLE tl_laqa11_usr_resp ADD CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_laqa11_que_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laqa11_wizard_cognitive_skill DROP FOREIGN KEY FK3BA4132BCBB0DC8D;
ALTER TABLE tl_laqa11_wizard_cognitive_skill ADD CONSTRAINT FK3BA4132BCBB0DC8D FOREIGN KEY (`category_uid`)
REFERENCES `tl_laqa11_wizard_category` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laqa11_wizard_question DROP FOREIGN KEY FKAF08A0C7EFF77FD4;
ALTER TABLE tl_laqa11_wizard_question ADD CONSTRAINT FKAF08A0C7EFF77FD4 FOREIGN KEY (`cognitive_skill_uid`)
REFERENCES `tl_laqa11_wizard_cognitive_skill` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;