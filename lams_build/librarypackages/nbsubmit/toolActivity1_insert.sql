insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(null,null,'Parallel NB Activity','Parallel NB Activity',null,null,${parent_activity_id},'Help Text for Activity',
1,null,null,0,
null,null,now(),0,
null,null,${tool_id},null,
null,null,null,'tool/lanb11/images/icon_noticeboard.swf',
2,0,4);