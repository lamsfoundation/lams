-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2657 Date and time restriction 
ALTER TABLE tl_laasse10_assessment ADD COLUMN submission_deadline DATETIME DEFAULT null;

-- LDEV-2684 Option for not displaying answers summary
ALTER TABLE tl_laasse10_assessment ADD COLUMN display_summary tinyint DEFAULT false;

-- LDEV-2714 Pool of questions for assessment tool 
CREATE TABLE tl_laasse10_question_reference (
   uid bigint not null auto_increment,
   question_uid bigint,
   question_type smallint,
   title varchar(255),
   sequence_id integer,
   default_grade integer DEFAULT 1,
   random_question tinyint DEFAULT 0,
   assessment_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;

-- LDEV-2805 On update from 2.3.5, make all available questions in previous assessment activities part of the questions in the list
INSERT INTO tl_laasse10_question_reference ( question_uid , question_type , title, sequence_id, default_grade, random_question, assessment_uid ) 
SELECT uid , question_type , title, sequence_id, default_grade, 0, assessment_uid FROM tl_laasse10_assessment_question;

-- LDEV-2717 Add passing mark feature to assessment tool
ALTER TABLE tl_laasse10_assessment ADD COLUMN passing_mark integer DEFAULT 0;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;