
 
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
admin_url,
language_file,
create_date_time,
modified_date_time
)
VALUES
(
'lazoom10',
'zoomService',
'Zoom',
'Web conferencing using Zoom',
'zoom',
'@tool_version@',
0,
2,
'tool/lazoom10/learning.do?mode=learner',
'tool/lazoom10/learning.do?mode=author',
'tool/lazoom10/learning.do?mode=teacher',
'tool/lazoom10/authoring.do',
'tool/lazoom10/monitoring.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lazoom10',
'tool/lazoom10/admin/view.do',
'org.lamsfoundation.lams.tool.zoom.ApplicationResources',
NOW(),
NOW()
)
