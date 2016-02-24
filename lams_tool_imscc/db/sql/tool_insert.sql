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
admin_url
)
VALUES
(
'laimsc11',
'commonCartridgeService',
'Shared CommonCartridge',
'Shared CommonCartridge',
'sharedcommonCartridge',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/laimsc11/learning/start.do?mode=learner',
'tool/laimsc11/learning/start.do?mode=author',
'tool/laimsc11/learning/start.do?mode=teacher',
'tool/laimsc11/authoring/start.do',
'tool/laimsc11/monitoring/summary.do',
'tool/laimsc11/definelater.do',
'tool/laimsc11/authoring/initPedagogicalPlannerForm.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/laimsc11',
'org.lamsfoundation.lams.tool.commonCartridge.ApplicationResources',
NOW(),
NOW(),
'tool/laimsc11/laimsc11admin/start.do'
)
