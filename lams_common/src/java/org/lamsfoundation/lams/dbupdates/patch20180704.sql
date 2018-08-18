-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4620 Audit Log entry for Marks released in tool
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(23, 'TYPE_TOOL_MARK_RELEASED', 'MARKS');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;