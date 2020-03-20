SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4989 Introduce Password Gate

ALTER TABLE lams_learning_activity ADD COLUMN gate_password VARCHAR(32) AFTER gate_activity_completion_based;

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, monitor_url,
	contribute_url, create_date_time)
VALUES (12, 16, 'Password Gate', 'Gate: Opens if learner provides correct password', 
	'learning/gate/knockGate.do', 'learning/gate/knockGate.do', 'monitoring/gate/viewGate.do',
	'monitoring/gate/viewGate.do', now());
	
INSERT INTO lams_learning_activity_type (learning_activity_type_id, description)
VALUES (16, 'GATE_PASSWORD');

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;

