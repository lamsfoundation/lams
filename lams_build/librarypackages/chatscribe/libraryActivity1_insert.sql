insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id, language_file)
values
(null,null,'Combined Chat and Scribe','Prepare Group Report',null,null,null,
'Learners to prepare group report, discussing report in chat',
6,null,null,0,
null,${learning_library_id},now(),0,
null,null,null,null,
null,null,null,'images/icon_groupreporting.svg',
2,0,5,
'org.lamsfoundation.lams.library.chatscribe.ApplicationResources');