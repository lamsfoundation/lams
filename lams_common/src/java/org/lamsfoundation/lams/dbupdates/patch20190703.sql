-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4834 Add Learning Outcomes to QB questions
ALTER TABLE lams_outcome_mapping ADD COLUMN qb_question_id INT,
								 ADD CONSTRAINT FK_lams_outcome_mapping_4 FOREIGN KEY (qb_question_id)
								 	REFERENCES lams_qb_question (question_id) ON DELETE CASCADE ON UPDATE CASCADE;
								 	
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;