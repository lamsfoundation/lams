-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_lamc11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lamc11_session ADD COLUMN mc_group_leader_uid BIGINT;
ALTER TABLE tl_lamc11_session ADD INDEX FK_lamc11_session1 (mc_group_leader_uid), ADD CONSTRAINT FK_lamc11_session1 FOREIGN KEY (mc_group_leader_uid) REFERENCES tl_lamc11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put automatic letters in front of MCQ answers
ALTER TABLE tl_lamc11_content ADD COLUMN prefix_answers_with_letters TINYINT(1) NOT NULL DEFAULT 1;

-- Adding a constraint to the MCQ user table so no same user_id and session_id can be repetead. 
-- In order to achieve this remove duplicate users and according attempts. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_lamc11_que_usr GROUP BY que_usr_id, mc_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_lamc11_que_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_que_usr ADD UNIQUE INDEX(que_usr_id, mc_session_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
