-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4440 Update system tool access URLs after migration to Spring MVC
UPDATE lams_system_tool SET monitor_url = 'monitoring/grouping/startGrouping.do', contribute_url = 'monitoring/grouping/startGrouping.do' WHERE system_tool_id = '1';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '2';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '3';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '4';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '5';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/chosenBranching.do?method=assignBranch', contribute_url = 'monitoring/grouping/startGrouping.do' WHERE system_tool_id = '6';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/groupedBranching.do?method=viewBranching', contribute_url = 'monitoring/groupedBranching.do?method=assignBranch' WHERE system_tool_id = '7';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/toolBranching.do?method=viewBranching', contribute_url = 'monitoring/toolBranching.do?method=viewBranching' WHERE system_tool_id = '8';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/sequence/viewSequence.do', contribute_url = 'monitoring/sequence/viewSequence.do' WHERE system_tool_id = '9';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
