-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3707 Use Flashless chosen grouping Monitoring interface
UPDATE lams_system_tool SET contribute_url = 'monitoring/grouping.do?method=startGrouping' WHERE tool_display_name = 'Monitor Chosen Branching';

COMMIT;
SET AUTOCOMMIT = 1;