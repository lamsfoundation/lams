-- Script to be run for LAMS 2.1 release, on LAMS 2.0.3 tables.
-- Adds all the data needed for branching

INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCHING_CHOSEN');
INSERT INTO lams_learning_activity_type VALUES (11, 'BRANCHING_GROUP');
INSERT INTO lams_learning_activity_type VALUES (12, 'BRANCHING_TOOL');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url,
        export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (6, 10, 'Monitor Chosen Branching', 'Select between multiple sequence activities, with the branch chosen in monitoring.',
        'learning/branching.do?method=performBranching&type=chosen', 'learning/branching.do?method=performBranching&type=chosen',
        'learning/branching.do?method=viewBranching&mode=teacher&type=chosen', 'learning/branchingExportPortfolio?mode=learner&type=chosen',
        'learning/branchingExportPortfolio?mode=teacher&type=chosen', 'monitoring/branching.do?method=assignBranch&type=chosen',
        'monitoring/branching.do?method=assignBranch&type=chosen', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url,
        export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (7, 11, 'Group Based Branching', 'Select between multiple sequence activities, with the branch chosen by an existing group.',
        'learning/branching.do?method=performBranching&type=group', 'learning/branching.do?method=performBranching&type=group',
        'learning/branching.do?method=viewBranching&mode=teacher&type=group', 'learning/branchingExportPortfolio?mode=learner&type=group',
        'learning/branchingExportPortfolio?mode=teacher&type=group', 'monitoring/branching.do?method=assignBranch&type=group',
        'monitoring/branching.do?method=assignBranch&type=group', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url,
        export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (8, 12, 'Tool Output Based Branching', 'Select between multiple sequence activities, with the branch chosen on results of another activi\
ty.',
        'learning/branching.do?method=performBranching&type=tool', 'learning/branching.do?method=performBranching&type=tool',
        'learning/branching.do?method=viewBranching&mode=teacher&type=tool', 'learning/branchingExportPortfolio?mode=learner&type=tool',
        'learning/branchingExportPortfolio?mode=teacher&type=tool', 'monitoring/branching.do?method=assignBranch&type=tool',
        'monitoring/branching.do?method=assignBranch&type=tool', now());
