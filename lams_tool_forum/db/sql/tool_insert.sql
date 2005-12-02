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
supports_define_later_flag,
supports_run_offline_flag,
supports_moderation_flag,
supports_contribute_flag,
learner_url,
author_url,
monitor_url,
define_later_url,
export_portfolio_url,
contribute_url,
moderation_url,
create_date_time
)
VALUES
(
'lafrum11',
'forumService',
'Forum',
'Forum / Message Boards',
'forum',
'1.1',
NULL,
NULL,
0,
1,
1,
1,
1,
0,
'tool/lafrum11/learning/viewForum.do',
'tool/lafrum11/authoring/init.do',
'tool/lafrum11/monitoring/listContentUsers.do',
'tool/lafrum11/definelater.do',
'tool/lafrum11/export.do',
'tool/lafrum11/contribute.do',
'tool/lafrum11/moderate.do',
NOW()
)
