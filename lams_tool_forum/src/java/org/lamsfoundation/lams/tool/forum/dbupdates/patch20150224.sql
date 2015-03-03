SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------Put all sql statements below here-------------------------

-- Adding thread ids. 5 patch files needed for the change - patch20150224 thru patch20150228

ALTER TABLE `tl_lafrum11_message_seq` 
ADD COLUMN `thread_message_uid` BIGINT(20) DEFAULT NULL,
ADD INDEX `fkfrum11mseqthread` (`thread_message_uid` ASC);
ALTER TABLE `tl_lafrum11_message_seq` 
ADD CONSTRAINT `fkfrum11mseqthread`
  FOREIGN KEY (`thread_message_uid`)
  REFERENCES `tl_lafrum11_message` (`uid`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

-- --------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;

