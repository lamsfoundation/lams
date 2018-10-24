SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
-- LDEV-4145 Comments to have different background when posted via Monitor UI
ALTER TABLE lams_comment 
ADD COLUMN monitor SMALLINT(6) NULL DEFAULT 0;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
