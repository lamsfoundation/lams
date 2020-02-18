SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- LDEV-4951 Replace MCQ with Asssessment

-- transform McContent to Assessment
ALTER TABLE `tl_laasse10_assessment` ADD COLUMN `tmp_mcq_uid` BIGINT;
ALTER TABLE tl_laasse10_assessment ADD INDEX (tmp_mcq_uid);

INSERT INTO tl_laasse10_assessment (`create_date`, `update_date`, `submission_deadline`,`create_by`,`title`,`time_limit`,
	`attempts_allowed`,`passing_mark`,`instructions`,`define_later`,`content_id`,`allow_question_feedback`,`allow_overall_feedback`,
	`allow_disclose_answers`,`allow_right_answers`,`allow_wrong_answers`,`allow_grades_after_attempt`,`allow_history_responses`,
	`display_summary`,`questions_per_page`,`shuffled`,`attempt_completion_notify`,`reflect_on_activity`,`reflect_instructions`,
	`numbered`,`use_select_leader_tool_ouput`,`enable_confidence_levels`,`confidence_levels_type`, `tmp_mcq_uid`)
SELECT `creation_date`,`update_date`,`submission_deadline`,NULL,`title`,0,
	!`retries`,`pass_mark`,`instructions`,`define_later`,`content_id`,1,`display_feedback_only`,
	1,0,0,`showMarks`,0,
	`displayAnswers`, `questions_sequenced`,`randomize`,0,`reflect`,`reflectionSubject`,
	`prefix_answers_with_letters`,`use_select_leader_tool_ouput`,`enable_confidence_levels`,1, uid
    FROM tl_lamc11_content;   
-- MCQ's `show_report` property was seemingly  abandoned by tool itself

-- transform McSession to AssessmentSession
ALTER TABLE `tl_laasse10_session` ADD COLUMN `tmp_mcq_session_uid` BIGINT;
ALTER TABLE tl_laasse10_session ADD INDEX (tmp_mcq_session_uid);
    
INSERT INTO tl_laasse10_session(`session_end_date`,`session_start_date`,`status`,`assessment_uid`,`session_id`,`session_name`,`group_leader_uid`,tmp_mcq_session_uid)
SELECT ses.`session_end_date`,ses.`session_start_date`,ses.`session_status`="COMPLETED",asse.`uid`,ses.`mc_session_id`,ses.`session_name`,ses.`mc_group_leader_uid`,ses.uid
    FROM tl_lamc11_session as ses
    JOIN tl_laasse10_assessment AS asse ON asse.tmp_mcq_uid = ses.mc_content_id; 

    
-- transform McQueUser to AssessmentUser
ALTER TABLE `tl_laasse10_user` ADD COLUMN `tmp_mcq_user_uid` BIGINT;
ALTER TABLE tl_laasse10_user ADD INDEX (tmp_mcq_user_uid);

INSERT INTO tl_laasse10_user(`user_id`,`last_name`,`first_name`,`login_name`,`session_finished`,`session_uid`,`assessment_uid`,tmp_mcq_user_uid)
SELECT usr.`que_usr_id`,commonUsr.`last_name`,commonUsr.`first_name`,usr.`username`,usr.`responseFinalised`,ses.uid,ses.assessment_uid,usr.uid
    FROM tl_lamc11_que_usr as usr
    JOIN lams_user AS commonUsr ON usr.que_usr_id = commonUsr.user_id
    JOIN tl_laasse10_session AS ses ON usr.mc_session_id = ses.tmp_mcq_session_uid; 
--  `viewSummaryRequested` tinyint(1) NOT NULL DEFAULT '0',
--  `last_attempt_total_mark` int(11) DEFAULT NULL,
--  `number_attempts` int(11) DEFAULT '0',

-- change references from MCQ users to Assessment ones
UPDATE tl_laasse10_session AS ses, tl_laasse10_user AS usr
    SET ses.group_leader_uid = usr.uid
    WHERE ses.group_leader_uid = usr.tmp_mcq_user_uid AND ses.tmp_mcq_session_uid IS NOT NULL;
    
