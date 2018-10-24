-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3984 Rename actions to have more simple names
 
UPDATE lams_tool SET learner_url = 'tool/lavote11/learning.do?dispatch=start&mode=learner',
					 learner_preview_url = 'tool/lavote11/learning.do?dispatch=start&mode=author',
					 learner_progress_url = 'tool/lavote11/learning.do?dispatch=start&mode=teacher'
WHERE tool_signature = 'lavote11';

UPDATE lams_tool SET monitor_url = 'tool/lavote11/monitoring.do?dispatch=start'
WHERE tool_signature = 'lavote11';

UPDATE lams_tool SET author_url = 'tool/lavote11/authoring.do?dispatch=start'
WHERE tool_signature = 'lavote11';



----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;