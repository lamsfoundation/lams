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
'eueadv10',
'eadventureService',
'eAdventure',
'eAdventure',
'eAdventure game',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/eueadv10/learning/start.do?mode=learner',
'tool/eueadv10/learning/start.do?mode=author',
'tool/eueadv10/learning/start.do?mode=teacher',
'tool/eueadv10/authoring/start.do',
'tool/eueadv10/monitoring/summary.do',
'tool/eueadv10/definelater.do',
'tool/eueadv10/authoring/initPedagogicalPlannerForm.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/eueadv10',
'org.eucm.lams.tool.eadventure.ApplicationResources',
NOW(),
NOW(),
1
)
