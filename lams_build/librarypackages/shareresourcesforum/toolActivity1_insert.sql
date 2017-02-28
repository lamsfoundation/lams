insert into lams_learning_activity
(description,title,parent_activity_id,
learning_activity_type_id,
create_date_time,
tool_id,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id,
language_file)
values
('Share Resources','Share Resources',${parent_activity_id},
1,
now(),
${tool_id},
'tool/larsrc11/images/icon_rsrc.svg',
2,0,4,
'org.lamsfoundation.lams.tool.rsrc.ApplicationResources');