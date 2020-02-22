-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4976 Add cascade so when a row from parent table gets removed, a row from inheriting table gets removed too

ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_1 FOREIGN KEY (uid) 
	REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
