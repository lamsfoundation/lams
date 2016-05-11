

INSERT INTO lams_tool
(
tool_signature,
service_name,
tool_display_name,
description,
tool_identifier,
tool_version,
learning_library_id,
default_tool_content_id,
valid_flag,
grouping_support_type_id,
supports_run_offline_flag,
learner_url,
learner_preview_url,
learner_progress_url,
author_url,
monitor_url,
define_later_url,
export_pfolio_learner_url,
export_pfolio_class_url,
pedagogical_planner_url,
help_url,
language_file,
create_date_time,
modified_date_time
)
VALUES
(
'lasbmt11',
'submitFilesService',
'Submit File',
'Submit File Tool Description',
'submitfile',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/lasbmt11/learner.do?mode=learner',
'tool/lasbmt11/learner.do?mode=author',
'tool/lasbmt11/learner.do?mode=teacher',
'tool/lasbmt11/authoring.do',
'tool/lasbmt11/monitoring.do',
'tool/lasbmt11/definelater.do?mode=teacher',
'tool/lasbmt11/exportPortfolio?mode=learner',
'tool/lasbmt11/exportPortfolio?mode=teacher',
'tool/lasbmt11/pedagogicalPlanner.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lasbmt11',
'org.lamsfoundation.lams.tool.sbmt.ApplicationResources',
NOW(),
NOW()
);



