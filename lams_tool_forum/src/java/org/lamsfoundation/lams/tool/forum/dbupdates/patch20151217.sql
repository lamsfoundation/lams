-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lafrum11_attachment DROP FOREIGN KEY FK389AD9A2131CE31E;
ALTER TABLE tl_lafrum11_attachment ADD CONSTRAINT FK389AD9A2131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_attachment DROP FOREIGN KEY FK389AD9A2FE939F2A;
ALTER TABLE tl_lafrum11_attachment ADD CONSTRAINT FK389AD9A2FE939F2A FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 

ALTER TABLE tl_lafrum11_forum DROP FOREIGN KEY FK87917942E42F4351;
ALTER TABLE tl_lafrum11_forum ADD CONSTRAINT FK87917942E42F4351 FOREIGN KEY (`create_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

 
ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_1;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_1 FOREIGN KEY (`user_id`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_2;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_2 FOREIGN KEY (`message_id`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY FKD2C71F8845213B4D;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT FKD2C71F8845213B4D FOREIGN KEY (`root_message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY FKD2C71F88FE939F2A;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT FKD2C71F88FE939F2A FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message_seq DROP FOREIGN KEY fkfrum11mseqthread;
ALTER TABLE tl_lafrum11_message_seq ADD CONSTRAINT fkfrum11mseqthread FOREIGN KEY (`thread_message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8647A7264;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8647A7264 FOREIGN KEY (`modified_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;
							 	
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8E42F4351;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8E42F4351 FOREIGN KEY (`create_by`)
REFERENCES `tl_lafrum11_forum_user` (`uid`)	ON DELETE SET NULL ON UPDATE CASCADE;
							 	
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E824089E4D;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E824089E4D FOREIGN KEY (`parent_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E8131CE31E;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E8131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
			
ALTER TABLE tl_lafrum11_message DROP FOREIGN KEY FK4A6067E89357B45B;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E89357B45B FOREIGN KEY (`forum_session_uid`)
REFERENCES `tl_lafrum11_tool_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
			

ALTER TABLE tl_lafrum11_tool_session DROP FOREIGN KEY FK5A04D7AE131CE31E;
ALTER TABLE tl_lafrum11_tool_session ADD CONSTRAINT FK5A04D7AE131CE31E FOREIGN KEY (`forum_uid`)
REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_timestamp DROP FOREIGN KEY ForumUserFK;
ALTER TABLE tl_lafrum11_timestamp ADD CONSTRAINT ForumUserFK FOREIGN KEY (`forum_user_uid`)
REFERENCES `tl_lafrum11_forum_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lafrum11_timestamp DROP FOREIGN KEY MessageFK;
ALTER TABLE tl_lafrum11_timestamp ADD CONSTRAINT MessageFK FOREIGN KEY (`message_uid`)
REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lafrum11_forum_user DROP FOREIGN KEY FK7B83A4A85F0116B6;
ALTER TABLE tl_lafrum11_forum_user ADD CONSTRAINT FK7B83A4A85F0116B6 FOREIGN KEY (`session_id`)
REFERENCES `tl_lafrum11_tool_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;