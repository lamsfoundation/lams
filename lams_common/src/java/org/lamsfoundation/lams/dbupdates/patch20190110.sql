-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4746 Create Question Bank table structure and migrate existing data

-- Create QB question table
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT, 
							   `local` TINYINT(1) NOT NULL DEFAULT 0,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `create_date` DATETIME NOT NULL DEFAULT NOW(),
							   `name` TEXT NOT NULL,
							   `description` TEXT,
							   `max_mark` INT,
							   `feedback` TEXT,
							   `penalty_factor` float DEFAULT 0,
							   `answer_required` TINYINT(1) DEFAULT 0,
							   `multiple_answers_allowed` TINYINT(1) DEFAULT 0,
							   `incorrect_answer_nullifies_mark` TINYINT(1) DEFAULT 0,
							   `feedback_on_correct` TEXT,
							   `feedback_on_partially_correct` TEXT,
							   `feedback_on_incorrect` TEXT,
							   `shuffle` TINYINT(1) DEFAULT 0,
							   `prefix_answers_with_letters` TINYINT(1) DEFAULT 0,
							   `case_sensitive` TINYINT(1) DEFAULT 0,
							   `correct_answer` TINYINT(1) DEFAULT 0,
							   `allow_rich_editor` TINYINT(1) DEFAULT 0,
							   `max_words_limit` int(11) DEFAULT 0,
							   `min_words_limit` int(11) DEFAULT 0,
							   `hedging_justification_enabled` TINYINT(1) DEFAULT 0,
							   `tmp_question_id` BIGINT,
							   PRIMARY KEY (uid),
							   INDEX (tmp_question_id),
							   CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version),
							   INDEX (`local`));
							   
-- Create a question table from which tools' questions will inherit						   
CREATE TABLE lams_qb_tool_question (`tool_question_uid` BIGINT AUTO_INCREMENT,
									`qb_question_uid` BIGINT NOT NULL,
									`tool_content_id` BIGINT NOT NULL,
									`display_order` TINYINT NOT NULL DEFAULT 1,
									PRIMARY KEY (tool_question_uid),
									INDEX (tool_content_id),
									CONSTRAINT FK_lams_qb_tool_question_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE);
-- create Question Bank option
CREATE TABLE lams_qb_option (`uid` BIGINT AUTO_INCREMENT,
							 `qb_question_uid` BIGINT NOT NULL,
							 `display_order` TINYINT NOT NULL DEFAULT 1,
							 `name` TEXT,
							 `correct` TINYINT(1) NOT NULL DEFAULT 0,
							 `matching_pair` TEXT,
							 `numerical_option` float DEFAULT 0,
							 `max_mark` float DEFAULT 0,
							 `accepted_error` float DEFAULT 0,
							 `feedback` TEXT,
							 PRIMARY KEY (uid),
							 INDEX (display_order),
							 CONSTRAINT FK_lams_qb_option_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- create Question Bank question unit (used by numerical type of questions only)
CREATE TABLE lams_qb_question_unit (`uid` BIGINT AUTO_INCREMENT,
									`qb_question_uid` BIGINT NOT NULL,
									`display_order` TINYINT NOT NULL DEFAULT 1,
									`multiplier` float DEFAULT 0,
									`name` varchar(255),
									PRIMARY KEY (uid),
									CONSTRAINT FK_lams_qb_question_unit_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- create an answer table from which tools' answers will inherit
CREATE TABLE lams_qb_tool_answer (`answer_uid` BIGINT AUTO_INCREMENT,
								  `tool_question_uid` BIGINT NOT NULL,
								  `qb_option_uid` BIGINT DEFAULT NULL,
								  PRIMARY KEY (answer_uid),
								  CONSTRAINT FK_lams_qb_tool_answer_1 FOREIGN KEY (tool_question_uid) 
								  	REFERENCES lams_qb_tool_question (tool_question_uid) ON DELETE CASCADE ON UPDATE CASCADE,
								  CONSTRAINT FK_lams_qb_tool_answer_2 FOREIGN KEY (qb_option_uid)
								  	REFERENCES lams_qb_option (uid)  ON DELETE CASCADE ON UPDATE CASCADE);
							 
-- Migrate MCQ question into Question Bank question
-- This part of patch should be in MCQ, but it must be run after lams_qb_question is created and it must not run in parallel with other tools' patches,
-- so the safest solution is to place it here
			
-- default value for a concat result is 1024 characters, which can be too little for multiple concatenated answers
-- we choose a value big enough to accept anything
SET group_concat_max_len = 100000;


-- MULTIPLE CHOICE (MCQ)


-- create a mapping of MCQ question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content MEDIUMTEXT)
	AS SELECT q.uid AS question_uid,
			  GROUP_CONCAT(TRIM(question), TRIM(mc_que_option_text) ORDER BY displayOrder) AS content		  
	FROM tl_lamc11_que_content AS q
	JOIN tl_lamc11_options_content AS o ON q.uid = o.mc_que_content_id
	GROUP BY q.uid;

-- to speed up matching; index has almost maximum allowed length
ALTER TABLE tmp_question ADD INDEX (content(500));

-- create a mapping of MCQ question UID -> UID of one of MCQ questions which holds the same content
CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
								 target_uid BIGINT)
	AS SELECT q.question_uid, merged.question_uid AS target_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q USING (content);
	
