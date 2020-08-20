-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5050 Move "is required" attribute from QB question to activity level

ALTER TABLE lams_qb_tool_question ADD COLUMN answer_required TINYINT NOT NULL DEFAULT 0 AFTER tool_content_id;

UPDATE lams_qb_tool_question AS tq
	JOIN lams_qb_question AS q ON q.uid = tq.qb_question_uid
	SET tq.answer_required = q.answer_required;	
	
ALTER TABLE lams_qb_question DROP COLUMN answer_required;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
