-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4746 Rewrite QB question insert trigger so it accepts existing UUID if it is provided
DROP TRIGGER IF EXISTS before_insert_qb_question;

CREATE TRIGGER before_insert_qb_question
  BEFORE INSERT ON lams_qb_question
  FOR EACH ROW
  SET new.uuid = IFNULL(new.uuid, UUID_TO_BIN(UUID()));

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;