ALTER TABLE tmp_question_match ADD INDEX (target_uid);

									
-- fill Question Bank question table with unique questions, with manually incremented question ID
SET @question_id = (SELECT IF(MAX(question_id) IS NULL, 0, MAX(question_id)) FROM lams_qb_question);
							   
INSERT INTO lams_qb_question (uid, `local`, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id) 
	SELECT NULL, 0, 1, @question_id:=@question_id + 1, 1, IFNULL(c.creation_date, NOW()),
		'MCQ question', mcq.question, IFNULL(mcq.max_mark, 1), mcq.feedback, q.target_uid
	FROM (SELECT uid,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM question))) AS question,
				 mark AS max_mark,
				 IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback,
				 mc_content_id
		  FROM tl_lamc11_que_content) AS mcq
	JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
		ON mcq.uid = q.target_uid
	JOIN tl_lamc11_content AS c
		ON mcq.mc_content_id = c.uid;

-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.uid, c.content_id, mcq.display_order
	FROM lams_qb_question AS qb
	JOIN tmp_question_match AS q
		ON qb.tmp_question_id = q.target_uid
	JOIN tl_lamc11_que_content AS mcq
		ON q.question_uid = mcq.uid
	JOIN tl_lamc11_content AS c
		ON mcq.mc_content_id = c.uid;

-- remove columns from MCQ which are duplicated in Question Bank
ALTER TABLE tl_lamc11_que_content DROP COLUMN question,
								  DROP COLUMN mark,
								  DROP COLUMN display_order,
								  DROP COLUMN feedback;
								  
-- fill table with options matching unique QB questions inserted above
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct)
	SELECT q.uid, o.displayOrder, TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.mc_que_option_text))), o.correct_option
	FROM tl_lamc11_options_content AS o
	JOIN lams_qb_question AS q
		ON o.mc_que_content_id = q.tmp_question_id
	ORDER BY  o.mc_que_content_id, o.displayOrder;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_2, 
								  DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3;
	
-- rewrite references from MCQ options to QB options
UPDATE tl_lamc11_usr_attempt AS ua, tl_lamc11_options_content AS mco, lams_qb_tool_question AS tq, lams_qb_option AS qo
	SET ua.mc_que_option_id = qo.uid
	WHERE mco.displayOrder = qo.display_order
		AND ua.mc_que_option_id = mco.uid
		AND qo.qb_question_uid = tq.qb_question_uid
		AND mco.mc_que_content_id = tq.tool_question_uid;

-- prepare for answer inheritance		
INSERT INTO lams_qb_tool_answer
	SELECT uid, mc_que_content_id, mc_que_option_id FROM tl_lamc11_usr_attempt;

-- clean up
ALTER TABLE tl_lamc11_usr_attempt DROP INDEX attempt_unique_index,
								  DROP COLUMN mc_que_content_id,
								  DROP COLUMN mc_que_option_id;
								  
DROP TABLE tl_lamc11_options_content;

-- prepare for next tool migration
DELETE FROM tmp_question;
DELETE FROM tmp_question_match;


-- SCRATCHIE


-- shift Scratchie question UIDs by offset equal to existing UIDs of MCQ in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
UPDATE tl_lascrt11_scratchie_item SET uid = uid + @max_tool_question_id ORDER BY uid DESC;
-- UPDATE tl_lascrt11_scratchie_answer SET scratchie_item_uid = scratchie_item_uid + @max_tool_question_id ORDER BY scratchie_item_uid DESC;

-- create a mapping of Scratchie question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
INSERT INTO tmp_question
	SELECT q.uid,
		   GROUP_CONCAT(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM q.description))),
		   				TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.description))) ORDER BY o.order_id)
	FROM tl_lascrt11_scratchie_item AS q
	JOIN tl_lascrt11_scratchie_answer AS o
		ON q.uid = o.scratchie_item_uid
	GROUP BY q.uid;
	
