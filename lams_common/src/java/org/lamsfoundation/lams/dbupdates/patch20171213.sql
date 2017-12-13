-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4485 Remove ratings when whole lesson is removed
ALTER TABLE lams_rating DROP FOREIGN KEY FK_lams_rating_3;
ALTER TABLE lams_rating ADD CONSTRAINT FK_lams_rating_3 FOREIGN KEY (tool_session_id) REFERENCES lams_tool_session (tool_session_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE lams_rating_comment DROP FOREIGN KEY FK_lams_rating_comment_3;
ALTER TABLE lams_rating_comment ADD CONSTRAINT FK_lams_rating_comment_3 FOREIGN KEY (tool_session_id) REFERENCES lams_tool_session (tool_session_id) ON DELETE CASCADE ON UPDATE CASCADE;		

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;