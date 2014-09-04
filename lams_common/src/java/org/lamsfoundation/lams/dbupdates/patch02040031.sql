SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-3292 Add index on Activity table

CREATE INDEX lams_learning_activity_tool_content_id ON lams_learning_activity(tool_content_id);

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;