-- create a similar mapping for existing questions in QB
CREATE TABLE tmp_qb_question (question_uid BIGINT PRIMARY KEY,
						      content MEDIUMTEXT)
	AS SELECT q.uid AS question_uid,
			  GROUP_CONCAT(q.description, o.name ORDER BY o.display_order) AS content
	FROM lams_qb_question AS q
	JOIN lams_qb_option AS o
		ON q.uid = o.qb_question_uid
	GROUP BY q.uid;
	
ALTER TABLE tmp_qb_question ADD INDEX (content(500));

-- create a mapping of Scratchie question UID -> UID of one of Scratchie questions which holds the same content
INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content)
	LEFT JOIN tmp_qb_question AS qb
		USING (content)
	WHERE qb.question_uid IS NULL;

-- reset column for matching QB questions with Scratchie questions
UPDATE lams_qb_question SET tmp_question_id = -1;
	
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question (uid, `local`, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id)
	SELECT NULL, 0, 1, @question_id:=@question_id + 1, 1, sq.create_date, 
		sq.question, sq.description, NULL, NULL, q.target_uid
	FROM (SELECT uid,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM title))) AS question,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM description))) AS description,
				 create_date
		  FROM tl_lascrt11_scratchie_item) AS sq
	JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
	ON sq.uid = q.target_uid;
	
-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.uid, s.content_id, sq.order_id
	FROM lams_qb_question AS qb
	JOIN tmp_question_match AS q
		ON qb.tmp_question_id = q.target_uid
	JOIN tl_lascrt11_scratchie_item AS sq
		ON q.question_uid = sq.uid
	JOIN tl_lascrt11_scratchie AS s
		ON sq.scratchie_uid = s.uid;
	
-- set up references to QB question UIDs for existing questions
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.question_uid, s.content_id, sq.order_id
	FROM tmp_question AS q
	JOIN tmp_qb_question qb
		USING (content)
	JOIN tl_lascrt11_scratchie_item AS sq
		ON q.question_uid = sq.uid
	JOIN tl_lascrt11_scratchie AS s
		ON sq.scratchie_uid = s.uid;
		
-- delete obsolete columns
ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93EC0D3147, 
									   DROP COLUMN title,
								  	   DROP COLUMN description,
								   	   DROP COLUMN create_date,
								   	   DROP COLUMN create_by_author,
								   	   DROP COLUMN session_uid,
								   	   DROP COLUMN order_id;

-- some Scratchie options can be ordered from 0, some from 1
-- shift ones ordered from 0 by +1 to match the other group								   	  
CREATE TABLE tmp_scratchie_answer
SELECT scratchie_item_uid FROM tl_lascrt11_scratchie_answer WHERE order_id = 0 AND scratchie_item_uid IS NOT NULL;

ALTER TABLE tmp_scratchie_answer ADD PRIMARY KEY (scratchie_item_uid);
								   	   
UPDATE tl_lascrt11_scratchie_answer AS sa
SET order_id = order_id + 1
WHERE EXISTS
	(SELECT 1 FROM tmp_scratchie_answer WHERE scratchie_item_uid = sa.scratchie_item_uid);
		
DROP TABLE tmp_scratchie_answer;


-- fill table with options matching unique QB questions inserted above		  
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct)
	SELECT q.uid, o.order_id, TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.description))), o.correct
	FROM tl_lascrt11_scratchie_answer AS o
	JOIN lams_qb_question AS q
		ON o.scratchie_item_uid = q.tmp_question_id
	ORDER BY o.scratchie_item_uid, o.order_id;
	

ALTER TABLE tl_lascrt11_answer_log ADD COLUMN scratchie_item_uid BIGINT;

-- shift Scratchie answer UIDs by offset equal to existing UIDs of MCQ answers in lams_qb_tool_answer 								   
SET @max_answer_uid = (SELECT MAX(answer_uid) FROM lams_qb_tool_answer);
UPDATE tl_lascrt11_answer_log SET uid = uid + @max_answer_uid ORDER BY uid DESC;

ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DFE,
								   DROP KEY FK_NEW_lascrt11_30113BFC309ED321;

-- rewrite references from Scratchie options to QB options
UPDATE tl_lascrt11_answer_log AS sl, tl_lascrt11_scratchie_answer AS sa, lams_qb_tool_question AS tq, lams_qb_option AS qo
	SET sl.scratchie_answer_uid = qo.uid,
		sl.scratchie_item_uid = tq.tool_question_uid
	WHERE   sa.order_id = qo.display_order
		AND sl.scratchie_answer_uid = sa.uid
		AND qo.qb_question_uid = tq.qb_question_uid
		AND sa.scratchie_item_uid = tq.tool_question_uid;
		
