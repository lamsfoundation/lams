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
'lantbk11',
'notebookService',
'Notebook',
'Notebook',
'notebook',
'@tool_version@',
0,
2,
'tool/lantbk11/learning.do?mode=learner',
'tool/lantbk11/learning.do?mode=author',
'tool/lantbk11/learning.do?mode=teacher',
'tool/lantbk11/authoring.do',
'tool/lantbk11/monitoring.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lantbk11',
'org.lamsfoundation.lams.tool.notebook.ApplicationResources',
NOW(),
NOW(),
1
)