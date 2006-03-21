-- CVS ID: $Id$

insert into lams_learning_design(learning_design_ui_id,description,title,first_activity_id,max_id,valid_design_flag,
								 read_only_flag,user_id,help_text,copy_type_id,create_date_time,version,
								 parent_learning_design_id,workspace_folder_id)
								values
								(10,'Test Survey Learning Design','Test survey only design',100,1,1,0,1,
								'Help Text',1,'20041223','1.0',null,1);	

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(100,6,'Survey Template','Survey',10,20,null,'Help Text for Activity',
1,null,0,0,
2,6,'20050101',0,
null,null,6,17,
null,null,null,'SurveyImage',
1,0,1);

