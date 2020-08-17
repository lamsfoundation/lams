insert into lams_learning_activity
(description,title,
learning_activity_type_id,
learning_library_id,create_date_time,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag, language_file)
values
('Combined Chat and Scribe','Prepare Group Report',
6,
${learning_library_id},now(),
'images/icon_groupreporting.svg',
2,0,
'org.lamsfoundation.lams.library.chatscribe.ApplicationResources');