-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4976 Introduce an extra column which holds the same information as parent table, then add unique index to prevent duplicates

ALTER TABLE tl_lamc11_usr_attempt ADD COLUMN qb_tool_question_uid BIGINT AFTER uid;

UPDATE tl_lamc11_usr_attempt AS a JOIN lams_qb_tool_answer AS q ON a.uid = q.answer_uid
SET a.qb_tool_question_uid = q.tool_question_uid;

ALTER TABLE tl_lamc11_usr_attempt MODIFY COLUMN qb_tool_question_uid BIGINT NOT NULL,
								  ADD UNIQUE INDEX IDX_attempt_duplicate_prevent (qb_tool_question_uid, que_usr_id);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
