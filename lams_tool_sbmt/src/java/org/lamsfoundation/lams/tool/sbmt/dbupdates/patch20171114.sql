-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4482


ALTER TABLE tl_lasbmt11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE tl_lasbmt11_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_lasbmt11_session ADD INDEX FK_lasbmt11_session_1 (group_leader_uid), ADD CONSTRAINT tl_lasbmt11_session FOREIGN KEY (group_leader_uid) REFERENCES tl_lasbmt11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the assessment user table so no same user_id and session_id can be repetead 
-- (Please, note there are some duplicate users exist (their session_uid is null), they got formed when techers created questions in authoring. It's required to keep track of questions' authors.) 
ALTER TABLE tl_lasbmt11_user ADD UNIQUE INDEX(user_id, session_id);
 

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;