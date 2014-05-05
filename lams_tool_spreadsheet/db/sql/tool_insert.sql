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
export_pfolio_learner_url,
export_pfolio_class_url,
help_url,
language_file,
create_date_time,
modified_date_time
)
VALUES
(
'lasprd10',
'spreadsheetService',
'Spreadsheet Tool',
'Spreadsheet Tool',
'spreadsheet',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/lasprd10/learning/start.do?mode=learner',
'tool/lasprd10/learning/start.do?mode=author',
'tool/lasprd10/learning/start.do?mode=teacher',
'tool/lasprd10/authoring/start.do',
'tool/lasprd10/monitoring/summary.do',
'tool/lasprd10/definelater.do',
'tool/lasprd10/exportPortfolio?mode=learner',
'tool/lasprd10/exportPortfolio?mode=teacher',
'http://wiki.lamsfoundation.org/display/lamsdocs/lasprd10',
'org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources',
NOW(),
NOW()
)
