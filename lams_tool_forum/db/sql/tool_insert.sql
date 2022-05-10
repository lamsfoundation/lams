

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
'lafrum11',
'forumService',
'Forum',
'Forum / Message Boards',
'forum',
'@tool_version@',
0,
2,
'tool/lafrum11/learning/viewForum.do?mode=learner',
'tool/lafrum11/learning/viewForum.do?mode=author',
'tool/lafrum11/learning/viewForum.do?mode=teacher',
'tool/lafrum11/authoring/authoring.do',
'tool/lafrum11/monitoring/monitoring.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lafrum11',
'org.lamsfoundation.lams.tool.forum.ApplicationResources',
NOW(),
NOW(),
1
)