-- prepare for answer inheritance
INSERT INTO lams_qb_tool_answer
	SELECT uid, scratchie_item_uid, scratchie_answer_uid FROM tl_lascrt11_answer_log;


-- cleanup
ALTER TABLE tl_lascrt11_answer_log DROP COLUMN scratchie_item_uid,
								   DROP COLUMN scratchie_answer_uid;
								   
DROP TABLE tl_lascrt11_scratchie_answer;
		   
-- prepare for next tool migration
DELETE FROM tmp_question;
DELETE FROM tmp_question_match;
DELETE FROM tmp_qb_question;


-- ASSESSMENT


-- shift Assessment question UIDs by offset equal to existing UIDs of MCQ adn Scratchie in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
UPDATE tl_laasse10_assessment_question SET uid = uid + @max_tool_question_id ORDER BY uid DESC;

-- first, process questions with question_type=1 (as MCQ and Scratchie have only this type of questions)

-- create a mapping of Assessment question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
INSERT INTO tmp_question
	SELECT q.uid,
		   GROUP_CONCAT(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM q.question))), 
		   				TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.option_string))) ORDER BY o.sequence_id)
	FROM tl_laasse10_assessment_question AS q
	JOIN tl_laasse10_question_option AS o
		ON q.uid = o.question_uid
	WHERE q.question_type = 1
	GROUP BY q.uid;
	
-- create a similar mapping for existing questions in QB
INSERT INTO tmp_qb_question
	SELECT q.uid AS question_uid,
			  GROUP_CONCAT(q.description, o.name ORDER BY o.display_order) AS content
	FROM lams_qb_question AS q
	JOIN lams_qb_option AS o
		ON q.uid = o.qb_question_uid
	GROUP BY q.uid;
	
-- set up references to QB question UIDs for existing questions
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.question_uid, assess.content_id, aq.sequence_id
	FROM tmp_question AS q
	JOIN tmp_qb_question qb
		USING (content)
	JOIN tl_laasse10_assessment_question AS aq
		ON q.question_uid = aq.uid
	JOIN tl_laasse10_assessment AS assess
		ON aq.assessment_uid = assess.uid;

-- create a mapping of Assessment question UID -> UID of one of Assessment questions which holds the same content
INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content)
	LEFT JOIN tmp_qb_question AS qb
		USING (content)
	WHERE qb.question_uid IS NULL;	
	
	
-- second, process questions with all other question_type

DELETE FROM tmp_question;

INSERT INTO tmp_question
	SELECT q.uid,
		   GROUP_CONCAT(q.question_type, IFNULL(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM q.question))), ''), q.correct_answer,
						IFNULL(CONCAT(IFNULL(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.question))), ''), 
						IFNULL(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.option_string))), ''), o.option_float, o.correct), '') 
						ORDER BY o.sequence_id)
	FROM tl_laasse10_assessment_question AS q
	LEFT JOIN tl_laasse10_question_option AS o
		ON q.uid = o.question_uid
	WHERE q.question_type != 1
	GROUP BY q.uid;
	
INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content);
	
-- reset column for matching QB questions with Assessment questions
UPDATE lams_qb_question SET tmp_question_id = -1;

	
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question SELECT NULL, 0, aq.question_type, @question_id:=@question_id + 1, 1, IFNULL(assessment.create_date, NOW()), 
				IFNULL(aq.question, ''), aq.description, IFNULL(aq.max_mark, 1), aq.feedback, aq.penalty_factor, aq.answer_required,
				aq.multiple_answers_allowed, aq.incorrect_answer_nullifies_mark, aq.feedback_on_correct, aq.feedback_on_partially_correct,
				aq.feedback_on_incorrect, aq.shuffle, aq.prefix_answers_with_letters, aq.case_sensitive, aq.correct_answer,
				aq.allow_rich_editor, aq.max_words_limit, aq.min_words_limit, aq.hedging_justification_enabled, q.target_uid
	FROM (SELECT uid,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM title))) AS question,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM question))) AS description,
				 default_grade AS max_mark,
				 question_type,
				 IF(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM general_feedback)))= '', 
				 	NULL, TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM general_feedback)))) AS feedback,
				 penalty_factor,
				 answer_required,
				 multiple_answers_allowed,
				 incorrect_answer_nullifies_mark,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_correct))) AS feedback_on_correct,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_partially_correct))) AS feedback_on_partially_correct,
				 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_incorrect))) AS feedback_on_incorrect,
				 shuffle,
				 prefix_answers_with_letters,
				 case_sensitive,
				 correct_answer,
				 allow_rich_editor,
				 max_words_limit,
				 min_words_limit,
				 hedging_justification_enabled,
				 assessment_uid
		  FROM tl_laasse10_assessment_question) AS aq
	JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
		ON aq.uid = q.target_uid
	JOIN tl_laasse10_assessment AS assessment
		ON aq.assessment_uid = assessment.uid;
	
