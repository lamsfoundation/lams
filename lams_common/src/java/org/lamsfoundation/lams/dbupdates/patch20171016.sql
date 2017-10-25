-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4451 Ability to choose confidence levels in Assessment and MCQ and display them later in Scratchie
CREATE TABLE lams_confidence_level (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  question_uid bigint(20),
  user_id bigint(20) NOT NULL,
  confidence_level int,
  tool_session_id BIGINT(20),
  PRIMARY KEY (uid),
  KEY user_id (user_id),
  CONSTRAINT FK_lams_confidence_level_1 FOREIGN KEY (user_id)
  		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
