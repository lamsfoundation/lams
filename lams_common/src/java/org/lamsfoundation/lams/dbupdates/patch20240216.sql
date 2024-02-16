-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5446 Rewrite QB question insert trigger so it accepts existing UUID if it is provided
DROP TRIGGER IF EXISTS before_insert_qb_question;

CREATE TRIGGER before_insert_qb_question
  BEFORE INSERT ON lams_qb_question
  FOR EACH ROW
  SET new.uuid = IFNULL(new.uuid, UUID_TO_BIN(UUID()));

-- Remove duplicate UUIDs and make sure they are unique in the future
UPDATE lams_qb_question AS q1, lams_qb_question AS q2 SET q2.uuid = UUID_TO_BIN(UUID())
WHERE q1.uuid = q2.uuid AND q1.uid < q2.uid;

ALTER TABLE lams_qb_question ADD CONSTRAINT UQ_uuid UNIQUE (uuid);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;