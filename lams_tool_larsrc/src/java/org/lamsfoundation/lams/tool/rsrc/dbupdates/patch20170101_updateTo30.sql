-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-2941 Enable the option "Open URL in pop-up" for the example URL by default
UPDATE tl_larsrc11_resource_item SET open_url_new_window=1 WHERE uid='1';

-- LDEV-3153 Enlarge instructions field for Share Resource
ALTER TABLE tl_larsrc11_item_instruction MODIFY description TEXT;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_larsrc11_resource DROP COLUMN online_instructions;
ALTER TABLE tl_larsrc11_resource DROP COLUMN offline_instructions;
ALTER TABLE tl_larsrc11_resource DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_larsrc11_attachment;

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_larsrc11_resource MODIFY COLUMN reflect_instructions text;

-- LDEV-3292 These indexes will help Hibernate.
CREATE INDEX idx_user_user_id ON tl_larsrc11_user(user_id);
CREATE INDEX idx_item_log_session_id ON tl_larsrc11_item_log(session_id);

-- LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_larsrc11_session ADD UNIQUE (session_id);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_larsrc11_item_instruction DROP FOREIGN KEY FKA5665013980570ED;
ALTER TABLE tl_larsrc11_item_instruction ADD CONSTRAINT FKA5665013980570ED FOREIGN KEY (`item_uid`)
REFERENCES `tl_larsrc11_resource_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_item_log DROP FOREIGN KEY FK693580A438BF8DFE;
ALTER TABLE tl_larsrc11_item_log ADD CONSTRAINT FK693580A438BF8DFE FOREIGN KEY (`resource_item_uid`)
REFERENCES `tl_larsrc11_resource_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_item_log DROP FOREIGN KEY FK693580A441F9365D;
ALTER TABLE tl_larsrc11_item_log ADD CONSTRAINT FK693580A441F9365D FOREIGN KEY (`user_uid`)
REFERENCES `tl_larsrc11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_resource DROP FOREIGN KEY FK89093BF758092FB;
ALTER TABLE tl_larsrc11_resource ADD CONSTRAINT FK89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_larsrc11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_resource_item DROP FOREIGN KEY FKF52D1F9330E79035;
ALTER TABLE tl_larsrc11_resource_item ADD CONSTRAINT FKF52D1F9330E79035 FOREIGN KEY (`resource_uid`)
REFERENCES `tl_larsrc11_resource` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_resource_item DROP FOREIGN KEY FKF52D1F93758092FB;
ALTER TABLE tl_larsrc11_resource_item ADD CONSTRAINT FKF52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_larsrc11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_resource_item DROP FOREIGN KEY FKF52D1F93EC0D3147;
ALTER TABLE tl_larsrc11_resource_item ADD CONSTRAINT FKF52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_larsrc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_session DROP FOREIGN KEY FK24AA78C530E79035;
ALTER TABLE tl_larsrc11_session ADD CONSTRAINT FK24AA78C530E79035 FOREIGN KEY (`resource_uid`)
REFERENCES `tl_larsrc11_resource` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_user DROP FOREIGN KEY FK30113BFCEC0D3147;
ALTER TABLE tl_larsrc11_user ADD CONSTRAINT FK30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_larsrc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_larsrc11_user DROP FOREIGN KEY FK30113BFC309ED320;
ALTER TABLE tl_larsrc11_user ADD CONSTRAINT FK30113BFC309ED320 FOREIGN KEY (`resource_uid`)
REFERENCES `tl_larsrc11_resource` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

-- LDEV-3760 Add notification on file upload
ALTER TABLE tl_larsrc11_resource ADD COLUMN file_upload_notify tinyint DEFAULT 0;

-- LDEV-4180
ALTER TABLE tl_larsrc11_resource MODIFY COLUMN lock_on_finished TINYINT(1),
								 MODIFY COLUMN content_in_use TINYINT(1),
								 MODIFY COLUMN define_later TINYINT(1),
								 MODIFY COLUMN allow_auto_run TINYINT(1),
								 MODIFY COLUMN reflect_on_activity TINYINT(1),
								 MODIFY COLUMN allow_add_files TINYINT(1),
								 MODIFY COLUMN allow_add_urls TINYINT(1),
								 MODIFY COLUMN assigment_submit_notify TINYINT(1),
								 MODIFY COLUMN file_upload_notify TINYINT(1);
									 	
ALTER TABLE tl_larsrc11_resource_item MODIFY COLUMN create_by_author TINYINT(1),
									  MODIFY COLUMN is_hide TINYINT(1),
									  MODIFY COLUMN open_url_new_window TINYINT(1);

ALTER TABLE tl_larsrc11_item_log MODIFY COLUMN complete TINYINT(1);									 		 
	
ALTER TABLE tl_larsrc11_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='larsrc11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;