SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-3366: Fix Learning Library IDs incorrectly imported from Learning Designs

UPDATE lams_learning_activity AS act, lams_tool AS tool
SET act.learning_library_id = tool.learning_library_id
WHERE act.tool_id = tool.tool_id AND act.learning_library_id <> tool.learning_library_id;


COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;