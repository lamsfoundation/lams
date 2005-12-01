
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
'DPTST1',
'test.test.deploy.Test',
'DEPLOY TEST TOOL 1',
'TEST DATA FOR DEPLOY APP',
'DPTST1',
'1.1',
NULL,
NULL,
0,
1,
1,
1,
0,
0,
'tool/dptst1/foo.jsp',
'tool/dptst1/foo.jsp',
'tool/dptst1/foo.jsp',
'tool/dptst1/foo.jsp',
'tool/dptst1/foo.jsp',
NULL,
NULL,
NOW()
);



INSERT INTO lams_learning_library
(
description,
title,
valid_flag,
create_date_time
)
VALUES
(
'TEST DEPLOY TOOL LIBRARY',
'Test Deploy Library',
0,
NOW()
);




INSERT INTO lams_learning_activity
(
activity_ui_id
, description
, title
, help_text
, xcoord
, ycoord
, parent_activity_id
, parent_ui_id
, learning_activity_type_id
, grouping_support_type_id
, apply_grouping_flag
, grouping_id
, grouping_ui_id
, order_id
, define_later_flag
, learning_design_id
, learning_library_id
, create_date_time
, run_offline_flag
, max_number_of_options
, min_number_of_options
, options_instructions
, tool_id
, tool_content_id
, activity_category_id
, gate_activity_level_id
, gate_open_flag
, gate_start_time_offset
, gate_end_time_offset
, gate_start_date_time
, gate_end_date_time
, library_activity_ui_image
, create_grouping_id
, create_grouping_ui_id
, library_activity_id
)
VALUES
(
NULL
, 'TEST DEPLOY TOOL ACTIVITY'
, 'Test Deploy Tool'
, 'Does this help?'
, NULL
, NULL
, NULL
, NULL
, 1
, 1
, 0
, NULL
, NULL
, NULL
, 0
, NULL
, 1
, NOW()
, 0
, NULL
, NULL
, NULL
, 1
, NULL
, 4
, NULL
, NULL
, NULL
, NULL
, NULL
, NULL
, '/lams/tool/dptst1/images/tool.gif'
, NULL
, NULL
, NULL
);