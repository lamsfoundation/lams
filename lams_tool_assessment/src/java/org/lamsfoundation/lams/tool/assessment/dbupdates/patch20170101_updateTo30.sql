-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- patch20130216.sql
-- 	LDEV-2953 Add notebook option after learners finish Assessment
ALTER TABLE tl_laasse10_assessment ADD COLUMN reflect_on_activity BIT DEFAULT false;
ALTER TABLE tl_laasse10_assessment ADD COLUMN reflect_instructions TEXT;

-- patch20130611.sql
-- LDEV-3048 Make question numbering optional
ALTER TABLE tl_laasse10_assessment ADD COLUMN numbered tinyint DEFAULT true;

-- patch20131118.sql
-- LDEV-3151 Display max mark correctly for question
ALTER TABLE tl_laasse10_question_result ADD COLUMN max_mark FLOAT;

-- patch20140101.sql
-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_laasse10_assessment ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_laasse10_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_laasse10_session ADD INDEX FK_laasse10_session_1 (group_leader_uid), ADD CONSTRAINT tl_laasse10_session FOREIGN KEY (group_leader_uid) REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the assessment user table so no same user_id and session_id can be repetead 
-- (Please, note there are some duplicate users exist (their session_uid is null), they got formed when techers created questions in authoring. It's required to keep track of questions' authors.) 
ALTER TABLE tl_laasse10_user ADD UNIQUE INDEX(user_id, session_uid);

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_laasse10_assessment DROP COLUMN online_instructions;
ALTER TABLE tl_laasse10_assessment DROP COLUMN offline_instructions;
ALTER TABLE tl_laasse10_assessment DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_laasse10_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='laasse10';

-- patch20140428.sql
-- LDEV-3224 Ability to change, add, remove questions even after student have reached it
ALTER TABLE tl_laasse10_assessment DROP COLUMN content_in_use;

UPDATE lams_tool SET tool_version='20140428' WHERE tool_signature='laasse10';

-- patch20140612.sql
-- LDEV-3249 Removing old redundant question answers that had left from the times when autopatch created new question answers on each save 
DELETE tl_laasse10_option_answer.* FROM tl_laasse10_question_result, tl_laasse10_option_answer
                      WHERE tl_laasse10_question_result.uid = tl_laasse10_option_answer.question_result_uid 
                      AND tl_laasse10_question_result.result_uid IS NULL;
DELETE FROM tl_laasse10_question_result where result_uid IS NULL;

-- patch20140707.sql
-- LDEV-3265 Ability to set questions required to be answered
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN answer_required TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-3263 Add support for renaming field names on sequence import
UPDATE lams_tool SET tool_version='20140707' WHERE tool_signature='laasse10';

-- patch20140723.sql
-- LDEV-
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN max_words_limit integer DEFAULT 0;
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN min_words_limit integer DEFAULT 0;

-- patch20150629.sql
-- LDEV-3489 New question type for marks hedging
ALTER TABLE tl_laasse10_question_option ADD COLUMN correct TINYINT(1) NOT NULL DEFAULT 0;

-- patch20150706.sql
-- LDEV-3492 Change behaviour results in MCQ question type with multiple answers in assessment
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN incorrect_answer_nullifies_mark TINYINT(1) NOT NULL DEFAULT 0;

-- patch20150731.sql
--  LDEV-3522 Add justification textareas to the hedging marks question type
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN hedging_justification_enabled TINYINT(1) NOT NULL DEFAULT 0;

-- patch20150928.sql
--  LDEV-3564 session_id must be unique
ALTER TABLE tl_laasse10_session ADD UNIQUE (session_id);

-- patch20151204.sql
--  LDEV-3627 Improve Assessment tool's monitor paging
ALTER TABLE tl_laasse10_assessment_result ADD COLUMN latest TINYINT(1) DEFAULT 0;

UPDATE tl_laasse10_assessment_result result
  JOIN
    ( SELECT user_uid, MAX(start_date) AS max_date
      FROM tl_laasse10_assessment_result
      GROUP BY user_uid
    ) AS c
    ON result.user_uid = c.user_uid
    AND result.start_date = c.max_date
