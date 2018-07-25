-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;