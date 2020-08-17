insert into lams_learning_activity
(description,title,parent_activity_id,
learning_activity_type_id,
create_date_time,
tool_id,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,
language_file)
values
('Scribe Tool','Scribe Tool',${parent_activity_id},
1,
now(),
${tool_id},
'tool/lascrb11/images/icon_scribe.svg',
2,0,
'org.lamsfoundation.lams.tool.scribe.ApplicationResources');