-- $Id$

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
contribute_url,
moderation_url,
language_file,
create_date_time
)
VALUES
(
'laqa11',
'qaService',
'Question and Answer',
'Q/A Tool',
'qa',
'1.1',
NULL,
NULL,
0,
2,
1,
'tool/laqa11/learningStarter.do?mode=learner',
'tool/laqa11/learningStarter.do?mode=author',
'tool/laqa11/learningStarter.do?mode=teacher',
'tool/laqa11/authoringStarter.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/defineLaterStarter.do',
'tool/laqa11/exportPortfolio?mode=learner',
'tool/laqa11/exportPortfolio?mode=teacher',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/monitoringStarter.do',
'org.lamsfoundation.lams.tool.qa.ApplicationResources',
NOW()
)
