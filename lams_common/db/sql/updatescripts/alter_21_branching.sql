-- Script to be run for LAMS 2.1 release, on LAMS 2.0.3 tables.
-- Adds all the data needed for branching

INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCHING');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (6, 10, 'Branching', 'Contains multiple sequence activities.', 
	'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching', 
	'learning/branching.do?method=viewBranching&mode=teacher', 'learning/branchingExportPortfolio?mode=learner',
	'learning/branchingExportPortfolio?mode=teacher', 'monitoring/branching.do?method=startBranching', 
	'monitoring/branching.do?method=startBranching', now()	);
