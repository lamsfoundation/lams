-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lasurv11_survey DROP COLUMN online_instructions;
ALTER TABLE tl_lasurv11_survey DROP COLUMN offline_instructions;
ALTER TABLE tl_lasurv11_survey DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lasurv11_attachment;

-- LDEV-3326 Option to include everyone results after survey responses are sent
ALTER TABLE tl_lasurv11_survey ADD COLUMN show_other_users_answers TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lasurv11_user ADD COLUMN response_finalized TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_lasurv11_survey MODIFY COLUMN reflect_instructions text;

-- LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lasurv11_session ADD UNIQUE (session_id);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_lasurv11_option DROP FOREIGN KEY FK85AB46F26966134F;
ALTER TABLE tl_lasurv11_option ADD CONSTRAINT FK85AB46F26966134F FOREIGN KEY (`question_uid`)
REFERENCES `tl_lasurv11_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_question DROP FOREIGN KEY FK872D4F23D14146E5;
ALTER TABLE tl_lasurv11_question ADD CONSTRAINT FK872D4F23D14146E5 FOREIGN KEY (`survey_uid`)
REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_question DROP FOREIGN KEY FK872D4F23E4C99A5F;
ALTER TABLE tl_lasurv11_question ADD CONSTRAINT FK872D4F23E4C99A5F FOREIGN KEY (`create_by`)
REFERENCES `tl_lasurv11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_session DROP FOREIGN KEY FKF08793B9D14146E5;
ALTER TABLE tl_lasurv11_session ADD CONSTRAINT FKF08793B9D14146E5 FOREIGN KEY (`survey_uid`)
REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_survey DROP FOREIGN KEY FK8CC465D7E4C99A5F;
ALTER TABLE tl_lasurv11_survey ADD CONSTRAINT FK8CC465D7E4C99A5F FOREIGN KEY (`create_by`)
REFERENCES `tl_lasurv11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_user DROP FOREIGN KEY FK633F25884F803F63;
ALTER TABLE tl_lasurv11_user ADD CONSTRAINT FK633F25884F803F63 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lasurv11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_user DROP FOREIGN KEY FK633F2588D14146E5;
ALTER TABLE tl_lasurv11_user ADD CONSTRAINT FK633F2588D14146E5 FOREIGN KEY (`survey_uid`)
REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;			 

ALTER TABLE tl_lasurv11_answer DROP FOREIGN KEY FK6DAAFE3BB1423DC1;
ALTER TABLE tl_lasurv11_answer ADD CONSTRAINT FK6DAAFE3BB1423DC1 FOREIGN KEY (`user_uid`)
REFERENCES `tl_lasurv11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasurv11_answer DROP FOREIGN KEY FK6DAAFE3B25F3BB77;
ALTER TABLE tl_lasurv11_answer ADD CONSTRAINT FK6DAAFE3B25F3BB77 FOREIGN KEY (`question_uid`)
REFERENCES `tl_lasurv11_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-4180
ALTER TABLE tl_lasurv11_survey MODIFY COLUMN lock_on_finished TINYINT(1),
							   MODIFY COLUMN content_in_use TINYINT(1),
							   MODIFY COLUMN define_later TINYINT(1),
							   MODIFY COLUMN show_questions_on_one_page TINYINT(1),
							   MODIFY COLUMN reflect_on_activity TINYINT(1),
							   MODIFY COLUMN answer_submit_notify TINYINT(1);
									 	
ALTER TABLE tl_lasurv11_question MODIFY COLUMN append_text TINYINT(1),
								 MODIFY COLUMN optional TINYINT(1),
								 MODIFY COLUMN allow_multiple_answer TINYINT(1);

ALTER TABLE tl_lasurv11_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lasurv11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;