insert into lams_learning_activity
(description,title,
learning_activity_type_id,
learning_library_id,create_date_time,
library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag, language_file)
values
('Combined Share Resources and Forum','Discuss Shared Resources',
6,
${learning_library_id},now(),
'images/icon_urlcontentmessageboard.svg',
2,0,
'org.lamsfoundation.lams.library.shareresourcesforum.ApplicationResources');