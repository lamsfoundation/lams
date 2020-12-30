SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170209.sql to patch20200626.sql
-- but 4.0 introduced other patches in between, so we officially say it is just 20190103 patch so 4.0 patches execute
-- It should upgrade this tool to version 3.1


-- LDEV-4271 Remove unnecessary fields
ALTER TABLE `tl_laasse10_assessment_question` DROP FOREIGN KEY `FK_NEW_1720029621_F52D1F93758092FB`;
ALTER TABLE `tl_laasse10_assessment_question` DROP COLUMN `create_by`, DROP INDEX `FK_NEW_1720029621_F52D1F93758092FB`;

ALTER TABLE `tl_laasse10_assessment_question` DROP COLUMN `create_date`;

UPDATE lams_tool SET tool_version='20170315' WHERE tool_signature='laasse10';



-- LDEV-4410 Add unique index to prevent duplicate result copied from leader
ALTER TABLE tl_laasse10_assessment_result ADD UNIQUE KEY UQ_tl_laasse10_assessment_result_4 (assessment_uid, user_uid, finish_date);

UPDATE lams_tool SET tool_version='20170822' WHERE tool_signature='laasse10';



-- LDEV-4438 Add unique constraint to prevent duplicate results with 'latest' flag being ON

--remove all duplicates that have identical pairs (assessment_uid, user_uid, latest) where latest=1 
DELETE a
FROM tl_laasse10_assessment_result as a, tl_laasse10_assessment_result as b
WHERE
          (a.assessment_uid   = b.assessment_uid OR a.assessment_uid IS NULL AND b.assessment_uid IS NULL)
      AND (a.user_uid = b.user_uid OR a.user_uid IS NULL AND b.user_uid IS NULL)
      AND (a.latest = 1 AND b.latest = 1)
      AND a.uid < b.uid;
      
--set up tl_laasse10_assessment_result table to properly handle constraint
UPDATE tl_laasse10_assessment_result SET latest=NULL WHERE latest='0';
ALTER TABLE tl_laasse10_assessment_result CHANGE COLUMN latest latest TINYINT(1) NULL DEFAULT NULL;

--add unique constraint
ALTER TABLE tl_laasse10_assessment_result ADD UNIQUE KEY UQ_tl_laasse10_assessment_result_5 (assessment_uid, user_uid, latest);

--remove unique constraint previously added in patch20170822.sql (LDEV-4410 Add unique index to prevent duplicate result copied from leader)
ALTER TABLE tl_laasse10_assessment_result DROP INDEX UQ_tl_laasse10_assessment_result_4;

UPDATE lams_tool SET tool_version='20171011' WHERE tool_signature='laasse10';



-----------------Put all sql statements below here-------------------------

-- LDEV-4451 add 'Enable Confidence levels' advanced setting
ALTER TABLE tl_laasse10_assessment ADD COLUMN enable_confidence_levels TINYINT(1) NOT NULL DEFAULT 0;

--add questions' hashes in order to be able to search for the similar questions with the same title and question
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN question_hash CHAR(40);
UPDATE tl_laasse10_assessment_question SET question_hash = SHA1(question);

--add confidence level property to tl_laasse10_question_result
ALTER TABLE tl_laasse10_question_result ADD COLUMN confidence_level int;

UPDATE lams_tool SET tool_version='20171016' WHERE tool_signature='laasse10';



-- LDEV-4490 Fix NULL ConfidenceLevel columns creating exceptions
UPDATE tl_laasse10_question_result SET confidence_level = 0 WHERE confidence_level IS NULL;

ALTER TABLE tl_laasse10_question_result
CHANGE COLUMN confidence_level confidence_level INT(11) NOT NULL DEFAULT 0;




-- LDEV-4540 Allow teachers to release correct and other groups' answers in monitor

ALTER TABLE tl_laasse10_assessment ADD COLUMN allow_disclose_answers TINYINT(1) DEFAULT 0 AFTER allow_overall_feedback;

ALTER TABLE tl_laasse10_assessment_question ADD COLUMN correct_answers_disclosed TINYINT(1) DEFAULT 0,
											ADD COLUMN groups_answers_disclosed TINYINT(1) DEFAULT 0;

											
											

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laasse10_assessment MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_overall_feedback MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY question MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY general_feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_correct MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_partially_correct MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_incorrect MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY question MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY option_string MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_result MODIFY answer_string MEDIUMTEXT;




-- LDEV-4582 Option to add prefix letter to assessment MCQ answers 
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN prefix_answers_with_letters TINYINT(1) NOT NULL DEFAULT 0;




-- LDEV-4813 Add a missing foreign key and index to speed up queries
ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid)
	REFERENCES tl_laasse10_question_option (uid) ON DELETE CASCADE ON UPDATE CASCADE;



-- LDEV-4813 Add missing indexes to speed up queries
ALTER TABLE tl_laasse10_assessment_result ADD INDEX (latest);
ALTER TABLE tl_laasse10_option_answer ADD INDEX (answer_boolean);



--LDEV-5002 Add question Etherpads
ALTER TABLE tl_laasse10_assessment ADD COLUMN question_etherpad_enabled TINYINT(1) DEFAULT 0 AFTER use_select_leader_tool_ouput;



--LDEV-5047 Make numeric option more accurate as with large numbers it performs rounding
ALTER TABLE tl_laasse10_question_option MODIFY COLUMN option_float DOUBLE;



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='laasse10';



-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;