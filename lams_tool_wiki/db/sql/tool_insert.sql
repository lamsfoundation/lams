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
help_url,
language_file,
create_date_time,
modified_date_time,
supports_outputs
)
VALUES
(
'lawiki10',
'wikiService',
'Wiki',
'Wiki',
'wiki',
'@tool_version@',
0,
2,
'tool/lawiki10/learning/learning.do?mode=learner',
'tool/lawiki10/learning/learning.do?mode=author',
'tool/lawiki10/learning/learning.do?mode=teacher',
'tool/lawiki10/authoring/authoring.do',
'tool/lawiki10/monitoring/monitoring.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10',
'org.lamsfoundation.lams.tool.wiki.ApplicationResources',
NOW(),
NOW(),
1
)