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
'lanb11',
'nbService',
'Noticeboard',
'Displays a Noticeboard',
'nb',
'1.1',
NULL,
NULL,
0,
1,
1,
1,
0,
0,
'/lams/tool/lanb11/starter/learner.do',
'/lams/tool/lanb11/starter/authoring.do',
'/lams/tool/lanb11/starter/authoring.do?defineLater=true',
'/lams/tool/lanb11/exportPortfolio.do',
'/lams/tool/lanb11/starter/monitor.do',
NULL,
NULL,
NOW()
)
