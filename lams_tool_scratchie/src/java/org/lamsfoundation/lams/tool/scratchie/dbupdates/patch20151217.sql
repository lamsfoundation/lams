-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lascrt11_scratchie_answer DROP FOREIGN KEY FK_scratchie_answer_1;
ALTER TABLE tl_lascrt11_scratchie_answer ADD CONSTRAINT FK_scratchie_answer_1 FOREIGN KEY (`scratchie_item_uid`)
REFERENCES `tl_lascrt11_scratchie_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F9330E79035;
ALTER TABLE tl_lascrt11_scratchie_item ADD CONSTRAINT FK_NEW_610529188_F52D1F9330E79035 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93EC0D3147;
ALTER TABLE tl_lascrt11_scratchie_item ADD CONSTRAINT FK_NEW_610529188_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lascrt11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lascrt11_session DROP FOREIGN KEY FK_lalead11_session1;
ALTER TABLE tl_lascrt11_session ADD CONSTRAINT FK_lalead11_session1 FOREIGN KEY (`group_leader_uid`)
REFERENCES `tl_lascrt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_session DROP FOREIGN KEY FK_NEW_610529188_24AA78C530E79035;
ALTER TABLE tl_lascrt11_session ADD CONSTRAINT FK_NEW_610529188_24AA78C530E79035 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFCEC0D3147;
ALTER TABLE tl_lascrt11_user ADD CONSTRAINT FK_NEW_610529188_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lascrt11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFC309ED320;
ALTER TABLE tl_lascrt11_user ADD CONSTRAINT FK_NEW_610529188_30113BFC309ED320 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

							 
ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY sessionIdIndex;
ALTER TABLE tl_lascrt11_answer_log ADD CONSTRAINT sessionIdIndex FOREIGN KEY (`session_id`)
REFERENCES `tl_lascrt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;
							 
ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DFE;
ALTER TABLE tl_lascrt11_answer_log ADD CONSTRAINT FK_NEW_610529188_693580A438BF8DFE FOREIGN KEY (`scratchie_answer_uid`)
REFERENCES `tl_lascrt11_scratchie_answer` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

							 	   
ALTER TABLE tl_lascrt11_burning_question DROP FOREIGN KEY sessionIdIndex2;
ALTER TABLE tl_lascrt11_burning_question ADD CONSTRAINT sessionIdIndex2 FOREIGN KEY (`session_id`)
REFERENCES `tl_lascrt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_burning_question DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DF2;
ALTER TABLE tl_lascrt11_burning_question ADD CONSTRAINT FK_NEW_610529188_693580A438BF8DF2 FOREIGN KEY (`scratchie_item_uid`)
REFERENCES `tl_lascrt11_scratchie_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;		 

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;