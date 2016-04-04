-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-
--ALTER TABLE tl_lascrt11_scratchie ADD COLUMN burning_questions_enabled TINYINT DEFAULT 1;
CREATE TABLE tl_lascrt11_burning_que_like (
   uid bigint NOT NULL auto_increment,
   burning_question_uid BIGINT,
   session_id bigint,
   PRIMARY KEY (uid)
)ENGINE=InnoDB;
ALTER TABLE tl_lascrt11_burning_que_like ADD INDEX FK_burning_que_uid (burning_question_uid), ADD CONSTRAINT FK_burning_que_uid FOREIGN KEY (burning_question_uid) REFERENCES tl_lascrt11_burning_question (uid) ON DELETE CASCADE ON UPDATE CASCADE;


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
