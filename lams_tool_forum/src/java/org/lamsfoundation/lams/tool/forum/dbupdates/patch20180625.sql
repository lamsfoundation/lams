-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4603 Delete preview lessons with peer review activity doesn't work
ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_2;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_2
  FOREIGN KEY (message_id)
  REFERENCES tl_lafrum11_message (uid)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;

