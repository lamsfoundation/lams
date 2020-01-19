-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

UPDATE lams_tool SET pedagogical_planner_url='tool/latask10/authoring/initPedagogicalPlannerForm.do' WHERE tool_signature='latask10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;