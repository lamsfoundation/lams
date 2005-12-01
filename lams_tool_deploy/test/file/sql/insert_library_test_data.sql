# Inserts the tools nb, qa, sbmt.
# Used to test the library deploy, 
# tools must exist before library deploy is to be executed


######## NB Tool ##############


INSERT INTO lams_tool
(
tool_id,
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
1,
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
'tool/lanb11/starter/learner.do',
'tool/lanb11/authoring.do',
'tool/lanb11/starter/authoring.do?defineLater=true',
'tool/lanb11/exportPortfolio.do',
'tool/lanb11/starter/monitor.do',
NULL,
NULL,
NOW()
);

INSERT INTO lams_learning_library
(
learning_library_id,
description,
title,
valid_flag,
create_date_time
)
VALUES
(
1,
'Displays a Noticeboard',
'Noticeboard',
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
, 'Displays a Noticeboard'
, 'Noticeboard'
, 'Put some help text here.'
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
, 'tool/lanb11/images/icon_noticeboard.swf'
, NULL
, NULL
, NULL
);

######## Submit Tool ##############

INSERT INTO lams_tool
(
tool_id,
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
2,
'lasbmt11',
'submitFilesService',
'Submit File',
'Submit File Tool Description',
'submitfile',
'1.1',
NULL,
NULL,
0,
1,
1,
1,
1,
0,
'tool/lasbmt11/learner.do',
'tool/lasbmt11/authoring.do',
'tool/lasbmt11/definelater.do',
'tool/lasbmt11/export.do',
'tool/lasbmt11/monitoring.do',
'tool/lasbmt11/monitoring.do',
'tool/lasbmt11/monitoring.do',
NOW()
);

INSERT INTO lams_learning_library
(
learning_library_id,
description,
title,
valid_flag,
create_date_time
)
VALUES
(
2,
'Uploading of files by learners, for review by teachers.',
'Submit file',
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
, 'Uploading of files by learners, for review by teachers.'
, 'Submit File'
, 'Put some help text here.'
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
, 2
, NOW()
, 0
, NULL
, NULL
, NULL
, 2
, NULL
, 4
, NULL
, NULL
, NULL
, NULL
, NULL
, NULL
, 'tool/lasbmt11/images/icon_reportsubmission.swf'
, NULL
, NULL
, NULL
);

##### QA tool #####

INSERT INTO lams_tool
(
tool_id,
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
3,
'laqa11',
'qaService',
'Question and Answer',
'Question and Answer Tool Description',
'qa',
'1.1',
NULL,
10,
0,
1,
1,
1,
1,
0,
'tool/laqa11/learningStarter.do',
'tool/laqa11/authoringStarter.do',
'tool/laqa11/definelater.do',
'tool/laqa11/export.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/monitoringStarter.do',
NOW()
);

INSERT INTO lams_learning_library
(
learning_library_id,
description,
title,
valid_flag,
create_date_time
)
VALUES
(
3,
'Question and Answer Learning Library Description',
'Question and Answer',
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
, 'Question and Answer Description'
, 'Question and Answer'
, 'Put some help text here.'
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
, 3
, NOW()
, 0
, NULL
, NULL
, NULL
, 3
, NULL
, 4
, NULL
, NULL
, NULL
, NULL
, NULL
, NULL
, 'tool/laqa11/images/icon_questionanswer.swf'
, NULL
, NULL
, NULL
);



