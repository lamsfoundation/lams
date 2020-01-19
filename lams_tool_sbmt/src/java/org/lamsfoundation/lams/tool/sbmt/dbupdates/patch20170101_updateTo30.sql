-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lasbmt11_content DROP COLUMN online_instruction;
ALTER TABLE tl_lasbmt11_content DROP COLUMN offline_instruction;
ALTER TABLE tl_lasbmt11_content DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lasbmt11_instruction_files;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lasbmt11_content MODIFY title varchar(255) not null;
ALTER TABLE tl_lasbmt11_content MODIFY reflect_instructions text;

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_lasbmt11_content MODIFY COLUMN reflect_instructions text;

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_lasbmt11_content DROP FOREIGN KEY FKAEF329AC172BC670;
ALTER TABLE tl_lasbmt11_content ADD CONSTRAINT FKAEF329AC172BC670 FOREIGN KEY (`created_by`)
REFERENCES `tl_lasbmt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lasbmt11_session DROP FOREIGN KEY FKEC8C77C9785A173A;
ALTER TABLE tl_lasbmt11_session ADD CONSTRAINT FKEC8C77C9785A173A FOREIGN KEY (`content_id`)
REFERENCES `tl_lasbmt11_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasbmt11_submission_details DROP FOREIGN KEY FK1411A53C93C861A;
ALTER TABLE tl_lasbmt11_submission_details ADD CONSTRAINT FK1411A53C93C861A FOREIGN KEY (`session_id`)
REFERENCES `tl_lasbmt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasbmt11_submission_details DROP FOREIGN KEY FK1411A53CFFD5A38B;
ALTER TABLE tl_lasbmt11_submission_details ADD CONSTRAINT FK1411A53CFFD5A38B FOREIGN KEY (`learner_id`)
REFERENCES `tl_lasbmt11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

DELETE FROM tl_lasbmt11_report WHERE report_id NOT IN (SELECT submission_id FROM tl_lasbmt11_submission_details);
ALTER TABLE tl_lasbmt11_report ADD FOREIGN KEY (`report_id`)
REFERENCES `tl_lasbmt11_submission_details` (`submission_id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELETE FROM tl_lasbmt11_user WHERE session_id NOT IN (SELECT session_id FROM tl_lasbmt11_session);
ALTER TABLE tl_lasbmt11_user ADD FOREIGN KEY (`session_id`)
REFERENCES `tl_lasbmt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-3688 Ability to remove/delete files from sbmt files
ALTER TABLE tl_lasbmt11_submission_details 
ADD COLUMN `removed` TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-4022 Increase the file description size (currently at 255 characters)
ALTER TABLE tl_lasbmt11_submission_details MODIFY fileDescription text;

-- LDEV-4180
ALTER TABLE tl_lasbmt11_content MODIFY COLUMN lock_on_finished TINYINT(1),
								MODIFY COLUMN content_in_use TINYINT(1),
								MODIFY COLUMN define_later TINYINT(1),
								MODIFY COLUMN reflect_on_activity TINYINT(1),
								MODIFY COLUMN limit_upload TINYINT(1),
								MODIFY COLUMN mark_release_notify TINYINT(1) DEFAULT 0,
								MODIFY COLUMN file_submit_notify TINYINT(1) DEFAULT 0;

ALTER TABLE tl_lasbmt11_session MODIFY COLUMN status TINYINT(1);									 		 
	
ALTER TABLE tl_lasbmt11_user MODIFY COLUMN finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lasbmt11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;