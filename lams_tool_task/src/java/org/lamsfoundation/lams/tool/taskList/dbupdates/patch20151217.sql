-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_latask10_condition DROP FOREIGN KEY FK_tl_latask10_condition_1;
ALTER TABLE tl_latask10_condition ADD CONSTRAINT FK_tl_latask10_condition_1 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_condition_tl_item DROP FOREIGN KEY FK_tl_latask10_tasklist_item_condition_1;
ALTER TABLE tl_latask10_condition_tl_item ADD CONSTRAINT FK_tl_latask10_tasklist_item_condition_1 FOREIGN KEY (`condition_uid`)
REFERENCES `tl_latask10_condition` (`condition_uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_condition_tl_item DROP FOREIGN KEY FK_tl_latask10_tasklist_item_condition_2;
ALTER TABLE tl_latask10_condition_tl_item ADD CONSTRAINT FK_tl_latask10_tasklist_item_condition_2 FOREIGN KEY (`uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_item_attachment DROP FOREIGN KEY FK_tl_latask10_item_attachment_1;
ALTER TABLE tl_latask10_item_attachment ADD CONSTRAINT FK_tl_latask10_item_attachment_1 FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_item_attachment DROP FOREIGN KEY FK_tl_latask10_item_attachment_2;
ALTER TABLE tl_latask10_item_attachment ADD CONSTRAINT FK_tl_latask10_item_attachment_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_latask10_item_comment DROP FOREIGN KEY FK_tl_latask10_item_comment_2;
ALTER TABLE tl_latask10_item_comment ADD CONSTRAINT FK_tl_latask10_item_comment_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_item_comment DROP FOREIGN KEY FK_tl_latask10_item_comment_3;
ALTER TABLE tl_latask10_item_comment ADD CONSTRAINT FK_tl_latask10_item_comment_3 FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_latask10_session DROP FOREIGN KEY FK_NEW_174079138_24AA78C530E79035;
ALTER TABLE tl_latask10_session ADD CONSTRAINT FK_NEW_174079138_24AA78C530E79035 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_tasklist DROP FOREIGN KEY FK_NEW_174079138_89093BF758092FB;
ALTER TABLE tl_latask10_tasklist ADD CONSTRAINT FK_NEW_174079138_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F9330E79035;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F9330E79035 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F93758092FB;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F93EC0D3147;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_latask10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_user DROP FOREIGN KEY FK_NEW_174079138_30113BFCEC0D3147;
ALTER TABLE tl_latask10_user ADD CONSTRAINT FK_NEW_174079138_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_latask10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_user DROP FOREIGN KEY FK_NEW_174079138_30113BFC309ED320;
ALTER TABLE tl_latask10_user ADD CONSTRAINT FK_NEW_174079138_30113BFC309ED320 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
		
			 
ALTER TABLE tl_latask10_item_log DROP FOREIGN KEY FK_NEW_174079138_693580A441F9365D;
ALTER TABLE tl_latask10_item_log ADD CONSTRAINT FK_NEW_174079138_693580A441F9365D FOREIGN KEY (`user_uid`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;		

ALTER TABLE tl_latask10_item_log DROP FOREIGN KEY FK_NEW_174079138_693580A438BF8DFE;
ALTER TABLE tl_latask10_item_log ADD CONSTRAINT FK_NEW_174079138_693580A438BF8DFE FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;