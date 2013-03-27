-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3010 Emails sent via forum when rich text editor is able should be sent with the correct mime-type
ALTER TABLE lams_events ADD COLUMN html_format TINYINT(1) DEFAULT 0; 

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
