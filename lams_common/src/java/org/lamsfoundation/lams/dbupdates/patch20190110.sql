-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4746 Create Question Bank table structure and migrate existing data

-- Create QB question table
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT, 
							   `local` TINYINT(1) NOT NULL DEFAULT 1,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `create_date` DATETIME NOT NULL DEFAULT NOW(),
							   `name` TEXT NOT NULL,
							   `description` TEXT,
							   `mark` INT,
							   `feedback` TEXT,
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
							 `name` TEXT NOT NULL,
							 `correct` TINYINT(1) NOT NULL DEFAULT 0,
							 PRIMARY KEY (uid),
							 INDEX (display_order),
							 CONSTRAINT FK_lams_qb_option_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- create an answer table from tools' answers will inherit
CREATE TABLE lams_qb_tool_answer (`answer_uid` BIGINT AUTO_INCREMENT,
								  `tool_question_uid` BIGINT NOT NULL,
								  `qb_option_uid` BIGINT NOT NULL,
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
						   content TEXT)
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

INSERT INTO lams_qb_question SELECT NULL, 1, 1, @question_id:=@question_id + 1, 1, IFNULL(c.creation_date, NOW()),
									mcq.question, NULL, IFNULL(mcq.mark, 1), mcq.feedback, q.target_uid
	FROM (SELECT uid,
				 TRIM(question) AS question,
				 mark,
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
	SELECT q.uid, o.displayOrder, TRIM(o.mc_que_option_text), o.correct_option
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
		   GROUP_CONCAT(TRIM(q.title), TRIM(o.description) ORDER BY o.order_id)
	FROM tl_lascrt11_scratchie_item AS q
	JOIN tl_lascrt11_scratchie_answer AS o
		ON q.uid = o.scratchie_item_uid
	GROUP BY q.uid;
	
-- create a similar mapping for existing questions in QB
CREATE TABLE tmp_qb_question
	AS SELECT q.uid AS question_uid,
			  GROUP_CONCAT(TRIM(q.name), TRIM(o.name) ORDER BY o.display_order) AS content
	FROM lams_qb_question AS q
	JOIN lams_qb_option AS o
		ON q.uid = o.qb_question_uid
	WHERE q.type = 1
	GROUP BY q.uid;

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
INSERT INTO lams_qb_question SELECT NULL, 1, 1, @question_id:=@question_id + 1, 1, sq.create_date, 
									sq.question, sq.description, NULL, NULL, q.target_uid
	FROM (SELECT uid,
				 TRIM(title) AS question,
				 TRIM(description) AS description,
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
	SELECT q.uid, o.order_id, TRIM(o.description), o.correct
	FROM tl_lascrt11_scratchie_answer AS o
	JOIN lams_qb_question AS q
		ON o.scratchie_item_uid = q.tmp_question_id
	WHERE o.scratchie_item_uid IS NOT NULL
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
		AND sa.scratchie_item_uid = tq.tool_question_uid
		AND sa.scratchie_item_uid IS NOT NULL;
		
-- prepare for answer inheritance
INSERT INTO lams_qb_tool_answer
	SELECT uid, scratchie_item_uid, scratchie_answer_uid FROM tl_lascrt11_answer_log;


-- cleanup
ALTER TABLE lams_qb_question DROP COLUMN tmp_question_id;
ALTER TABLE tl_lascrt11_answer_log DROP COLUMN scratchie_item_uid,
								   DROP COLUMN scratchie_answer_uid;
								   
DROP TABLE tl_lascrt11_scratchie_answer,
		   tmp_question,
		   tmp_question_match,
		   tmp_qb_question;
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;