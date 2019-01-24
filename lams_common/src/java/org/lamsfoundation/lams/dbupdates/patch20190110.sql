-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4746 Create Question Bank table structure and migrate existing data

-- Create QB question table
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT, 
							   `local` TINYINT(1) NOT NULL DEFAULT 1,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `name` TEXT NOT NULL,
							   `mark` INT,
							   `feedback` TEXT,
							   `tmp_question_id` BIGINT NOT NULL UNIQUE,
							   PRIMARY KEY (uid),
							   CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version),
							   INDEX (`local`));
							   
-- Create a question table from which tools' questions will inherit						   
CREATE TABLE lams_qb_tool_question (`tool_question_uid` BIGINT AUTO_INCREMENT,
									`qb_question_uid` BIGINT NOT NULL,
									PRIMARY KEY (tool_question_uid),
									CONSTRAINT FK_lams_qb_tool_question_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE);

-- Migrate MCQ question into Question Bank question
-- This part of patch should be in MCQ, but it must be run after lams_qb_question is created and it must not run in parallel with other tools' patches,
-- so the safest solution is to place it here
			
-- default value for a concat result is 1024 characters, which can be too little for multiple concatenated answers
-- 16000 is chosen because varchar can have maximum length of about 16000 characters on utf8mb4
SET group_concat_max_len = 16000;

-- create a mapping of MCQ question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
						   content VARCHAR(16000))
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

INSERT INTO lams_qb_question SELECT NULL, 1, 1, @question_id:=@question_id + 1, 1, mcq.question, mcq.mark, mcq.feedback, q.target_uid
	FROM (SELECT uid,
				 TRIM(question) AS question,
				 mark,
				 IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback
		  FROM tl_lamc11_que_content) AS mcq
	JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
	ON mcq.uid = q.target_uid;

-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
	SELECT q.question_uid, qb.uid
	FROM lams_qb_question AS qb
	JOIN tmp_question_match AS q
		ON qb.tmp_question_id = q.target_uid;

-- remove columns from MCQ which are duplicated in Question Bank
ALTER TABLE tl_lamc11_que_content DROP COLUMN question,
								  DROP COLUMN mark,
								  DROP COLUMN feedback;
								  
-- create Question Bank option
CREATE TABLE lams_qb_option (`uid` BIGINT AUTO_INCREMENT,
							 `qb_question_uid` BIGINT NOT NULL,
							 `display_order` TINYINT NOT NULL DEFAULT 1,
							 `name` TEXT NOT NULL,
							 `correct` TINYINT(1) NOT NULL DEFAULT 0,
							 PRIMARY KEY (uid),
							 INDEX (display_order),
							 CONSTRAINT FK_lams_qb_option_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- fill table with options matching unique QB questions inserted above
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct)
	SELECT q.uid, o.displayOrder, TRIM(o.mc_que_option_text), o.correct_option
	FROM tl_lamc11_options_content AS o
	JOIN lams_qb_question AS q ON o.mc_que_content_id = q.tmp_question_id
	ORDER BY o.displayOrder;

-- rewrite references from MCQ options to QB options
UPDATE tl_lamc11_usr_attempt AS ua, tl_lamc11_options_content AS mco, lams_qb_tool_question AS tq, lams_qb_option AS qo
	SET ua.mc_que_option_id = qo.uid
	WHERE mco.displayOrder = qo.display_order
		AND ua.mc_que_option_id = mco.uid
		AND qo.qb_question_uid = tq.qb_question_uid
		AND mco.mc_que_content_id = tq.tool_question_uid;

-- rewrite foreign keys and column name
ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3,
								  RENAME COLUMN mc_que_option_id TO qb_option_uid;
								  
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_3 FOREIGN KEY (qb_option_uid)
	REFERENCES lams_qb_option (uid) ON DELETE CASCADE ON UPDATE CASCADE;
	
-- clean up
ALTER TABLE lams_qb_question DROP COLUMN tmp_question_id;
DROP TABLE tl_lamc11_options_content,
		   tmp_question,
		   tmp_question_match;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;