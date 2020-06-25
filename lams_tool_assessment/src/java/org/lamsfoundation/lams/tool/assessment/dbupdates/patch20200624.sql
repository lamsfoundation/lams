-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5033 Add justification to question answer in Assessment

ALTER TABLE tl_laasse10_question_result ADD COLUMN justification VARCHAR(10000);
ALTER TABLE tl_laasse10_assessment ADD COLUMN allow_answer_justification TINYINT DEFAULT 0 AFTER allow_history_responses;
		

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
