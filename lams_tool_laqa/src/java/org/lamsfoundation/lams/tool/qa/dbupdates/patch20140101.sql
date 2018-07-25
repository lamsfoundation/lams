-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_laqa11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_laqa11_session ADD COLUMN qa_group_leader_uid BIGINT;
ALTER TABLE tl_laqa11_session ADD INDEX FK_laqa11_session1 (qa_group_leader_uid), ADD CONSTRAINT FK_laqa11_session1 FOREIGN KEY (qa_group_leader_uid) REFERENCES tl_laqa11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the QA user table so no same que_usr_id and qa_session_id can be repetead 
-- In order to achieve this remove duplicate users and according responses. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_laqa11_que_usr GROUP BY que_usr_id, qa_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_laqa11_usr_resp WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_laqa11_que_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_laqa11_que_usr ADD UNIQUE INDEX(que_usr_id, qa_session_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