-- transform McQueContent to AssessmentQuestion
INSERT INTO tl_laasse10_assessment_question(`uid`,`assessment_uid`)
SELECT mcQuestion.`uid`,asses.`uid`
    FROM tl_lamc11_que_content as mcQuestion
    JOIN tl_laasse10_assessment AS asses ON mcQuestion.mc_content_id = asses.tmp_mcq_uid; 

UPDATE lams_qb_tool_question AS qbToolQuestion, tl_lamc11_content AS mcContent, tl_laasse10_assessment as assessment
    SET qbToolQuestion.tool_content_id = assessment.content_id
    WHERE mcContent.content_id = qbToolQuestion.tool_content_id AND assessment.tmp_mcq_uid=mcContent.uid;

ALTER TABLE `tl_laasse10_question_reference` ADD COLUMN `tmp_mcq` TINYINT(1);
INSERT INTO tl_laasse10_question_reference(`question_uid`,`sequence_id`,`default_grade`,`random_question`,`assessment_uid`,tmp_mcq)
SELECT mcQuestion.`uid`,qbToolQuestion.display_order,1,0,asses.`uid`,1
    FROM tl_lamc11_que_content as mcQuestion
    JOIN tl_laasse10_assessment AS asses ON mcQuestion.mc_content_id = asses.tmp_mcq_uid
    JOIN lams_qb_tool_question AS qbToolQuestion ON mcQuestion.uid = qbToolQuestion.tool_question_uid; 


-- transform McUsrAttempt to AssessmentResult
INSERT INTO tl_laasse10_assessment_result(`assessment_uid`,`start_date`,`finish_date`,`user_uid`,`session_id`,`grade`,`latest`,`time_limit_launched_date`)
SELECT session.`assessment_uid`, mcAttempt.`attempt_time`, IF(mcUser.responseFinalised,mcAttempt.`attempt_time`,NULL), assessmentUser.uid, session.session_id,
	IF(mcUser.last_attempt_total_mark IS NULL,0,mcUser.last_attempt_total_mark), 1, NULL
    FROM tl_lamc11_usr_attempt as mcAttempt
    JOIN tl_laasse10_user as assessmentUser ON mcAttempt.que_usr_id = assessmentUser.tmp_mcq_user_uid
    JOIN tl_lamc11_que_usr as mcUser ON mcAttempt.que_usr_id = mcUser.uid
    JOIN tl_laasse10_session as session ON assessmentUser.session_uid = session.uid
    GROUP BY mcAttempt.que_usr_id;
    
-- calculate AssessmentResult'maxMark (it's a different query, as user might have not answered all questions)
UPDATE tl_laasse10_assessment_result AS result
    JOIN (SELECT assessment_uid, SUM(default_grade) maxGrade FROM tl_laasse10_question_reference WHERE tmp_mcq=1 GROUP BY assessment_uid) AS maxGrade
	ON result.assessment_uid = maxGrade.assessment_uid 
    SET result.maximum_grade = maxGrade.maxGrade;

INSERT INTO tl_laasse10_question_result(`uid`,`result_uid`,`answer_float`,`answer_boolean`,`mark`,
	`penalty`,`finish_date`,`max_mark`,`confidence_level`)
SELECT mcAttempt.`uid`,assmResult.uid,0,0,mcAttempt.`mark`,0,IF(mcUser.responseFinalised,mcAttempt.`attempt_time`,NULL),qbQuestion.`max_mark`,mcAttempt.`confidence_level`
    FROM tl_lamc11_usr_attempt as mcAttempt
    JOIN lams_qb_tool_answer AS qbToolAnswer ON mcAttempt.uid = qbToolAnswer.answer_uid
    JOIN lams_qb_tool_question AS qbToolQuestion ON qbToolAnswer.tool_question_uid = qbToolQuestion.tool_question_uid
    JOIN lams_qb_question AS qbQuestion ON qbToolQuestion.qb_question_uid = qbQuestion.uid
    
    JOIN tl_laasse10_user as assmUser ON mcAttempt.que_usr_id = assmUser.tmp_mcq_user_uid
    JOIN tl_laasse10_assessment_result AS assmResult ON assmUser.uid = assmResult.user_uid
    
    JOIN tl_lamc11_que_usr as mcUser ON mcAttempt.que_usr_id = mcUser.uid;

