SET FOREIGN_KEY_CHECKS=0;


-- Populates the lams_learning_library table with default libraries

insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (1,'Notebook','Notebook','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (2,'NoticeBoard','NoticeBoard','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (3,'Multiple Choice Questions','Multiple Choice','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (4,'Questions and Answer','Question And Answer','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (5,'Message Board','Message Board','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (6,'Survey','Survey','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (7,'Chat','Chat','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (8,'Shared Resources','Shared Resources','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (9,'Voting','Voting','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (10,'Submit Files','Submit Files','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (11,'Instant Messaging','Instant Messaging','20050207');

-- Populates the lams_tool table with default tools corresponding to each of the above libraries

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(1,'notebook_signature','notebookService','Notebook','Notebook Description','notebook','1.1',1,
2,0,1,0,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,1);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(2,'noticeboard_signature','noticeboardService','Noticeboard','Noticeboard Description','noticeboard','1.1',2,
2,1,1,0,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,2);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(3,'multiple_choice_signature','multipleChoiceService','Multiple Choice','Multiple Coice Description','multiple choice','1.1',3,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,3);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(4,'question_answer_signature','questionAnswerService','Question and Answers','Question and Answers Description','q & a','1.1',4,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,4);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(5,'message_board_signature','messageBoardService','Message Board','Message Board Description','forum','1.1',5,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,5);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(6,'survey_signature','srvyService','Survey','Survey Description','survey','1.1',6,
2,1,1,1,1,
'/lams_tool_survey/tool/survey/survey.do?method=loadQuestionnaire','/lams_tool_survey/tool/survey/authoring.do?method=loadSurvey','define_later_url','export_portfolio_url',
'/lams_tool_survey/tool/survey/report.do?method=loadMonitorReport','contribute_url','moderation_url',
1,6);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(7,'chat_signature','chatService','Chat','Chat Description','chat','1.1',7,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,7);

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(8,'shared_resources_signature','sharedResourcesService','Shared Resources','Shared Resources Description','shared resources','1.1',8,
2,1,0,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,8);


insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(9,'voting_signature','votingService','Voting','Voting Description','voting','1.1',9,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,9);


insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(10,'submit_files_signature','submitFilesService','Submit Files','Submit Files Description','submit files','1.1',10,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,10);


insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,tool_identifier,tool_version,default_tool_content_id,
grouping_support_type_id,supports_define_later_flag,supports_run_offline_flag,supports_moderation_flag,supports_contribute_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url,contribute_url,moderation_url,
valid_flag,learning_library_id)
values(11,'instant_messaging_signature','instantMessagingService','Instant Messaging','Instant Messaging Description','instant messaging','1.1',11,
2,1,1,1,1,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url','contribute_url','moderation_url',
1,11);

-- Populates the lams_tool_content table with dummy default content for all above tools

insert into lams_tool_content (tool_content_id,tool_id) values (1,1);
insert into lams_tool_content (tool_content_id,tool_id) values (2,2);
insert into lams_tool_content (tool_content_id,tool_id) values (3,3);
insert into lams_tool_content (tool_content_id,tool_id) values (4,4);
insert into lams_tool_content (tool_content_id,tool_id) values (5,5);
insert into lams_tool_content (tool_content_id,tool_id) values (6,6);
insert into lams_tool_content (tool_content_id,tool_id) values (7,7);
insert into lams_tool_content (tool_content_id,tool_id) values (8,8);
insert into lams_tool_content (tool_content_id,tool_id) values (9,9);
insert into lams_tool_content (tool_content_id,tool_id) values (10,10);
insert into lams_tool_content (tool_content_id,tool_id) values (11,11);
insert into lams_tool_content (tool_content_id,tool_id) values (12,1);
insert into lams_tool_content (tool_content_id,tool_id) values (13,2);
insert into lams_tool_content (tool_content_id,tool_id) values (14,3);
insert into lams_tool_content (tool_content_id,tool_id) values (15,4);
insert into lams_tool_content (tool_content_id,tool_id) values (16,5);
insert into lams_tool_content (tool_content_id,tool_id) values (17,6);
insert into lams_tool_content (tool_content_id,tool_id) values (18,7);
insert into lams_tool_content (tool_content_id,tool_id) values (19,8);
insert into lams_tool_content (tool_content_id,tool_id) values (20,4);
insert into lams_tool_content (tool_content_id,tool_id) values (21,4);
insert into lams_tool_content (tool_content_id,tool_id) values(22,4);
insert into lams_tool_content (tool_content_id,tool_id) values(23,2);
insert into lams_tool_content (tool_content_id,tool_id) values(24,7);
insert into lams_tool_content (tool_content_id,tool_id) values(25,3);
insert into lams_tool_content (tool_content_id,tool_id) values(26,6);

--insert a testing grouping
insert into lams_grouping values(99,99,1,2,-1,0,-1);
insert into lams_grouping values(100,100,2,1,-1,0,-1);		
INSERT INTO lams_grouping values(101, NULL, 3, NULL, NULL, 89, NULL);

insert into lams_group values(87,99,1);
insert into lams_group values(88,100,1);
insert into lams_group values(89, 101, 1);
insert into lams_group values(90, 101, 1);

insert into lams_user_group values(1,87);
insert into lams_user_group values(1,88);
insert into lams_user_group values(2,88);
insert into lams_user_group values(2, 90);
insert into lams_user_group values(3, 89);

insert into lams_lesson values (1, 1, 1,'Test_Lesson','This is for testing', '2005-02-23 17:37:25', 1, 101,3, NULL,'2005-02-25 17:37:25',NULL,'2005-02-26 17:37:25' );

-- Populates the lams_learning_activity table with default activity templates that would be avaialbe to the
-- author in the left panel and a default dummy learning_design defination 

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(1,1,'Notebook Template','Notebook',10,20,null,'Help Text for Activity',
1,null,0,0,
null,1,'20050101',0,
null,null,1,1,
null,null,null,'/images/icon_journal.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(2,2,'NoticeBoard Template','NoticeBoard',10,20,null,'Help Text for Activity',
1,null,0,0,
null,2,'20050101',0,
null,null,2,2,
null,null,null,'/images/icon_noticeboard.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(3,3,'Multiple Choice Template','Multiple Choice',10,20,null,'Help Text for Activity',
1,null,0,0,
null,3,'20050101',0,
null,null,3,3,
null,null,null,'missing.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(4,4,'Question and Answer Template','Question and Answer',10,20,null,'Help Text for Activity',
1,null,0,0,
null,4,'20050101',0,
null,null,4,4,
null,null,null,'tool/laqa/images/icon_questionanswer.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(5,5,'MessageBoard Template','MessageBoard',10,20,null,'Help Text for Activity',
1,null,0,0,
null,5,'20050101',0,
null,null,5,5,
null,null,null,'/images/icon_messageboard.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(6,6,'Survey Template','Survey',10,20,null,'Help Text for Activity',
1,null,0,0,
null,6,'20050101',0,
null,null,6,6,
null,null,null,'/images/icon_survey.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(7,7,'Chat Template','Chat',10,20,null,'Help Text for Activity',
1,null,0,0,
null,7,'20050101',0,
null,null,7,7,
null,null,null,'/images/icon_chat.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(8,8,'Shared Resources Template','Shared Resources',10,20,null,'Help Text for Activity',
1,null,0,0,
null,8,'20050101',0,
null,null,8,8,
null,null,null,'/images/icon_singleresource.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(9,9,'Voting Template','Voting',10,20,null,'Help Text for Activity',
1,null,0,0,
null,9,'20050101',0,
null,null,9,9,
null,null,null,'/images/icon_ranking.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(10,10,'Submit Files Template','Submit Files',10,20,null,'Help Text for Activity',
1,null,0,0,
null,10,'20050101',0,
null,null,10,10,
null,null,null,'/images/icon_reportsubmission.swf',
1,0,1);

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(11,11,'Instant Messaging Template','Instant Messaging',10,20,null,'Help Text for Activity',
1,null,0,0,
null,11,'20050101',0,
null,null,11,11,
null,null,null,'missing.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(12,12,'Optional Activity Description','Optional Activity Title',10,20,null,'Help Text for Activity',
7,null,1,0,
1,null,'20050101',0,
1,1,null,null,
null,'20050101','20050101','missing.swf',
3,1,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(13,13,'Parallel Activity Description','Parallel Activity Title',10,20,null,'Help Text for Activity',
6,null,1,0,
1,null,'20050101',0,
null,null,null,null,
null,null,null,'missing.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(14,14,'Sequence Activity Description','Sequence Activity Title',10,20,null,'Help Text for Activity',
8,null,1,0,
1,null,'20050101',0,
null,null,null,null,
null,null,null,'missing.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(15,1,'Notebook Activity Description','Notebook Activity Title',10,20,null,'Help Text for Activity',
1,null,1,0,
1,1,'20050101',0,
null,null,1,12,
null,null,null,'/images/icon_journal.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(16,2,'NoticeBoard Activity','NoticeBoard Activity',10,20,12,'Help Text for Activity',
1,null,0,0,
1,2,'20050101',0,
null,null,2,13,
null,null,null,'/images/icon_noticeboard.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(17,3,'Multiple Choice Activity ','Multiple Choice',10,20,12,'Help Text for Activity',
1,null,1,0,
1,3,'20050101',0,
null,null,3,14,
null,null,null,'missing.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(18,4,'Question and Answer Activity','Question and Answer',10,20,13,'Help Text for Activity',
1,null,0,1,
1,4,'20050101',0,
null,null,4,15,
null,null,null,'tool/laqa/images/icon_questionanswer.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(19,5,'Message Board Activity','Message Board',10,20,13,'Help Text for Activity',
1,100,1,0,
1,5,'20050101',0,
null,null,5,16,
null,null,null,'/images/icon_messageboard.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(20,6,'Survey Activity','Survey Title',10,20,null,'Help Text for Activity',
1,null,0,1,
1,6,'20050101',0,
null,null,6,17,
null,null,null,'/images/icon_survey.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(21,7,
'Chat Activity Description','Chat Title','Help Text for Chat Activity',
10,20,
null,null,1,
99,99,0,0,
1,7,'20050101',
1,
null,null,null,
7,18,
null,null,
null,null,
null,null,
'/images/icon_chat.swf',
null,null,
7,
2,1,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(22,8,'Shared Resources Activity','Shared Resources',10,20,14,'Help Text for Activity',
1,null,0,0,
1,8,'20050101',0,
null,null,8,19,
null,null,null,'/images/icon_singleresource.swf',
1,0,1);

-- Sample GroupingActivity with RandomGrouping 

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(23,23,
'Random Grouping Activity Description','RandomGroupingActivity Title','Help Text for RandomGroupingActivity',
10,20,
null,null,2,
null,null,0,0,
1,null,'20050101',
0,
null,null,null,
null,null,
null,null,
null,null,
null,null,
'missing.swf',
99,99,
null,
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(24,24,
'QNA Activity Description','QNA Activity Title','Help Text for QNA Activity',
10,20,
null,null,1,
99,99,0,1,
1,4,'20050101',
1,
null,null,null,
4,20,
null,null,
null,null,
null,null,
'tool/laqa/images/icon_questionanswer.swf',
null,null,
4,
2,1,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(25,25,
'QNA Activity Description','QNA Activity Title','Help Text for QNA Activity',
10,20,
14,14,1,
null,null,1,1,
1,4,'20050101',
1,
null,null,null,
4,21,
null,null,
null,null,
null,null,
'tool/laqa/images/icon_questionanswer.swf',
null,null,
4,
1,0,1);



insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(26,26,
'Synch Gate','Synch Gate Activity','Help Text for Synch Gate',
10,20,
null,null,3,
null,null,0,0,
1,null,'20050101',
0,
null,null,null,
null,null,
3,0,
null,null,
null,null,
'missing.swf',
null,null,
null,
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(27,27,
'Schedule Gate','Schedule Gate Activity','Help Text for Schedule Gate',
10,20,
null,null,4,
null,null,0,0,
1,null,'20050101',
0,
null,null,null,
null,null,
3,0,
1,2,
null,null,
'missing.swf',
null,null,
null,
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(28,28,
'Permission Gate','Permission Gate Activity','Help Text for Permission Gate',
10,20,
null,null,5,
null,null,0,0,
1,null,'20050101',
0,
null,null,null,
null,null,
3,0,
null,null,
null,null,
'missing.swf',
null,null,
null,
1,0,1);

-- Populates the lams_learning_transition table with various transitions for the dummy design 

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(1,1,'Survey to Synch gate','Survey to Synch gate',26,20,1,'20050207',26,6);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(2,2,'Synch gate to Notebook','Synch gate to Notebook',15,26,1,'20050207',1,26);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(3,3,'Notebook to GroupingActivity','Notebook to GroupingActivity',23,15,1,'20050207',23,1);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(4,4,'GroupingActivity to Schedule gate','GroupingActivity to Schedule gate',27,23,1,'20050207',27,23);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(5,5,'Schedule gate to Chat','Schedule gate to Chat',21,27,1,'20050207',7,27);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(6,6,'Chat to QNA','Chat to QNA',24,21,1,'20050207',24,7);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(7,7,'QNA to Permission gate','QNA to Permission gate',28,24,1,'20050207',28,24);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(8,8,'Permission gate to OptionsActivity','Permission gate to OptionsActivity',12,28,1,'20050207',12,28);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(6,6,'OptionsActivity to Notebook','OptionsActivity to Notebook',16,12,1,'20050207',2,12);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(7,7,'Notebook to MultipleChoice','Notebook to MultipleChoice',17,16,1,'20050207',3,2);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(8,8,'MultipleChoice to ParallelActivity','MultipleChoice to ParallelActivity',13,17,1,'20050207',13,3);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(9,9,'OptionsActivity to ParallelActivity','OptionsActivity to ParallelActivity',13,12,1,'20050207',13,12);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(9,9,'ParallelActivity to QNA','ParallelActivity to QNA',18,13,1,'20050207',4,13);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(10,10,'QNA to MessageBoard','QNA to MessageBoard',19,18,1,'20050207',5,4);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(10,10,'ParallelActivity to SequenceActivity','ParallelActivity to SequenceActivity',14,13,1,'20050207',14,13);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(12,12,'SequenceActivity to Shared Resources','SequenceActivity to Shared Resources',22,14,1,'20050207',8,14);

--insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
--values(13,13,'Shared Resources to QNA','Shared Resources to QNA',25,22,1,'20050207',25,8);


insert into lams_learning_design(learning_design_ui_id,description,title,first_activity_id,max_id,valid_design_flag,
								 read_only_flag,user_id,help_text,copy_type_id,create_date_time,version,
								 parent_learning_design_id,workspace_folder_id,
								 online_instructions,offline_instructions)
								values
								(1,'Test Learning Design','Test Learning Design title',20,1,1,0,1,
								'Help Text',1,'20041223','1.0',null,2,
								'Online instructions', 'Offline instructions');		

-- insert the test data for the content repository
INSERT INTO lams_cr_credential (credential_id, name, password) VALUES (1, 'atool','atool');
INSERT INTO lams_cr_credential (credential_id, name, password) VALUES (2, 'workspaceManager','flashClient');

INSERT INTO lams_cr_workspace  (workspace_id, name) VALUES (1, 'atoolWorkspace');
INSERT INTO lams_cr_workspace  (workspace_id, name) VALUES (2, 'atoolWorkspace2');
INSERT INTO lams_cr_workspace  (workspace_id, name) VALUES (3, 'flashClientsWorkspace');


INSERT INTO lams_cr_workspace_credential  (wc_id, workspace_id, credential_id) VALUES  (1, 1,1);
INSERT INTO lams_cr_workspace_credential  (wc_id, workspace_id, credential_id) VALUES  (2, 2,1);
INSERT INTO lams_cr_workspace_credential  (wc_id, workspace_id, credential_id) VALUES  (3, 3,2);

-- SubmitFiles Tool related information
INSERT INTO lams_cr_credential (credential_id, name, password) VALUES (3, 'SubmitFilesLogin','SubmitFilesPassword');
INSERT INTO lams_cr_workspace  (workspace_id, name) VALUES (4, 'SubmitFilesWorkspace');
INSERT INTO lams_cr_workspace_credential  (wc_id, workspace_id, credential_id) VALUES  (4,4,3);

INSERT INTO lams_cr_node (node_id, workspace_id, type, created_date_time, path, next_version_id) VALUES (1, 1, 'DATANODE', 20050106103100, "/test",3);
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, created_date_time)
VALUES (1, 1, 1, 20050106103100);
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, created_date_time)
VALUES (2, 1, 2, 20050106110000);

INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (1,1,"APPSPEC","abcd",1);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (2,1,"VERSIONDESC","Initial Version.",1);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (3,2,"APPSEC","The quick brown fox jumped over the lazy dog.",1);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (4,2,"VERSIONDESC","The second version.",1);

-- Note : this file node is missing its file as can't setup the repository
INSERT INTO lams_cr_node (node_id, workspace_id, type, created_date_time, path, next_version_id) VALUES (2, 1, 'FILENODE', 20050106103100, "", 2);
INSERT INTO lams_cr_node_version (nv_id, node_id, version_id, created_date_time)
VALUES (3, 2, 1, 20050106103100);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (5,3,"VERSIONDESC","Initial Version.",1);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (6,3,"FILENAME","nofile.txt",1);
INSERT INTO lams_cr_node_version_property( id, nv_id, name, value, type) VALUES (7,3,"MIMETYPE","unknown/unknown",1);														

INSERT INTO lams_workspace_folder_content VALUES (1,1,'Document','Test Description','20050101','20050101',2,NULL,NULL,'DOC');
INSERT INTO lams_workspace_folder_content VALUES (2,1,'Text File','Test Description','20050101','20050101',2,NULL,NULL,'TXT');
INSERT INTO lams_workspace_folder_content VALUES (3,1,'Text File','Test Description','20050101','20050101',3,NULL,NULL,'TXT');
INSERT INTO lams_workspace_folder_content VALUES (4,1,'Gif File','Test Description','20050101','20050101',3,NULL,NULL,'GIF');
INSERT INTO lams_workspace_folder_content VALUES (5,1,'Gif2 File','Test Description','20050101','20050101',3,NULL,NULL,'GIF');
INSERT INTO lams_workspace_folder_content VALUES (6,1,'Text2 File','Test Description','20050101','20050101',3,NULL,NULL,'TXT');

SET FOREIGN_KEY_CHECKS=1;