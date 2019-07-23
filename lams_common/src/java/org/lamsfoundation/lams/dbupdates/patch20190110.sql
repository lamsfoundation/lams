-- LDEV-4746 Create Question Bank table structure and migrate existing data

-- AUTO COMMIT stays ON because there is so many ALTER TABLE statements which are committed immediately anyway, that it does not make a difference

-- Create QB question table
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `create_date` DATETIME NOT NULL DEFAULT NOW(),
							   `content_folder_id` char(36),
							   `name` TEXT,
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
							   CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version));
							   
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

-- remove characters that prevent detecting identical questions
UPDATE tl_lamc11_que_content SET `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
UPDATE tl_lamc11_options_content SET `mc_que_option_text` = TRIM(REPLACE(REPLACE(REPLACE(mc_que_option_text, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));

-- create a mapping of MCQ question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
-- Also remove all whitespace just for less demanding matching
CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content MEDIUMTEXT)
	AS SELECT q.uid AS question_uid,
			  REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(question, mc_que_option_text ORDER BY displayOrder))
			  						  COLLATE utf8mb4_0900_ai_ci, 
			  				  ' ', ''),
			  		 '\t', ''), 
			  '&nbsp;', '') AS content		  
	FROM tl_lamc11_que_content AS q
	JOIN tl_lamc11_options_content AS o ON q.uid = o.mc_que_content_id
	GROUP BY q.uid;

-- to speed up matching; index has almost maximum allowed length
ALTER TABLE tmp_question ADD INDEX (content(500));

-- create a mapping of MCQ question UID -> UID of one of MCQ questions which holds the same content
CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
								 target_uid BIGINT)
	SELECT q.question_uid, merged.question_uid AS target_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q USING (content)
	GROUP BY q.question_uid;
	
ALTER TABLE tmp_question_match ADD INDEX (target_uid);

									
-- fill Question Bank question table with unique questions, with manually incremented question ID
SET @question_id = (SELECT IF(MAX(question_id) IS NULL, 0, MAX(question_id)) FROM lams_qb_question);
							   
INSERT INTO lams_qb_question (uid, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id) 
	SELECT NULL, 1, @question_id:=@question_id + 1, 1, IFNULL(c.creation_date, NOW()),
		SUBSTRING(TRIM(REPLACE(REPLACE(strip_tags(mcq.question) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', '')), 1, 80),
		mcq.question, IFNULL(mcq.max_mark, 1), mcq.feedback, q.target_uid
	FROM (SELECT uid,
				 question AS question,
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
	SELECT q.uid, o.displayOrder, o.mc_que_option_text, o.correct_option
	FROM tl_lamc11_options_content AS o
	JOIN lams_qb_question AS q
		ON o.mc_que_content_id = q.tmp_question_id
	ORDER BY  o.mc_que_content_id, o.displayOrder;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_2, 
								  DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3,
								  DROP INDEX attempt_unique_index;

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
ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN mc_que_content_id,
								  DROP COLUMN mc_que_option_id;
								  
DROP TABLE tl_lamc11_options_content;

-- prepare for next tool migration, recreate tables
DROP TABLE tmp_question;
DROP TABLE tmp_question_match;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content MEDIUMTEXT);
ALTER TABLE tmp_question ADD INDEX (content(500));

CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
								 target_uid BIGINT);
ALTER TABLE tmp_question_match ADD INDEX (target_uid);


-- SCRATCHIE

-- shift Scratchie question UIDs by offset equal to existing UIDs of MCQ in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
-- remove characters that prevent detecting identical questions
UPDATE tl_lascrt11_scratchie_item SET `title` = TRIM(REPLACE(REPLACE(REPLACE(title, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
									  `description` = TRIM(REPLACE(REPLACE(REPLACE(description, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
									   uid = uid + @max_tool_question_id ORDER BY uid DESC;
UPDATE tl_lascrt11_scratchie_answer SET `description` = TRIM(REPLACE(REPLACE(REPLACE(description, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
-- UPDATE tl_lascrt11_scratchie_answer SET scratchie_item_uid = scratchie_item_uid + @max_tool_question_id ORDER BY scratchie_item_uid DESC;

-- create a mapping of Scratchie question UID -> its question description + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
INSERT INTO tmp_question
	SELECT q.uid,
	 		REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.description ORDER BY o.order_id))
	 								COLLATE utf8mb4_0900_ai_ci,
	 						' ', ''),
	 				'\t', ''),
	 		'&nbsp;', '')
	FROM tl_lascrt11_scratchie_item AS q
	JOIN tl_lascrt11_scratchie_answer AS o
		ON q.uid = o.scratchie_item_uid
	GROUP BY q.uid;
	
-- create a similar mapping for existing questions in QB
CREATE TABLE tmp_qb_question (question_uid BIGINT PRIMARY KEY,
						      content MEDIUMTEXT)
	AS SELECT q.uid AS question_uid,
			  REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.name ORDER BY o.display_order))
			  						  COLLATE utf8mb4_0900_ai_ci, 
			  				  ' ', ''),
			  		 '\t', ''), 
			  '&nbsp;', '') AS content
	FROM lams_qb_question AS q
	JOIN lams_qb_option AS o
		ON q.uid = o.qb_question_uid
	GROUP BY q.uid;
	
ALTER TABLE tmp_qb_question ADD INDEX (content(500));

-- create a table which holds IDs of questions which are already in the question bank
CREATE TABLE tmp_qb_question_match (question_uid BIGINT PRIMARY KEY, qb_question_uid BIGINT)
	AS SELECT q.question_uid, qb.question_uid AS qb_question_uid
	FROM tmp_question AS q
	JOIN tmp_qb_question AS qb
		USING (content)
	GROUP BY q.question_uid;

ALTER TABLE tmp_qb_question_match ADD INDEX (qb_question_uid);

-- create a mapping of Scratchie question UID -> UID of one of Scratchie questions which holds the same content
INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content)
	LEFT JOIN tmp_qb_question_match AS qb
		ON q.question_uid = qb.question_uid
	WHERE qb.question_uid IS NULL
	GROUP BY q.question_uid;

-- reset column for matching QB questions with Scratchie questions
UPDATE lams_qb_question SET tmp_question_id = -1;
	
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question (uid, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id)
	SELECT NULL, 1, @question_id:=@question_id + 1, 1, sq.create_date, 
	TRIM(REPLACE(REPLACE(strip_tags(sq.question) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', '')),
	TRIM(sq.description), NULL, NULL, q.target_uid
	FROM (SELECT uid,
				 title AS question,
				 description,
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
	SELECT qb.question_uid, qb.qb_question_uid, s.content_id, sq.order_id
	FROM tmp_qb_question_match AS qb
	JOIN tl_lascrt11_scratchie_item AS sq
		ON qb.question_uid = sq.uid
	JOIN tl_lascrt11_scratchie AS s
		ON sq.scratchie_uid = s.uid;
		
-- update question names generated for MCQ with real names from Scratchie 
UPDATE lams_qb_question AS q 
	JOIN (SELECT * FROM tmp_qb_question_match GROUP BY qb_question_uid) AS m
		ON q.uid = m.qb_question_uid
	JOIN tl_lascrt11_scratchie_item AS si
		ON si.uid = m.question_uid
	SET q.name = TRIM(si.title);
		
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
	SELECT q.uid, o.order_id, o.description, o.correct
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
		   
-- prepare for next tool migration, recreate tables
DROP TABLE tmp_question;
DROP TABLE tmp_question_match;
DROP TABLE tmp_qb_question;
DROP TABLE tmp_qb_question_match;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content MEDIUMTEXT);
ALTER TABLE tmp_question ADD INDEX (content(500));

CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
								 target_uid BIGINT);
ALTER TABLE tmp_question_match ADD INDEX (target_uid);

CREATE TABLE tmp_qb_question (question_uid BIGINT PRIMARY KEY,
						      content MEDIUMTEXT);
ALTER TABLE tmp_qb_question ADD INDEX (content(500));

CREATE TABLE tmp_qb_question_match (question_uid BIGINT PRIMARY KEY,
									qb_question_uid BIGINT);
ALTER TABLE tmp_qb_question_match ADD INDEX (qb_question_uid);

-- ASSESSMENT

-- shift Assessment question UIDs by offset equal to existing UIDs of MCQ adn Scratchie in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
-- remove characters that prevent detecting identical questions
UPDATE tl_laasse10_assessment_question SET `title` = TRIM(REPLACE(REPLACE(REPLACE(title, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
										   `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
										   uid = uid + @max_tool_question_id ORDER BY uid DESC;
