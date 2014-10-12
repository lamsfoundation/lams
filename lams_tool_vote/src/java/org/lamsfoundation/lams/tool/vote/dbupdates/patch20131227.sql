-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV
ALTER TABLE tl_lavote11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lavote11_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_lavote11_session ADD INDEX FK_lavote11_session_1 (group_leader_uid), ADD CONSTRAINT tl_lavote11_session FOREIGN KEY (group_leader_uid) REFERENCES tl_lavote11_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the vote user table so no same user_id and vote_session_id can be repetead 
-- In order to achieve this remove duplicate users and according attempts. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_lavote11_usr GROUP BY user_id, vote_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lavote11_usr_attempt WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_lavote11_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lavote11_usr ADD UNIQUE INDEX(user_id, vote_session_id);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