-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.uid, assess.content_id, aq.sequence_id
	FROM lams_qb_question AS qb
	JOIN tmp_question_match AS q
		ON qb.tmp_question_id = q.target_uid
	JOIN tl_laasse10_assessment_question AS aq
		ON q.question_uid = aq.uid
	JOIN tl_laasse10_assessment AS assess
		ON aq.assessment_uid = assess.uid;
	
-- delete obsolete columns
ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93EC0D3147, 
									   DROP COLUMN title,
								  	   DROP COLUMN question,
								  	   DROP COLUMN question_type,
								  	   DROP COLUMN default_grade,
								  	   DROP COLUMN feedback,
								  	   DROP COLUMN general_feedback,
									   DROP COLUMN penalty_factor,
									   DROP COLUMN answer_required,
									   DROP COLUMN multiple_answers_allowed,
									   DROP COLUMN incorrect_answer_nullifies_mark,
									   DROP COLUMN feedback_on_correct,
									   DROP COLUMN feedback_on_partially_correct,
									   DROP COLUMN feedback_on_incorrect,
									   DROP COLUMN shuffle,
									   DROP COLUMN prefix_answers_with_letters,
									   DROP COLUMN case_sensitive,
									   DROP COLUMN correct_answer,
									   DROP COLUMN allow_rich_editor,
									   DROP COLUMN max_words_limit,
									   DROP COLUMN min_words_limit,  
									   DROP COLUMN hedging_justification_enabled,
								   	   DROP COLUMN session_uid,
								   	   DROP COLUMN sequence_id;


-- fill table with options matching unique QB questions inserted above		  
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct, matching_pair, numerical_option, max_mark, accepted_error, feedback)
	SELECT q.uid, o.sequence_id, IFNULL(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM o.option_string))), ''), o.correct, o.question, o.option_float,
		   o.grade, o.accepted_error, o.feedback
	FROM tl_laasse10_question_option AS o
	JOIN lams_qb_question AS q
		ON o.question_uid = q.tmp_question_id
	ORDER BY o.question_uid, o.sequence_id;
	
	
-- fill table with units matching unique QB questions inserted above
INSERT INTO lams_qb_question_unit (qb_question_uid, display_order, multiplier, name)
	SELECT q.uid, u.sequence_id, u.multiplier, u.unit
	FROM tl_laasse10_assessment_unit AS u
	JOIN lams_qb_question AS q
		ON u.question_uid = q.tmp_question_id
	ORDER BY u.question_uid, u.sequence_id;


-- shift Assessment answer UIDs by offset equal to existing UIDs of MCQ and Assessment answers in lams_qb_tool_answer 								   
SET @max_answer_uid = (SELECT MAX(answer_uid) FROM lams_qb_tool_answer);
UPDATE tl_laasse10_question_result SET uid = uid + @max_answer_uid ORDER BY uid DESC;

ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_NEW_1720029621_693580A438BF8DFE;

-- rewrite references from Assessment options to QB options
UPDATE tl_laasse10_question_result AS sl, tl_laasse10_question_option AS o, lams_qb_tool_question AS tq, lams_qb_option AS qo
	SET sl.submitted_option_uid = qo.uid,
		sl.assessment_question_uid = tq.tool_question_uid
	WHERE   o.sequence_id = qo.display_order
		AND sl.submitted_option_uid = o.uid
		AND qo.qb_question_uid = tq.qb_question_uid
		AND o.question_uid = tq.tool_question_uid;
		
-- prepare for answer inheritance
INSERT INTO lams_qb_tool_answer
	SELECT uid, assessment_question_uid, submitted_option_uid FROM tl_laasse10_question_result;


-- cleanup
ALTER TABLE lams_qb_question DROP COLUMN tmp_question_id;
ALTER TABLE tl_laasse10_question_result DROP COLUMN assessment_question_uid,
								   DROP COLUMN submitted_option_uid;
								   
DROP TABLE tl_laasse10_question_option,
		   tl_laasse10_assessment_unit,
		   tmp_question,
		   tmp_question_match,
		   tmp_qb_question;
		   
		   
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;