INSERT INTO tl_laasse10_option_answer(`question_result_uid`,`question_option_uid`,`answer_boolean`,`answer_int`)
SELECT mcAttempt.`uid`,qbOption.uid,qbToolAnswer.qb_option_uid=qbOption.uid,-1
    FROM tl_lamc11_usr_attempt as mcAttempt
    
    JOIN lams_qb_tool_answer AS qbToolAnswer ON mcAttempt.uid = qbToolAnswer.answer_uid
    JOIN lams_qb_tool_question AS qbToolQuestion ON qbToolAnswer.tool_question_uid = qbToolQuestion.tool_question_uid
    JOIN lams_qb_question AS qbQuestion ON qbToolQuestion.qb_question_uid = qbQuestion.uid
    
    LEFT JOIN lams_qb_option AS qbOption ON qbQuestion.uid = qbOption.qb_question_uid;

-- take care about lams_tool_content
UPDATE lams_tool_content AS toolContent
JOIN `lams_tool` as mcqTool ON mcqTool.tool_id=toolContent.tool_id AND mcqTool.`tool_signature` = 'lamc11'
JOIN `lams_tool` as assmTool ON assmTool.`tool_signature` = 'laasse10'
SET toolContent.tool_id = assmTool.tool_id;

-- take care about ActivityEvaluation
UPDATE lams_activity_evaluation AS evaluation
JOIN lams_learning_activity AS mcqActivity ON mcqActivity.activity_id=evaluation.activity_id
JOIN `lams_tool` as mcqTool ON mcqTool.tool_id=mcqActivity.tool_id AND mcqTool.`tool_signature` = 'lamc11'
SET evaluation.tool_output_definition = 'learner.total.score'
WHERE evaluation.tool_output_definition='learner.mark';
    
-- migrate lams_learning_activity
UPDATE lams_learning_activity AS activity
JOIN `lams_tool` as mcqTool ON mcqTool.tool_id=activity.tool_id AND mcqTool.`tool_signature` = 'lamc11'
JOIN `lams_tool` as assmTool ON assmTool.`tool_signature` = 'laasse10'
SET activity.learning_library_id = assmTool.learning_library_id, 
	activity.tool_id = assmTool.tool_id,
    activity.library_activity_ui_image = 'tool/laasse10/images/icon_assessment.svg';
    
-- migrate notebook entries
UPDATE lams_notebook_entry
SET external_signature = 'laasse10'
WHERE external_signature='lamc11';

--remove temporary columns
ALTER TABLE tl_laasse10_assessment DROP COLUMN tmp_mcq_uid;
ALTER TABLE tl_laasse10_session DROP COLUMN tmp_mcq_session_uid;
ALTER TABLE tl_laasse10_user DROP COLUMN tmp_mcq_user_uid;
ALTER TABLE tl_laasse10_question_reference DROP COLUMN tmp_mcq;

--remove all traces of MCQ tool
DROP TABLE tl_lamc11_content,
           tl_lamc11_session,
           tl_lamc11_que_usr,
           tl_lamc11_que_content,
           tl_lamc11_configuration,
           tl_lamc11_usr_attempt;
DELETE library FROM `lams_learning_library` library 
    JOIN lams_tool AS tool ON tool.learning_library_id = library.learning_library_id
    WHERE (tool.`tool_signature` = 'lamc11');
DELETE FROM `lams_tool` WHERE (`tool_signature` = 'lamc11');
DELETE patch FROM `patches` patch where system_name='lamc11';

UPDATE lams_tool SET tool_version='20200120' WHERE tool_signature='laasse10';
        
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;

