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
0,
2,
'tool/laimsc11/learning/start.do?mode=learner',
'tool/laimsc11/learning/start.do?mode=author',
'tool/laimsc11/learning/start.do?mode=teacher',
'tool/laimsc11/authoring/start.do',
'tool/laimsc11/monitoring/summary.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/laimsc11',
'org.lamsfoundation.lams.tool.commonCartridge.ApplicationResources',
NOW(),
NOW(),
'tool/laimsc11/laimsc11admin/start.do'
)