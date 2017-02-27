insert into lams_learning_activity
(description,title,help_text,
learning_activity_type_id,
learning_library_id,create_date_time,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id, language_file)
values
('Combined Chat and Scribe','Prepare Group Report',
'Learners to prepare group report, discussing report in chat',
6,
${learning_library_id},now(),
'images/icon_groupreporting.svg',
2,0,5,
'org.lamsfoundation.lams.library.chatscribe.ApplicationResources');