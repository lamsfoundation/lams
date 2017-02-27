insert into lams_learning_activity
(description,title,parent_activity_id,help_text,
learning_activity_type_id,
create_date_time,
tool_id,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id,
language_file)
values
('Chat Tool','Chat Tool',${parent_activity_id},'Chat',
1,
now(),
${tool_id},
'tool/lachat11/images/icon_chat.svg',
2,0,2,
'org.lamsfoundation.lams.tool.chat.ApplicationResources');