UPDATE tl_laasse10_question_option SET `option_string` = TRIM(REPLACE(REPLACE(REPLACE(option_string, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
									   `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
UPDATE tl_laasse10_question_result SET `answer_string` = TRIM(REPLACE(REPLACE(REPLACE(answer_string, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));

-- first, process questions with question_type=1 (as MCQ and Scratchie have only this type of questions)

-- create a mapping of Assessment question UID -> its question description + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
ALTER TABLE tl_laasse10_question_option ADD INDEX (sequence_id),
										ADD INDEX tmp_index (sequence_id, question_uid);
		
INSERT INTO tmp_question
	SELECT q.uid,
		   REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.question, o.option_string ORDER BY o.sequence_id))
		  						  COLLATE utf8mb4_0900_ai_ci, 
		  				  ' ', ''),
		  		 '\t', ''), 
		  '&nbsp;', '')
	FROM tl_laasse10_assessment_question AS q
	JOIN tl_laasse10_question_option AS o
		ON q.uid = o.question_uid
	WHERE q.question_type = 1
	GROUP BY q.uid;
	
-- create a similar mapping for existing questions in QB
INSERT INTO tmp_qb_question
	SELECT q.uid AS question_uid,
		   REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.name ORDER BY o.display_order))
		  						  COLLATE utf8mb4_0900_ai_ci, 
		  				  ' ', ''),
		  		 '\t', ''), 
		  '&nbsp;', '') AS content
	FROM lams_qb_question AS q
	JOIN lams_qb_option AS o
		ON q.uid = o.qb_question_uid
	GROUP BY q.uid;
	
-- create a mapping which holds IDs of questions which are already in the question bank
INSERT INTO tmp_qb_question_match
	SELECT q.question_uid, qb.question_uid AS qb_question_uid
	FROM tmp_question AS q
	JOIN tmp_qb_question AS qb
		USING (content);
		
-- set up references to QB question UIDs for existing questions
INSERT INTO lams_qb_tool_question
	SELECT qb.question_uid, qb.qb_question_uid, assess.content_id, aq.sequence_id
	FROM tmp_qb_question_match qb
	JOIN tl_laasse10_assessment_question AS aq
		ON qb.question_uid = aq.uid
	JOIN tl_laasse10_assessment AS assess
		ON aq.assessment_uid = assess.uid;
		
		
-- update question names generated for MCQ with real names from Assessment
UPDATE lams_qb_question AS q 
	JOIN (SELECT * FROM tmp_qb_question_match GROUP BY qb_question_uid) AS m
		ON q.uid = m.qb_question_uid
	JOIN tl_laasse10_assessment_question AS aq
		ON aq.uid = m.question_uid
	SET q.name = TRIM(aq.question);

-- create a mapping of Assessment question UID -> UID of one of Assessment questions which holds the same content
INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content)
	LEFT JOIN lams_qb_tool_question AS qb
		ON qb.tool_question_uid = q.question_uid
	WHERE qb.tool_question_uid IS NULL
	GROUP BY q.question_uid;
	
	
-- second run, process questions with all other question_type
DROP TABLE tmp_question;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content MEDIUMTEXT);
ALTER TABLE tmp_question ADD INDEX (content(500));

INSERT INTO tmp_question
	SELECT q.uid,
		  REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.question_type,
		  												 IFNULL(q.question, ''),
		   												 q.correct_answer,
														 IFNULL(
														 	CONCAT(IFNULL(o.question, ''),
																   IFNULL(o.option_string, ''),
																   o.option_float,
																   o.correct),
														 '')
											ORDER BY o.sequence_id))
									COLLATE utf8mb4_0900_ai_ci,
						  ' ', ''),
				  '\t', ''),
		 '&nbsp;', '') 
	FROM tl_laasse10_assessment_question AS q
	LEFT JOIN tl_laasse10_question_option AS o
		ON q.uid = o.question_uid
	WHERE q.question_type != 1
	GROUP BY q.uid;

INSERT INTO tmp_question_match
	SELECT q.question_uid, merged.question_uid
	FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
	JOIN tmp_question AS q
		USING (content)
	GROUP BY q.question_uid;
	
-- reset column for matching QB questions with Assessment questions
UPDATE lams_qb_question SET tmp_question_id = -1;

	
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question SELECT NULL, aq.question_type, @question_id:=@question_id + 1, 1, IFNULL(assessment.create_date, NOW()), 'temp_folder',
			    TRIM(REPLACE(REPLACE(strip_tags(aq.question) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', ' ')),
				TRIM(aq.description), IFNULL(aq.max_mark, 1), aq.feedback, aq.penalty_factor, aq.answer_required,
				aq.multiple_answers_allowed, aq.incorrect_answer_nullifies_mark, aq.feedback_on_correct, aq.feedback_on_partially_correct,
				aq.feedback_on_incorrect, aq.shuffle, aq.prefix_answers_with_letters, aq.case_sensitive, aq.correct_answer,
				aq.allow_rich_editor, aq.max_words_limit, aq.min_words_limit, aq.hedging_justification_enabled, q.target_uid
	FROM (SELECT uid,
				 title AS question,
				 question AS description,
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
									   

-- delete corrupted data
DELETE FROM  tl_laasse10_question_option WHERE question_uid IS NULL;

-- some Assessment options can be ordered from 0, some from 1
-- shift ones ordered from 0 by +1 to match the other group		
CREATE TABLE tmp_assessment_option
SELECT DISTINCT question_uid FROM tl_laasse10_question_option WHERE sequence_id = 0;

ALTER TABLE tmp_assessment_option ADD PRIMARY KEY (question_uid);
								   	   
UPDATE tl_laasse10_question_option AS o
SET sequence_id = sequence_id + 1
WHERE EXISTS
	(SELECT 1 FROM tmp_assessment_option WHERE question_uid = o.question_uid);
		
DROP TABLE tmp_assessment_option;

-- fill table with options matching unique QB questions inserted above		  
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct, matching_pair, numerical_option, max_mark, accepted_error, feedback)
	SELECT q.uid, o.sequence_id, IFNULL(o.option_string, ''), o.correct OR o.grade > 0, o.question, o.option_float,
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
	SET sl.submitted_option_uid = qo.uid
	WHERE   o.sequence_id = qo.display_order
		AND sl.submitted_option_uid = o.uid
		AND qo.qb_question_uid = tq.qb_question_uid
		AND o.question_uid = tq.tool_question_uid;
		
-- prepare for updating option IDs in tl_laasse10_option_answer
CREATE TABLE tmp_option_answer 
	SELECT DISTINCT sa.question_option_uid, qo.uid
 		FROM tl_laasse10_option_answer AS sa
 		JOIN tl_laasse10_question_option AS o FORCE INDEX (tmp_index) ON sa.question_option_uid = o.uid
 		JOIN lams_qb_option AS qo ON o.sequence_id = qo.display_order
 		JOIN lams_qb_tool_question AS tq ON qo.qb_question_uid = tq.qb_question_uid
 			AND o.question_uid = tq.tool_question_uid;
 
ALTER TABLE tmp_option_answer ADD PRIMARY KEY (question_option_uid);

-- there can be few malformed answers; delete them
DELETE o 
	FROM tl_laasse10_option_answer AS o
	LEFT JOIN tmp_option_answer AS t USING (question_option_uid)
	WHERE t.question_option_uid IS NULL;

ALTER TABLE tl_laasse10_option_answer DROP FOREIGN KEY FK_tl_laasse10_option_answer_2;


-- proper update
UPDATE tl_laasse10_option_answer AS sa, tmp_option_answer AS t 
	SET sa.question_option_uid = t.uid
	WHERE sa.question_option_uid = t.question_option_uid;

ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 
	FOREIGN KEY (question_option_uid) REFERENCES lams_qb_option (uid) ON DELETE CASCADE ON UPDATE CASCADE;
		
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
		   tmp_qb_question,
		   tmp_qb_question_match,
		   tmp_option_answer;

-- add a table for generating questionId in QbQuestion and possible other sequences in the future
CREATE TABLE lams_sequence_generator (lams_qb_question_question_id INT);
CREATE UNIQUE INDEX IDX_lams_qb_question_question_id ON lams_sequence_generator(lams_qb_question_question_id);
INSERT INTO lams_sequence_generator(lams_qb_question_question_id) VALUES ((SELECT MAX(question_id) FROM lams_qb_question));