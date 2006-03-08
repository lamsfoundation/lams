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
define_later_url,
export_portfolio_url,
monitor_url,
contribute_url,
moderation_url,
create_date_time
)
VALUES
(
'laqa11',
'qaService',
'Question and Answer',
'Question and Answer Tool Description',
'qa',
'1.1',
NULL,
10,
0,
2,
1,
1,
1,
0,
'tool/laqa11/learningStarter.do',
'tool/laqa11/authoringStarter.do',
'tool/laqa11/defineLaterStarter.do',
'tool/laqa11/export.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/monitoring.do',
'tool/laqa11/monitoring.do',
NOW()
)
