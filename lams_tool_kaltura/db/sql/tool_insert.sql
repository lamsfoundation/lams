
 
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
pedagogical_planner_url,
help_url,
language_file,
create_date_time,
modified_date_time,
supports_outputs
)
VALUES
(
'lakalt11',
'kalturaService',
'Kaltura',
'Kaltura',
'kaltura',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/lakalt11/learning.do?mode=learner',
'tool/lakalt11/learning.do?mode=author',
'tool/lakalt11/learning.do?mode=teacher',
'tool/lakalt11/authoring.do',
'tool/lakalt11/monitoring.do',
'tool/lakalt11/authoring.do?mode=teacher',
'tool/lakalt11/pedagogicalPlanner.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lakalt11',
'org.lamsfoundation.lams.tool.kaltura.ApplicationResources',
NOW(),
NOW(),
1
)
