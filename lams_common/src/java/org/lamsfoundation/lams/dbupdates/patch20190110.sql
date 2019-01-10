-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4746 Create a Question Bank question structure
CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT, 
							   `local` TINYINT(1) NOT NULL DEFAULT 1,
							   `type` TINYINT NOT NULL,
							   `question_id` INT NOT NULL,
							   `version` SMALLINT NOT NULL DEFAULT 1,
							   `name` TEXT,
							   `mark` INT,
							   `feedback` TEXT,
							   PRIMARY KEY (uid),
							   CONSTRAINT UQ_question_version UNIQUE KEY (question_id, version));

-- Convert MCQ question into Question Bank question
-- This part of patch should be in MCQ, but it must be run after lams_qb_question is created and it must not run in parallel with other tools' patches,
-- so the safest solution is to place it here

-- fill Question Bank table with unique questions, with manually incremented question ID
SET @question_id = (SELECT IF(MAX(question_id) IS NULL, 0, MAX(question_id)) FROM lams_qb_question);
SET @start_question_id = @question_id;
INSERT INTO lams_qb_question SELECT NULL, 1, 1, @question_id:=@question_id + 1, 1, question, mark, feedback
	FROM (SELECT DISTINCT TRIM(question) AS question, mark, IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback FROM tl_lamc11_que_content) AS mcq;

-- prepare MCQ's question table for referencing Question Bank's question
ALTER TABLE tl_lamc11_que_content ADD COLUMN qb_question_uid BIGINT AFTER uid,
								  ADD CONSTRAINT FK_qb_question FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE;

-- find matching questions in Question Bank and set up references
UPDATE tl_lamc11_que_content AS mcq, lams_qb_question AS qb
	SET mcq.qb_question_uid = qb.ui
	WHERE TRIM(mcq.question) = qb.name AND mcq.mark = qb.mark 
		AND (TRIM(mcq.feedback) = qb.feedback OR (IF(TRIM(mcq.feedback) = '', NULL, TRIM(mcq.feedback)) IS NULL AND qb.feedback IS NULL))
		AND qb.question_id > @start_question_id;

-- remove columns from MCQ which are duplicated in Question Bank
ALTER TABLE tl_lamc11_que_content DROP COLUMN question,
								  DROP COLUMN mark,
								  DROP COLUMN feedback;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;