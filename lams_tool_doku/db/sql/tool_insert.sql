# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-07 10:42:43
# 
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
learner_url,
learner_preview_url,
learner_progress_url,
author_url,
monitor_url,
pedagogical_planner_url,
help_url,
language_file,
create_date_time,
modified_date_time,
admin_url
)
VALUES
(
'ladoku11',
'dokumaranService',
'doKumaran',
'Collaborative real-time document editor',
'dokumaran',
'@tool_version@',
NULL,
NULL,
0,
2,
'tool/ladoku11/learning/start.do?mode=learner',
'tool/ladoku11/learning/start.do?mode=author',
'tool/ladoku11/learning/start.do?mode=teacher',
'tool/ladoku11/authoring/start.do',
'tool/ladoku11/monitoring/summary.do',
NULL,
'http://wiki.lamsfoundation.org/display/lamsdocs/ladoku11',
'org.lamsfoundation.lams.tool.dokumaran.ApplicationResources',
NOW(),
NOW(),
NULL
)
