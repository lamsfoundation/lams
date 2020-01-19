-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_ladaco10_contents DROP COLUMN online_instructions;
ALTER TABLE tl_ladaco10_contents DROP COLUMN offline_instructions;
ALTER TABLE tl_ladaco10_contents DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_ladaco10_attachments;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='ladaco10';

-- patch20150217.sql
-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_ladaco10_contents MODIFY COLUMN reflect_instructions text;

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_ladaco10_answer_options DROP FOREIGN KEY tl_ladaco10_answer_options_ibfk_1;
ALTER TABLE tl_ladaco10_answer_options ADD CONSTRAINT tl_ladaco10_answer_options_ibfk_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_ladaco10_questions` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_ladaco10_contents DROP FOREIGN KEY DacoToUser;
ALTER TABLE tl_ladaco10_contents ADD CONSTRAINT DacoToUser FOREIGN KEY (`create_by`)
REFERENCES `tl_ladaco10_users` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_ladaco10_questions DROP FOREIGN KEY QuestionToDaco;
ALTER TABLE tl_ladaco10_questions ADD CONSTRAINT QuestionToDaco FOREIGN KEY (`content_uid`)
REFERENCES `tl_ladaco10_contents` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_ladaco10_questions DROP FOREIGN KEY QuestionToUser;
ALTER TABLE tl_ladaco10_questions ADD CONSTRAINT QuestionToUser FOREIGN KEY (`create_by`)
REFERENCES `tl_ladaco10_users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_ladaco10_questions DROP FOREIGN KEY tl_ladaco10_questions_ibfk_1;
ALTER TABLE tl_ladaco10_questions ADD CONSTRAINT tl_ladaco10_questions_ibfk_1 FOREIGN KEY (`session_uid`)
REFERENCES `tl_ladaco10_sessions` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_ladaco10_sessions DROP FOREIGN KEY SessionToDaco;
ALTER TABLE tl_ladaco10_sessions ADD CONSTRAINT SessionToDaco FOREIGN KEY (`content_uid`)
REFERENCES `tl_ladaco10_contents` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_ladaco10_users DROP FOREIGN KEY UserToSession;
ALTER TABLE tl_ladaco10_users ADD CONSTRAINT UserToSession FOREIGN KEY (`session_uid`)
REFERENCES `tl_ladaco10_sessions` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
							  

ALTER TABLE tl_ladaco10_answers DROP FOREIGN KEY AnswerToUser;
ALTER TABLE tl_ladaco10_answers ADD CONSTRAINT AnswerToUser FOREIGN KEY (`user_uid`)
REFERENCES `tl_ladaco10_users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_ladaco10_answers DROP FOREIGN KEY AnswerToQuestion;
ALTER TABLE tl_ladaco10_answers ADD CONSTRAINT AnswerToQuestion FOREIGN KEY (`question_uid`)
REFERENCES `tl_ladaco10_questions` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20160927.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_ladaco10_users DROP FOREIGN KEY UserToDaco;
ALTER TABLE tl_ladaco10_users ADD CONSTRAINT UserToDaco FOREIGN KEY (content_uid)
REFERENCES tl_ladaco10_contents (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20170101.sql
-- LDEV-4180
ALTER TABLE tl_ladaco10_contents MODIFY COLUMN lock_on_finished TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN content_in_use TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN define_later TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN reflect_on_activity TINYINT(1),
								 MODIFY COLUMN learner_entry_notify TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN record_submit_notify TINYINT(1) DEFAULT 0;
								 
ALTER TABLE tl_ladaco10_questions MODIFY COLUMN is_required tinyint(1) DEFAULT 0;		

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='ladaco10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
