-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_lascrt11_scratchie MODIFY COLUMN extra_point TINYINT(1),
								  MODIFY COLUMN define_later TINYINT(1),
							      MODIFY COLUMN reflect_on_activity TINYINT(1),
								  MODIFY COLUMN burning_questions_enabled TINYINT(1);

ALTER TABLE tl_lascrt11_session MODIFY COLUMN status TINYINT(1),
								MODIFY COLUMN scratching_finished TINYINT(1) DEFAULT 0;
								
ALTER TABLE tl_lascrt11_user MODIFY COLUMN session_finished TINYINT(1);

ALTER TABLE tl_lascrt11_scratchie_answer MODIFY COLUMN correct TINYINT(1);	

ALTER TABLE tl_lascrt11_scratchie_item MODIFY COLUMN create_by_author TINYINT(1);	

ALTER TABLE tl_lascrt11_burning_question MODIFY COLUMN general_question TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lascrt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
