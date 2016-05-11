-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades
 
ALTER TABLE tl_lavote11_nomination_content DROP FOREIGN KEY FK_tl_lavote11_nomination_content_1;
ALTER TABLE tl_lavote11_nomination_content ADD CONSTRAINT FK_tl_lavote11_nomination_content_1 FOREIGN KEY (`vote_content_id`)
REFERENCES `tl_lavote11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 

ALTER TABLE tl_lavote11_usr DROP FOREIGN KEY FK_tl_lavote11_usr_1;
ALTER TABLE tl_lavote11_usr ADD CONSTRAINT FK_tl_lavote11_usr_1 FOREIGN KEY (`vote_session_id`)
REFERENCES `tl_lavote11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lavote11_session DROP FOREIGN KEY FK_tl_lavote11_session_1;
ALTER TABLE tl_lavote11_session ADD CONSTRAINT FK_tl_lavote11_session_1 FOREIGN KEY (`vote_content_id`)
REFERENCES `tl_lavote11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lavote11_usr_attempt DROP FOREIGN KEY FK_tl_lavote11_usr_attempt_2;
ALTER TABLE tl_lavote11_usr_attempt ADD CONSTRAINT FK_tl_lavote11_usr_attempt_2 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_lavote11_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lavote11_usr_attempt DROP FOREIGN KEY FK_tl_lavote11_usr_attempt_3;
ALTER TABLE tl_lavote11_usr_attempt ADD CONSTRAINT FK_tl_lavote11_usr_attempt_3 FOREIGN KEY (`vote_nomination_content_id`)
REFERENCES `tl_lavote11_nomination_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
-- This has to be added as Hibernate is not aware of the cascade
ALTER TABLE tl_lavote11_usr_attempt MODIFY COLUMN vote_nomination_content_id bigint(20) DEFAULT NULL;


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;