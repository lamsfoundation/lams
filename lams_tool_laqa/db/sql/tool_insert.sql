-- Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
INSERT INTO lams_tool
(
tool_signature,
service_name,
tool_display_name,
description,
tool_identifier,
tool_version,
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
supports_outputs,
admin_url
)
VALUES
(
'laqa11',
'qaService',
'Question and Answer',
'Q/A Tool',
'qa',
'@tool_version@',
0,
2,
'tool/laqa11/learningStarter.do?mode=learner',
'tool/laqa11/learningStarter.do?mode=author',
'tool/laqa11/learningStarter.do?mode=teacher',
'tool/laqa11/authoringStarter.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/pedagogicalPlanner.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/laqa11',
'org.lamsfoundation.lams.tool.qa.ApplicationResources',
NOW(),
NOW(),
1,
'tool/laqa11/laqa11admin.do'
)