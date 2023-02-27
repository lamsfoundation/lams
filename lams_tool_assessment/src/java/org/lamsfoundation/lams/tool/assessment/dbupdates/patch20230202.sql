-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5359 Add question sections
CREATE TABLE tl_laasse10_section(
   uid MEDIUMINT UNSIGNED NOT NULL auto_increment,
   assessment_uid BIGINT,
   display_order TINYINT UNSIGNED NOT NULL DEFAULT '1',
   name VARCHAR(100),
   question_count TINYINT UNSIGNED NOT NULL,
   CONSTRAINT FK_tl_laasse10_section_1  FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment(uid)
   		ON UPDATE CASCADE ON DELETE CASCADE,
   PRIMARY KEY (uid)
);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;