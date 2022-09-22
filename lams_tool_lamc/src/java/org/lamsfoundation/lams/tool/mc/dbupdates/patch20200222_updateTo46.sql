SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190517.sql to patch20200222.sql
-- It should upgrade this tool to version 4.6

--LDEV-4821 remove obsolete hash field
ALTER TABLE tl_lamc11_que_content DROP COLUMN question_hash;

--LDEV-4921 Allow question title hiding in learner
 CREATE TABLE `tl_lamc11_configuration` (
  `config_key` varchar(30),
  `config_value` varchar(255),
  PRIMARY KEY (`config_key`)
 );

INSERT INTO `tl_lamc11_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

UPDATE lams_tool SET admin_url = 'tool/lamc11/admin/start.do' WHERE tool_signature = 'lamc11';

--LDEV-4976 Introduce an extra column which holds the same information as parent table, then add unique index to prevent duplicates
ALTER TABLE tl_lamc11_usr_attempt ADD COLUMN qb_tool_question_uid BIGINT AFTER uid;

UPDATE tl_lamc11_usr_attempt AS a JOIN lams_qb_tool_answer AS q ON a.uid = q.answer_uid
SET a.qb_tool_question_uid = q.tool_question_uid;

ALTER TABLE tl_lamc11_usr_attempt MODIFY COLUMN qb_tool_question_uid BIGINT NOT NULL,
								  ADD UNIQUE INDEX IDX_attempt_duplicate_prevent (qb_tool_question_uid, que_usr_id);

--LDEV-4976 Add cascade so when a row from parent table gets removed, a row from inheriting table gets removed too
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_1 FOREIGN KEY (uid) 
	REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE;

								  
SET FOREIGN_KEY_CHECKS=1;