SET 
    result.latest = 1;

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_laasse10_assessment_overall_feedback DROP FOREIGN KEY FK_tl_laasse10_assessment_overall_feedback_1;
ALTER TABLE tl_laasse10_assessment_overall_feedback ADD CONSTRAINT FK_tl_laasse10_assessment_overall_feedback_1 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F9330E79035;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F9330E79035 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93758092FB;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93EC0D3147;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laasse10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_result DROP FOREIGN KEY FK_tl_laasse10_assessment_result_2;
ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_2 FOREIGN KEY (`user_uid`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_result DROP FOREIGN KEY FK_tl_laasse10_assessment_result_3;
ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_3 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_unit DROP FOREIGN KEY FK_tl_laasse10_assessment_unit_1;
ALTER TABLE tl_laasse10_assessment_unit ADD CONSTRAINT FK_tl_laasse10_assessment_unit_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_laasse10_question_option DROP FOREIGN KEY FK_tl_laasse10_question_option_1;
ALTER TABLE tl_laasse10_question_option ADD CONSTRAINT FK_tl_laasse10_question_option_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_tl_laasse10_question_result_1;
ALTER TABLE tl_laasse10_question_result ADD CONSTRAINT FK_tl_laasse10_question_result_1	FOREIGN KEY (`result_uid`)
REFERENCES `tl_laasse10_assessment_result` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_NEW_1720029621_693580A438BF8DFE;
ALTER TABLE tl_laasse10_question_result ADD CONSTRAINT FK_NEW_1720029621_693580A438BF8DFE FOREIGN KEY (`assessment_question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_option_answer DROP FOREIGN KEY FK_tl_laasse10_option_answer_1;
ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_1 FOREIGN KEY (`question_result_uid`)
REFERENCES `tl_laasse10_question_result` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
	

ALTER TABLE tl_laasse10_question_reference DROP FOREIGN KEY FK_tl_laasse10_question_reference_1;
ALTER TABLE tl_laasse10_question_reference ADD CONSTRAINT FK_tl_laasse10_question_reference_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_question_reference DROP FOREIGN KEY FK_tl_laasse10_question_reference_2;
ALTER TABLE tl_laasse10_question_reference ADD CONSTRAINT FK_tl_laasse10_question_reference_2 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 
 
ALTER TABLE tl_laasse10_user DROP FOREIGN KEY FK_NEW_1720029621_30113BFC309ED320;
ALTER TABLE tl_laasse10_user ADD CONSTRAINT FK_NEW_1720029621_30113BFC309ED320 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_user DROP FOREIGN KEY FK_NEW_1720029621_30113BFCEC0D3147;
ALTER TABLE tl_laasse10_user ADD CONSTRAINT FK_NEW_1720029621_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laasse10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_session DROP FOREIGN KEY FK_NEW_1720029621_24AA78C530E79035;
ALTER TABLE tl_laasse10_session ADD CONSTRAINT FK_NEW_1720029621_24AA78C530E79035 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment DROP FOREIGN KEY FK_NEW_1720029621_89093BF758092FB;
ALTER TABLE tl_laasse10_assessment ADD CONSTRAINT FK_NEW_1720029621_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;
								
-- patch20161130.sql
--  LDEV-3801 keep the time limit start time in DB
ALTER TABLE tl_laasse10_assessment_result ADD COLUMN time_limit_launched_date datetime;

-- LDEV-4180
ALTER TABLE tl_laasse10_assessment MODIFY COLUMN allow_question_feedback tinyint(1),
								   MODIFY COLUMN allow_overall_feedback tinyint(1),
								   MODIFY COLUMN allow_right_answers tinyint(1),
								   MODIFY COLUMN allow_wrong_answers tinyint(1),
								   MODIFY COLUMN allow_grades_after_attempt tinyint(1),
								   MODIFY COLUMN allow_history_responses tinyint(1),
								   MODIFY COLUMN display_summary tinyint(1),
								   MODIFY COLUMN shuffled tinyint(1),
   								   MODIFY COLUMN attempt_completion_notify tinyint(1) DEFAULT 0,
   								   MODIFY COLUMN numbered tinyint(1) DEFAULT 1;

ALTER TABLE tl_laasse10_assessment_question MODIFY COLUMN shuffle tinyint(1),
   								   			MODIFY COLUMN case_sensitive tinyint(1),
   								   			MODIFY COLUMN correct_answer tinyint(1) DEFAULT 0,
   								   			MODIFY COLUMN allow_rich_editor tinyint(1) DEFAULT 0;
   								   			
ALTER TABLE tl_laasse10_question_reference MODIFY COLUMN random_question tinyint(1) DEFAULT 0;


UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laasse10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;