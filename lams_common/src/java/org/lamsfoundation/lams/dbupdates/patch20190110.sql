-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4746 Create Question Bank table structure

-- Create QB question table
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT, 
							   `local` TINYINT(1) NOT NULL DEFAULT 1,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `name` TEXT NOT NULL,
							   `mark` INT,
							   `feedback` TEXT,
							   PRIMARY KEY (uid),
							   CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version),
							   INDEX (`local`));
							   
-- Create a question table from which tools' questions will inherit						   
CREATE TABLE lams_qb_tool_question (`tool_question_uid` BIGINT AUTO_INCREMENT,
									`qb_question_uid` BIGINT NOT NULL,
									PRIMARY KEY (tool_question_uid),
									CONSTRAINT FK_lams_qb_tool_question_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE);

-- Convert MCQ question into Question Bank question
-- This part of patch should be in MCQ, but it must be run after lams_qb_question is created and it must not run in parallel with other tools' patches,
-- so the safest solution is to place it here

-- fill Question Bank table with unique questions, with manually incremented question ID
SET @question_id = (SELECT IF(MAX(question_id) IS NULL, 0, MAX(question_id)) FROM lams_qb_question);
INSERT INTO lams_qb_question SELECT NULL, 1, 1, @question_id:=@question_id + 1, 1, question, mark, feedback
	FROM (SELECT DISTINCT TRIM(question) AS question, mark, IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback FROM tl_lamc11_que_content) AS mcq;

-- find matching questions in Question Bank and set up references
INSERT INTO lams_qb_tool_question
SELECT mcq.uid, qb.uid
FROM tl_lamc11_que_content AS mcq JOIN lams_qb_question AS qb
	ON TRIM(mcq.question) = qb.name
	AND mcq.mark = qb.mark 
	AND (TRIM(mcq.feedback) = qb.feedback OR (IF(TRIM(mcq.feedback) = '', NULL, TRIM(mcq.feedback)) IS NULL AND qb.feedback IS NULL))
WHERE qb.type = 1;

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

INSERT INTO lams_qb_option (qb_question_uid, display_order, name, correct)
	SELECT DISTINCT tq.qb_question_uid, o.displayOrder, TRIM(o.mc_que_option_text), o.correct_option
	FROM tl_lamc11_options_content AS o
	JOIN lams_qb_tool_question AS tq ON o.mc_que_content_id = tq.tool_question_uid;
	
ALTER TABLE tl_lamc11_options_content ADD COLUMN qb_option_uid BIGINT AFTER uid,
									  ADD CONSTRAINT FK_tl_lamc11_options_content_2 FOREIGN KEY (qb_option_uid) REFERENCES lams_qb_option (uid) ON UPDATE CASCADE;
									  
UPDATE tl_lamc11_options_content AS mco, lams_qb_tool_question AS tq, lams_qb_option AS qo
	SET mco.qb_option_uid = qo.uid
	WHERE TRIM(mco.mc_que_option_text) = qo.name
		AND qo.qb_question_uid = tq.qb_question_uid
		AND mco.mc_que_content_id = tq.tool_question_uid;
	
ALTER TABLE tl_lamc11_options_content DROP COLUMN mc_que_option_text,
									  DROP COLUMN correct_option,
									  DROP COLUMN displayOrder;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;