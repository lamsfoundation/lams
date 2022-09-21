SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190809.sql to patch20200710.sql
-- It should upgrade this tool to version 4.6
--LDEV-4976 Add cascade so when a row from parent table gets removed, a row from inheriting table gets removed too

ALTER TABLE tl_laqa11_usr_resp ADD CONSTRAINT FK_tl_laqa11_usr_resp_1 FOREIGN KEY (uid) 
	REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE;
						  
SET FOREIGN_KEY_CHECKS=1;